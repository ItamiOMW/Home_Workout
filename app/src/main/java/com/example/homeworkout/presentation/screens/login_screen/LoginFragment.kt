package com.example.homeworkout.presentation.screens.login_screen

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.homeworkout.*
import com.example.homeworkout.databinding.FragmentLoginBinding
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.example.homeworkout.utils.DATABASE_TO_USE
import com.example.homeworkout.utils.FIRESTORE_DATABASE
import com.example.homeworkout.utils.ROOM_DATABASE
import com.example.homeworkout.utils.ToastUtil.Companion.makeToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentLoginBinding is null")


    private val component by lazy {
        (requireActivity().application as AppWorkout).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUIState()
        setupOnButtonLoginClickListener()
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
                    is AuthenticationSucceed -> {
                        goToTabsScreen()
                    }
                    is Failure -> {
                        makeToast(requireContext(), state.message)
                    }
                }
            }
        }

    }

    private fun setupOnButtonLoginClickListener() {

        binding.buttonLoginFirebase.setOnClickListener {
            DATABASE_TO_USE = FIRESTORE_DATABASE
            signInGoogle()
        }

        binding.buttonLoginRoom.setOnClickListener {
            DATABASE_TO_USE = ROOM_DATABASE
            goToTabsScreen()
        }

    }

    private fun goToTabsScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_tabsFragment)
    }

    private fun signInGoogle() {

        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            } else {
                makeToast(requireContext(), getString(R.string.failed_with_google))
            }
        }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signIn(credential)
            }
        } else {
            makeToast(requireContext(), requireContext().getString(R.string.failed_with_google))
        }
    }

}