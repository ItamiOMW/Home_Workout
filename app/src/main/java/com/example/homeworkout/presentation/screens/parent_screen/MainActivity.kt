package com.example.homeworkout.presentation.screens.parent_screen

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.R
import com.example.homeworkout.presentation.screens.tabs_screen.TabsFragment
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.google.android.material.navigation.NavigationView
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
        collectUIState()
        checkSignedIn()
    }



    private fun collectUIState() {

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is IsSignedIn -> {
                        setStartDestination(state.isSignedIn, navController)
                    }
                    is Failure -> {
                        Toast.makeText(applicationContext, state.message, Toast.LENGTH_SHORT).show()
                    }
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}