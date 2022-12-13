package com.example.homeworkout.presentation.screens.training_screen

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.FragmentTrainingBinding
import com.example.homeworkout.databinding.WorkoutCompletedDialogBinding
import com.example.homeworkout.presentation.adapters.planned_workouts_adapter.PlannedWorkoutAdapter
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.example.homeworkout.utils.ToastUtil.Companion.makeToast
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
        collectUIState()
        viewModel.start(args.workoutModel, args.plannedWorkoutModel)
        setupOnButtonsClickListener()
        setupDialog()
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
                    is Exercise -> {
                        handleExercise(state)
                    }
                    is TimerTime -> {
                        binding.tvTimer.text = state.time
                    }
                    is CurrentExercisePositionAndAmountOfExercises -> {
                        binding.tvCountOfExercises.text = state.position
                    }
                    is IsWorkoutCompleted -> {
                        dialog.show()
                    }
                    is IsPlannedWorkoutCompleted -> {
                        makeToast(requireContext(), getString(R.string.comleted_planned_workout))
                    }
                    is Failure -> {
                        makeToast(requireContext(), state.message)
                    }
                }
            }
        }

    }

    private fun handleExercise(state: Exercise) {
        setupAnimation(state.exerciseModel.exerciseGif, binding.ivExerciseGif)
        binding.tvExerciseTitle.text = state.exerciseModel.title
        binding.tvReps.text = state.exerciseModel.reps.toString()
        binding.tvExerciseDetail.text = state.exerciseModel.description
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
        val workoutCompletedDialogBinding = WorkoutCompletedDialogBinding.inflate(layoutInflater)
        dialog.setContentView(workoutCompletedDialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.window_animation
        setupDialogButtons(workoutCompletedDialogBinding, dialog)

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

            tvWorkoutName.text = args.workoutModel.title

            lifecycleScope.launchWhenStarted {
                viewModel.state.collect { state ->
                    if (state is TimerTime) {
                        binding.tvTime.text = state.time
                    }
                }
            }
        }
    }

    private fun setupAnimation(imageUri: String, iv: ImageView) {
        Glide.with(requireContext()).asGif().load(imageUri).into(iv)
    }

}