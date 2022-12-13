package com.example.homeworkout.presentation.screens.profile_screen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.FragmentProfileBinding
import com.example.homeworkout.domain.models.UserModel
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.example.homeworkout.utils.ToastUtil.Companion.makeToast
import javax.inject.Inject


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding is null")

    private val component by lazy {
        (requireActivity().application as AppWorkout).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        setOnButtonSignOutClickListener()
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
                    is CurrentUser -> {
                        handleUser(state.user)
                    }
                    is SignedOutSuccessfully -> {
                        findNavController().navigate(R.id.action_profileFragment_to_main_graph)
                    }
                    is Failure -> {
                        makeToast(requireContext(), state.message)
                    }
                }
            }
        }

    }

    private fun handleUser(user: UserModel) {

        Glide.with(requireContext())
            .load(Uri.parse(user.imageUrl))
            .into(binding.ivUserPhoto)

        binding.tvDisplayName.text = user.displayName
    }

    private fun setOnButtonSignOutClickListener() {

        binding.buttonSignOut.setOnClickListener {
            viewModel.signOut()
        }

    }


}