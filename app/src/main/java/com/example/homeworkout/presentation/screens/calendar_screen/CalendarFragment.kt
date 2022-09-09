package com.example.homeworkout.presentation.screens.calendar_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.databinding.FragmentCalendarBinding
import com.example.homeworkout.presentation.adapters.planned_workouts_adapter.PlannedWorkoutAdapter
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

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    @Inject
    lateinit var workoutAdapter: PlannedWorkoutAdapter

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[CalendarViewModel::class.java]
        setupRV()
        setCalendarDateChangedListener()
        setOnButtonAddClickListener()
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
        binding.calendar.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, day: Int) {
                viewModel.updateDate(formatDateFromCalendarView(day, month, year))
            }
        })
    }

    private fun formatDateFromCalendarView(day: Int, month: Int, year: Int): String {
        //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
        return "${day}-${month + 1}-$year"
    }

    private fun setupRV() {
        binding.rvScheduledWorkouts.adapter = workoutAdapter
        setOnSwapListener(binding.rvScheduledWorkouts)
        viewModel.plannedWorkoutList.observe(viewLifecycleOwner) {
            workoutAdapter.submitList(it)
        }
        workoutAdapter.onItemClicked = {
            findNavController().navigate(
                CalendarFragmentDirections.actionCalendarFragmentToWorkout(
                    it.workoutModel,
                    it
                )
            )
        }
        workoutAdapter.onItemLongClicked = {
            //TODO SHOW DIALOG SHOULD DELETE OR GO TO THE DETAIL SCREEN
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
                target: RecyclerView.ViewHolder
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
}