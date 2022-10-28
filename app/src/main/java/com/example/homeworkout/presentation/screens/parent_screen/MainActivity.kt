package com.example.homeworkout.presentation.screens.parent_screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    private lateinit var viewModel: MainViewModel

    private val component by lazy {
        (application as AppWorkout).component
    }

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        //SHOW SPLASH SCREEN BEFORE LOAD THE MAIN ACTIVITY
        installSplashScreen()
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = getRootNavController()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        observeViewModel()
        checkSignedIn()
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) {
            when (it) {
                is IsSignedIn -> {
                    setStartDestination(it.isSignedIn, navController)
                }
            }
        }
    }

    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        return navHost.navController
    }

    private fun setStartDestination(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavGraphId())
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getSignInDestination()
            }
        )
        navController.graph = graph
    }

    private fun checkSignedIn() {
        viewModel.checkSignedIn()
    }

    private fun getMainNavGraphId() = R.navigation.main_graph

    private fun getTabsDestination() = R.id.tabsFragment

    private fun getSignInDestination() = R.id.loginFragment
}