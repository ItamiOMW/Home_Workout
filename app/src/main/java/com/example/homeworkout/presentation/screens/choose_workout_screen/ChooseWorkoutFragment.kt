package com.example.homeworkout.presentation.screens.choose_workout_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.databinding.FragmentChooseWorkoutBinding
import com.example.homeworkout.presentation.adapters.workouts_adapter.WorkoutAdapter
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import javax.inject.Inject


open class ChooseWorkoutFragment : Fragment() {

    private var _binding: FragmentChooseWorkoutBinding? = null
    private val binding: FragmentChooseWorkoutBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseWorkoutBinding is null")

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

    private lateinit var viewModel: ChooseWorkoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChooseWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ChooseWorkoutViewModel::class.java]
        setupRV()
        collectUIState()
    }

    private fun collectUIState() {

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                if (state is Loading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                } else {
                    binding.progressBarLoading.visibility = View.GONE
                }
                when (state) {
                    is WorkoutList -> {
                        workoutAdapter.submitList(state.list)
                    }
                    is Failure -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun setupRV() {
        binding.rvTrainings.adapter = workoutAdapter

        workoutAdapter.onItemClicked = {
            findNavController().navigate(
                ChooseWorkoutFragmentDirections.actionChooseWorkoutFragmentToWorkout(
                    it,
                    null
                )
            )
        }
    }


}