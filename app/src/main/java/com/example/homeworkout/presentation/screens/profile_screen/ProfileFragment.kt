package com.example.homeworkout.presentation.screens.profile_screen

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.FragmentProfileBinding
import com.example.homeworkout.databinding.FragmentProgressBinding
import com.example.homeworkout.domain.models.UserModel
import com.example.homeworkout.presentation.screens.progress_screen.ProgressViewModel
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
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
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is SignedOutSuccessfully -> {
                    //GO TO LOGIN SCREEN
                    if (it.boolean) {
                        findNavController().navigate(R.id.action_profileFragment_to_main_graph)
                    }
                }
                is CurrentUser -> {
                    handleUser(it.user)
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