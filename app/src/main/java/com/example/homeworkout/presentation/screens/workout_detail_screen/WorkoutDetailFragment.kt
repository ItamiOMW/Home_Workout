package com.example.homeworkout.presentation.screens.workout_detail_screen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.databinding.FragmentWorkoutDetailBinding
import com.example.homeworkout.presentation.adapters.exercises_adapter.ExerciseAdapter
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
        //LOADING DRAWABLE INTO THE BACKGROUND OF THE CARD VIEW
        Glide.with(requireContext()).asDrawable().load(args.workoutModel.image)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    binding.llWorkout.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        binding.tvWorkoutDuration.text = args.workoutModel.duration.toString()
        binding.tvWorkoutName.text = args.workoutModel.title
    }

    private fun setOnButtonStartClickListener() {
        binding.buttonStartWorkout.setOnClickListener {
            findNavController().navigate(WorkoutDetailFragmentDirections
                .actionWorkoutDetailFragmentToTrainingFragment(
                    args.workoutModel,
                    args.plannedWorkoutModel
                )
            )
        }
    }

}