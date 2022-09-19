package com.example.homeworkout.presentation.screens.training_screen

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.FragmentTrainingBinding
import com.example.homeworkout.databinding.WorkoutCompletedDialogBinding
import com.example.homeworkout.presentation.adapters.planned_workouts_adapter.PlannedWorkoutAdapter
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import javax.inject.Inject


class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null
    private val binding: FragmentTrainingBinding
        get() = _binding ?: throw RuntimeException("FragmentTrainingBinding is null")

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

    private lateinit var viewModel: TrainingViewModel

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val args by navArgs<TrainingFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[TrainingViewModel::class.java]
        viewModel.start(args.workoutModel, args.plannedWorkoutModel)
        observeCurrentExercisePositionAndAmountOfExercises()
        observeExercise()
        observeTimer()
        observeIfWorkoutCompleted()
        setupOnButtonsClickListener()
        setupDialog()
    }

    private fun observeExercise() {
        viewModel.exercise.observe(viewLifecycleOwner) {
//            binding.ivExerciseGif.setImageBitmap() TODO FIND OR CREATE ANIMATIONS
            binding.tvExerciseTitle.text = it.title
            binding.tvReps.text = it.reps.toString()
//            binding.tvExerciseDetail.text TODO ADD DETAIL INFO TO EXERCISE
        }
    }

    private fun observeCurrentExercisePositionAndAmountOfExercises() {
        viewModel.currentExercisePositionAndAmountOfExercises.observe(viewLifecycleOwner) {
            binding.tvCountOfExercises.text = it
        }
    }

    private fun observeTimer() {
        viewModel.timerTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }

    private fun observeIfWorkoutCompleted() {
        viewModel.isWorkoutCompleted.observe(viewLifecycleOwner) {
            dialog.show()
        }
    }

    private fun setupOnButtonsClickListener() {

        binding.buttonCompleted.setOnClickListener {
            viewModel.goToNextExercise()
        }

        binding.buttonPrevious.setOnClickListener {
            viewModel.goToPreviousExercise()
        }

        binding.buttonSkip.setOnClickListener {
            viewModel.goToNextExercise()
        }

    }

    private fun setupDialog() {
        dialog = Dialog(requireContext())
        val dialogBinding = WorkoutCompletedDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.window_animation
        setupDialogButtons(dialogBinding, dialog)

    }

    private fun setupDialogButtons(
        binding: WorkoutCompletedDialogBinding,
        dialog: Dialog,
    ) {
        with(binding) {

            tvCloseDialog.setOnClickListener {
                dialog.dismiss()
                findNavController().popBackStack()
            }

            viewModel.timerTime.observe(viewLifecycleOwner) {
                binding.tvTime.text = it
            }

            tvWorkoutName.text = args.workoutModel.title

        }
    }


}