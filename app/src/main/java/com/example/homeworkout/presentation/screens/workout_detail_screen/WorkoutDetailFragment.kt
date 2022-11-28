package com.example.homeworkout.presentation.screens.workout_detail_screen

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.databinding.FragmentWorkoutDetailBinding
import com.example.homeworkout.presentation.adapters.exercises_adapter.ExerciseAdapter
import com.example.homeworkout.fromByteArrayToBitmap
import javax.inject.Inject


class WorkoutDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding: FragmentWorkoutDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentWorkoutDetailBinding is null")

    private val component by lazy {
        (requireActivity().application as AppWorkout).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var exerciseAdapter: ExerciseAdapter

    private val args by navArgs<WorkoutDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()
        setupWorkoutCardView()
        setOnButtonStartClickListener()
    }

    private fun setupRV() {
        binding.rvExercises.adapter = exerciseAdapter
        exerciseAdapter.submitList(args.workoutModel.listExercises)
        binding.llWorkout
    }

    private fun setupWorkoutCardView() {
        binding.llWorkout.background = BitmapDrawable(
            Resources.getSystem(),
            fromByteArrayToBitmap(args.workoutModel.image)
        )
        binding.tvWorkoutDuration.text = args.workoutModel.duration.toString()
        binding.tvWorkoutName.text = args.workoutModel.title
    }

    private fun setOnButtonStartClickListener() {
        binding.buttonStartWorkout.setOnClickListener {
            findNavController().navigate(WorkoutDetailFragmentDirections
                .actionWorkoutDetailFragmentToTrainingFragment(
                    args.workoutModel,
                    args.plannedWorkoutModel
                ))
        }
    }

}