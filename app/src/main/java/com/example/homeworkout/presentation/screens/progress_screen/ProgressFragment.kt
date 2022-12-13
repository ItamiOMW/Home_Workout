package com.example.homeworkout.presentation.screens.progress_screen

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.BAR_CHART_ANIMATION_DURATION
import com.example.homeworkout.BuildConfig
import com.example.homeworkout.R
import com.example.homeworkout.databinding.AddWeightDialogBinding
import com.example.homeworkout.databinding.EditUserDialogBinding
import com.example.homeworkout.databinding.FragmentProgressBinding
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.presentation.adapters.user_info_adapter.UserInfoAdapter
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.example.homeworkout.utils.DateFormatterUtil
import com.example.homeworkout.utils.ToastUtil.Companion.makeToast
import com.example.homeworkout.utils.UriFromDrawableUtil
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class ProgressFragment : Fragment() {


    private var _binding: FragmentProgressBinding? = null
    private val binding: FragmentProgressBinding
        get() = _binding ?: throw RuntimeException("FragmentProgressBinding is null")


    private val component by lazy {
        (requireActivity().application as AppWorkout).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCountOfCompletedWorkouts()
    }

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    private lateinit var viewModel: ProgressViewModel

    @Inject
    lateinit var userInfoAdapter: UserInfoAdapter

    private lateinit var dialogAddUser: Dialog

    private lateinit var dialogEditUser: Dialog

    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    //URI OF SELECTED OR CAPTURED IMAGE
    private var latestTmpUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProgressViewModel::class.java]
        setupBarChart(null)
        setupUserInfoAdapter()
        collectUIState()
        setupDatePicker()
        setupDialogAddUser()
        setupOnButtonAddUserInfoClickListener()
        requestPermissions()
    }

    private fun collectUIState() {

        lifecycleScope.launchWhenStarted {
            viewModel.listUserInfo.collect { result ->
                if (result is Response.Loading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                } else {
                    binding.progressBarLoading.visibility = View.GONE
                }
                when (result) {
                    is Response.Success -> {
                        userInfoAdapter.submitList(result.data)
                        setupBarChart(result.data)
                    }
                    is Response.Failed -> {
                        makeToast(requireContext(), result.message)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.state.collect { state ->
                if (state is Loading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                } else {
                    binding.progressBarLoading.visibility = View.GONE
                }
                when (state) {
                    is Failure -> {
                        makeToast(requireContext(), state.message)
                    }
                    is AddedUserInfo -> {
                        makeToast(requireContext(), getString(R.string.added_scfly))
                    }
                    is UpdatedUserInfo -> {
                        makeToast(requireContext(), getString(R.string.updated_scfly))
                    }
                    is DeletedUserInfo -> {
                        makeToast(requireContext(), getString(R.string.deleted_scfly))
                    }
                    is CompletedWorkouts -> {
                        binding.tvCompletedWorkouts.text = String.format(
                            getString(R.string.count_of_workout_completed, state.count.toString()))
                    }
                }
            }
        }

    }

    private fun setupUserInfoAdapter() {

        binding.rvUserInfo.adapter = userInfoAdapter

        userInfoAdapter.onItemClicked = {
            setupDialogEditUser(it)
            dialogEditUser.show()
        }

        userInfoAdapter.onItemLongClicked = {
            setupDialogEditUser(it)
            dialogEditUser.show()
        }

    }

    private fun setupBarChart(listUserInfo: List<UserInfoModel>?) {
        val array: List<BarEntry> = if (listUserInfo != null && listUserInfo.isNotEmpty()) {
            listUserInfo.mapIndexed { index, userInfoModel ->
                BarEntry(index.toFloat() * 10, userInfoModel.weight.toFloat())
            }
        } else {
            listOf(BarEntry(0f, 0f))
        }
        val barDataSer = BarDataSet(array, getString(R.string.weight))
        barDataSer.setDrawValues(false)
        binding.barChart.data = BarData(barDataSer)
        binding.barChart.animateY(BAR_CHART_ANIMATION_DURATION)
        binding.barChart.description.text = getString(R.string.your_weight)
        binding.barChart.description.textColor = Color.BLUE

    }

    private fun setupOnButtonAddUserInfoClickListener() {

        binding.buttonAddUserInfo.setOnClickListener {
            dialogAddUser.show()
        }

    }

    //LAUNCH TO REQUEST PERMISSIONS
    private fun requestPermissions() {
        lifecycleScope.launchWhenStarted {
            requestPermissionsResult.launch(listOfPermissions)
        }
    }

    //REQUESTED PERMISSIONS RESULT
    private val requestPermissionsResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.map {
                if (!it.value) {
                    makeToast(requireContext(), getString(R.string.features_without_permissions))
                }
            }
        }


    //LAUNCH TO CAPTURE IMAGE
    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    //CAPTURED IMAGE URI
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    viewModel.updateImageUri(uri)
                }
            }
        }


    //LAUNCH TO SELECT IMAGE FROM GALLERY
    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    //SELECTED IMAGE URI
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.updateImageUri(uri) }
        }


    private fun getFileUri(): Uri {
        val imageName = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())

        val file = File.createTempFile(imageName, ".png").apply {
            createNewFile()
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            file
        )
    }


    private fun setupDialogAddUser() {
        val addWeightDialogBinding = AddWeightDialogBinding.inflate(layoutInflater)
        dialogAddUser = Dialog(requireContext())
        dialogAddUser.setContentView(addWeightDialogBinding.root)
        dialogAddUser.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialogAddUser.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogAddUser.setCancelable(true)
        dialogAddUser.window?.attributes?.windowAnimations = R.style.window_animation
        setOnDialogAddUserButtonsClickListeners(addWeightDialogBinding, dialogAddUser)
    }


    private fun setOnDialogAddUserButtonsClickListeners(
        binding: AddWeightDialogBinding,
        dialog: Dialog,
    ) {
        with(binding) {

            tvAdd.setOnClickListener {
                viewModel.addUserInfo(
                    getDateFromDatePickerInLong(),
                    etWeight.text.toString(),
                    if (latestTmpUri == null) UriFromDrawableUtil.getUriFromDrawable(
                        requireActivity().application,
                        R.drawable.nfoto)
                    else latestTmpUri.toString()
                )
                dialog.hide()
            }

            ivUserPhoto.setOnClickListener {
                if (checkIfPermissionIsGranted(android.Manifest.permission.CAMERA)) {
                    takeImage()
                } else {
                    makeToast(requireContext(), getString(R.string.permissions_werenot_granted))
                }
            }

            tvDate.setOnClickListener {
                datePickerDialog.show()
            }

            lifecycleScope.launchWhenStarted {
                viewModel.state.collect { state ->
                    when (state) {
                        is ImageUri -> binding.ivUserPhoto.setImageURI(state.uri)
                        is DateForProgressScreen -> binding.tvDate.text = state.date
                    }
                }
            }

        }
    }


    private fun setupDialogEditUser(userInfo: UserInfoModel) {
        val editUserDialogBinding = EditUserDialogBinding.inflate(layoutInflater)
        dialogEditUser = Dialog(requireContext())
        dialogEditUser.setContentView(editUserDialogBinding.root)
        dialogEditUser.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialogEditUser.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogEditUser.setCancelable(true)
        dialogEditUser.window?.attributes?.windowAnimations = R.style.window_animation
        setOnDialogEditUserButtonsClickListeners(editUserDialogBinding, dialogEditUser, userInfo)
    }

    private fun setOnDialogEditUserButtonsClickListeners(
        binding: EditUserDialogBinding,
        dialog: Dialog,
        userInfo: UserInfoModel,
    ) {
        with(binding) {

            tvEdit.setOnClickListener {
                viewModel.updateUserInfo(
                    date = getDateFromDatePickerInLong(),
                    weight = etWeight.text.toString(),
                    photoUri = if (latestTmpUri == null) userInfo.photo else latestTmpUri.toString(),
                    userInfo.firebaseId
                )
                dialog.hide()
            }

            tvDelete.setOnClickListener {
                viewModel.deleteUserInfo(userInfo)
                dialog.hide()
            }

            Glide.with(requireContext()).load(userInfo.photo).into(ivUserPhoto)

            ivUserPhoto.setOnClickListener {
                if (checkIfPermissionIsGranted(android.Manifest.permission.CAMERA)) {
                    takeImage()
                } else {
                    makeToast(requireContext(), getString(R.string.permissions_werenot_granted))
                }
            }

            etWeight.setText(userInfo.weight)

            tvDate.text = DateFormatterUtil.longToTime(userInfo.date)
                .format(DateTimeFormatter.ISO_LOCAL_DATE)

            lifecycleScope.launchWhenStarted {
                viewModel.state.collect { state ->
                    if (state is ImageUri) {
                        binding.ivUserPhoto.setImageURI(state.uri)
                    }
                }
            }

        }
    }

    private fun setupDatePicker() {

        datePickerDialog = DatePickerDialog(requireContext())

        datePickerDialog.datePicker.setOnDateChangedListener { _, year, month, day ->
            val date = DateFormatterUtil.formatDateFromDatePicker(day, month, year)
            viewModel.updateDate(date)
        }

    }

    private fun checkIfPermissionIsGranted(permissionToCheck: String) =
        requireActivity().checkSelfPermission(permissionToCheck) == PackageManager.PERMISSION_GRANTED


    private fun getDateFromDatePickerInLong(): Long {
        val year = datePickerDialog.datePicker.year
        val day = datePickerDialog.datePicker.dayOfMonth
        val month = datePickerDialog.datePicker.month
        return LocalDate.of(year, month + 1, day).toEpochDay()
    }

    companion object {

        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

        private val listOfPermissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}