package com.example.homeworkout.presentation.screens.login_screen

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.databinding.FragmentLoginBinding
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
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
        observeViewModel()
        setupOnButtonLoginClickListener()
    }

    private fun observeViewModel() {

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is AuthenticationSucceed -> {
                    findNavController().navigate(R.id.action_loginFragment_to_tabsFragment)
                }
            }
        }

    }

    private fun setupOnButtonLoginClickListener() {

        binding.buttonLogin.setOnClickListener {
            signInGoogle()
        }

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
                Log.d("TEST_BUG", "true")
            } else {
                Log.d("TEST_BUG", "false")
            }
        }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                Log.d("TEST_BUG", credential.toString())
                viewModel.signIn(credential)
            }
        } else {
            Toast.makeText(context, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        const val REQ_CODE_GOOGLE = 541
    }

}