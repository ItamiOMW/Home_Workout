package com.example.homeworkout.presentation.screens.plane_workout_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.FragmentCalendarBinding
import com.example.homeworkout.databinding.FragmentPlaneWorkoutBinding
import com.example.homeworkout.presentation.adapters.planned_workouts_adapter.PlannedWorkoutAdapter
import com.example.homeworkout.presentation.adapters.workouts_adapter.WorkoutAdapter
import com.example.homeworkout.presentation.screens.calendar_screen.CalendarViewModel
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import javax.inject.Inject


class PlaneWorkoutFragment : Fragment() {

    private var _binding: FragmentPlaneWorkoutBinding? = null
    private val binding: FragmentPlaneWorkoutBinding
        get() = _binding ?: throw RuntimeException("FragmentPlaneWorkoutBinding is null")

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
    lateinit var workoutAdapter: WorkoutAdapter

    private lateinit var viewModel: PlaneWorkoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaneWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val args by navArgs<PlaneWorkoutFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[PlaneWorkoutViewModel::class.java]
        setupRV()
    }

    private fun setupRV() {
        binding.rvTrainings.adapter = workoutAdapter
        viewModel.workoutList.observe(viewLifecycleOwner) {
            workoutAdapter.submitList(it)
        }
        workoutAdapter.onItemClicked = {
            //TODO SHOW DIALOG ADD OR GO TO THE DETAIL
        }
        workoutAdapter.onItemLongClicked = {
            //TODO ADD TO SCHEDULE
        }
    }
}

