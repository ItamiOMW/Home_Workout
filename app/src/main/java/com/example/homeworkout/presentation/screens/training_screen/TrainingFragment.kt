package com.example.homeworkout.presentation.screens.training_screen

import android.app.Dialog
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import android.util.Log
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
        observeViewModel()
        viewModel.start(args.workoutModel, args.plannedWorkoutModel)
        setupOnButtonsClickListener()
        setupDialog()
    }

    private fun observeViewModel() {

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is Exercise -> {
                    binding.ivExerciseGif.setImageDrawable(createGifSource(it.exerciseModel.exerciseGif))
                    binding.tvExerciseTitle.text = it.exerciseModel.title
                    binding.tvReps.text = it.exerciseModel.reps.toString()
                    binding.tvExerciseDetail.text = it.exerciseModel.description
                }
                is TimerTime -> {
                    binding.tvTimer.text = it.time
                }
                is CurrentExercisePositionAndAmountOfExercises -> {
                    binding.tvCountOfExercises.text = it.position
                }
                is IsWorkoutCompleted -> {
                    dialog.show()
                }
            }
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

            viewModel.state.observe(viewLifecycleOwner) {
                if (it is TimerTime) {
                    binding.tvTime.text = it.time
                }
            }

            tvWorkoutName.text = args.workoutModel.title
        }
    }

    private fun createGifSource(drawableId: Int): AnimatedImageDrawable {
        val source = ImageDecoder.createSource(resources, drawableId)
        val drawable = ImageDecoder.decodeDrawable(source)
        val animatedDrawable = (drawable as AnimatedImageDrawable)
        animatedDrawable.start()
        return animatedDrawable
    }


}