package com.example.homeworkout.presentation.screens.progress_screen

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.util.lruCache
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.BuildConfig
import com.example.homeworkout.R
import com.example.homeworkout.databinding.AddWeightDialogBinding
import com.example.homeworkout.databinding.EditUserDialogBinding
import com.example.homeworkout.databinding.FragmentProgressBinding
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.presentation.adapters.user_info_adapter.UserInfoAdapter
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.io.File
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var dialogAddUser: Dialog

    private lateinit var dialogEditUser: Dialog

    private lateinit var datePickerDialog: DatePickerDialog

    private var latestTmpUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProgressViewModel::class.java]
        setupUserInfoAdapter()
        observeViewModel()
        setupDatePicker()
        setupDialogAddUser()
        setupOnButtonAddUserInfoClickListener()
    }

    private fun observeViewModel() {

        viewModel.listUserInfo.observe(viewLifecycleOwner) {
            setupBarChart(it)
        }

        viewModel.completedWorkouts.observe(viewLifecycleOwner) {
            binding.tvCompletedWorkouts.text = String.format(
                getString(R.string.count_of_workout_completed, it.toString()))
        }

        viewModel.dateFailure.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "DATE WASN'T SELECTED", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.weightFailure.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "WEIGHT WASN'T ENTERED", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupUserInfoAdapter() {
        binding.rvUserInfo.adapter = userInfoAdapter

        viewModel.listUserInfo.observe(viewLifecycleOwner) {
            userInfoAdapter.submitList(it)
        }

        userInfoAdapter.onItemClicked = {
            setupDialogEditUser(it)
            dialogEditUser.show()
        }

        userInfoAdapter.onItemLongClicked = {
            setupDialogEditUser(it)
            dialogEditUser.show()
        }

    }

    private fun setupBarChart(listUserInfo: List<UserInfoModel>) {
        val array = listUserInfo.mapIndexed { index, userInfoModel ->
            BarEntry(index.toFloat() * 10, userInfoModel.weight.toFloat())
        }
        val barDataSer = BarDataSet(array, getString(R.string.weight))
        barDataSer.setDrawValues(false)
        binding.barChart.data = BarData(barDataSer)
        binding.barChart.animateY(1000)
        binding.barChart.description.text = getString(R.string.your_weight)
        binding.barChart.description.textColor = Color.BLUE
    }

    private fun setupOnButtonAddUserInfoClickListener() {

        binding.buttonAddUserInfo.setOnClickListener {
            dialogAddUser.show()
        }

    }

    private fun setupDialogAddUser() {
        dialogAddUser = Dialog(requireContext())
        val dialogBinding = AddWeightDialogBinding.inflate(layoutInflater)
        dialogAddUser.setContentView(dialogBinding.root)
        dialogAddUser.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialogAddUser.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogAddUser.setCancelable(true)
        dialogAddUser.window?.attributes?.windowAnimations = R.style.window_animation
        setOnDialogAddUserButtonsClickListeners(dialogBinding, dialogAddUser)
    }


    private fun setOnDialogAddUserButtonsClickListeners(
        binding: AddWeightDialogBinding,
        dialog: Dialog,
    ) {
        with(binding) {

            tvAdd.setOnClickListener {
                viewModel.addUserInfo(
                    tvDate.text.toString(),
                    etWeight.text.toString(),
                    ivUserPhoto.drawable.toBitmap(200, 200, null)
                )
                dialog.hide()
            }

            ivUserPhoto.setOnClickListener {
                takeImage()
            }

            tvDate.setOnClickListener {
                datePickerDialog.show()
            }

            viewModel.date.observe(viewLifecycleOwner) {
                tvDate.text = it
            }

            viewModel.imageUri.observe(viewLifecycleOwner) {
                ivUserPhoto.setImageURI(it)
            }

        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    viewModel.updateImageUri(uri)
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.updateImageUri(uri) }
        }


    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun setupDialogEditUser(userInfo: UserInfoModel) {
        dialogEditUser = Dialog(requireContext())
        val dialogBinding = EditUserDialogBinding.inflate(layoutInflater)
        dialogEditUser.setContentView(dialogBinding.root)
        dialogEditUser.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialogEditUser.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogEditUser.setCancelable(true)
        dialogEditUser.window?.attributes?.windowAnimations = R.style.window_animation
        setOnDialogEditUserButtonsClickListeners(dialogBinding, dialogEditUser, userInfo)
    }

    private fun setOnDialogEditUserButtonsClickListeners(
        binding: EditUserDialogBinding,
        dialog: Dialog,
        userInfo: UserInfoModel,
    ) {
        with(binding) {

            tvEdit.setOnClickListener {
                viewModel.updateUserInfo(
                    tvDate.text.toString(),
                    etWeight.text.toString(),
                    ivUserPhoto.drawable.toBitmap(200, 200, null)
                )
                dialog.hide()
            }

            tvDelete.setOnClickListener {
                viewModel.deleteUserInfo(userInfo)
                dialog.hide()
            }

            ivUserPhoto.setImageBitmap(userInfo.photo)

            ivUserPhoto.setOnClickListener {
                takeImage()
            }

            etWeight.setText(userInfo.weight)

            tvDate.text = userInfo.date

            viewModel.imageUri.observe(viewLifecycleOwner) {
                ivUserPhoto.setImageURI(it)
            }

        }
    }

    private fun setupDatePicker() {

        datePickerDialog = DatePickerDialog(requireContext())

        datePickerDialog.datePicker.setOnDateChangedListener { datePicker, year, month, day ->
            val date = formatDateFromDatePicker(day, month, year)
            viewModel.updateDate(date)
        }

    }

    private fun formatDateFromDatePicker(day: Int, month: Int, year: Int): String {
        //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
        return "${day}-${month + 1}-$year"
    }

    private fun formatWeightToKG(weight: String) =
        String.format(getString(R.string.weight_format), weight)

}