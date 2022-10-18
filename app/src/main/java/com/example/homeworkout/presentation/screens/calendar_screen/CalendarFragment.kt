package com.example.homeworkout.presentation.screens.calendar_screen

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.CustomActionDialogBinding
import com.example.homeworkout.databinding.FragmentCalendarBinding
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.formatDateFromCalendarView
import com.example.homeworkout.presentation.adapters.planned_workouts_adapter.PlannedWorkoutAdapter
import com.example.homeworkout.presentation.screens.plan_workout_screen.PlanWorkoutFragmentDirections
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import javax.inject.Inject


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding: FragmentCalendarBinding
        get() = _binding ?: throw RuntimeException("FragmentCalendarBinding is null")

    private val component by lazy {
        (requireActivity().application as AppWorkout).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateDate(viewModel.date)
    }

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    @Inject
    lateinit var workoutAdapter: PlannedWorkoutAdapter

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var dialog: Dialog

    private lateinit var plannedWorkoutModel: PlannedWorkoutModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[CalendarViewModel::class.java]
        setupRV()
        observeViewModelState()
        setupDialog()
        setCalendarDateChangedListener()
        setOnButtonAddClickListener()
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is PlannedWorkoutList -> {
                    workoutAdapter.submitList(it.list)
                }
            }
        }
    }

    private fun setOnButtonAddClickListener() {
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(
                CalendarFragmentDirections.actionCalendarFragmentToPlaneWorkout(
                    viewModel.date
                )
            )
        }
    }

    private fun setCalendarDateChangedListener() {
        binding.calendar.setOnDateChangeListener { p0, year, month, day ->
            viewModel.updateDate(
                formatDateFromCalendarView(day, month, year)
            )
        }
    }

    private fun setupRV() {
        binding.rvScheduledWorkouts.adapter = workoutAdapter
        setOnSwapListener(binding.rvScheduledWorkouts)
//        viewModel.plannedWorkoutList.observe(viewLifecycleOwner) {
//            workoutAdapter.submitList(it)
//        }
        workoutAdapter.onItemClicked = {
            findNavController().navigate(
                CalendarFragmentDirections.actionCalendarFragmentToWorkout(
                    it.workoutModel,
                    it
                )
            )
        }
        workoutAdapter.onItemLongClicked = {
            updatePlannedWorkoutModel(it)
            dialog.show()
        }
    }

    private fun setOnSwapListener(recyclerView: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = workoutAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deletePlannedWorkout(item)
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupDialog() {
        dialog = Dialog(requireContext())
        val dialogBinding = CustomActionDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.window?.attributes?.windowAnimations = R.style.window_animation
        setOnCustomDialogButtonsClickListeners(dialogBinding, dialog)

    }

    private fun setOnCustomDialogButtonsClickListeners(
        binding: CustomActionDialogBinding,
        dialog: Dialog,
    ) {
        with(binding) {

            tvCloseDialog.setOnClickListener {
                dialog.dismiss()
            }

            tvRightward.text = getString(R.string.delete)
            tvRightward.setOnClickListener {
                viewModel.deletePlannedWorkout(plannedWorkoutModel)
                dialog.dismiss()
            }

            tvLeftward.text = getString(R.string.go_to_detail)
            tvLeftward.setOnClickListener {
                dialog.dismiss()
                findNavController().navigate(
                    PlanWorkoutFragmentDirections.actionPlanWorkoutFragmentToWorkout(
                        plannedWorkoutModel.workoutModel, plannedWorkoutModel
                    )
                )
            }

        }
    }

    private fun updatePlannedWorkoutModel(plannedWorkoutModel: PlannedWorkoutModel) {
        this.plannedWorkoutModel = plannedWorkoutModel
    }
}