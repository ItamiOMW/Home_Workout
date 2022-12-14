package com.example.homeworkout.presentation.screens.parent_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.homeworkout.AppWorkout
import com.example.homeworkout.utils.DATABASE_TO_USE
import com.example.homeworkout.utils.FIRESTORE_DATABASE
import com.example.homeworkout.R
import com.example.homeworkout.databinding.ActivityMainBinding
import com.example.homeworkout.presentation.viewmodel_factory.WorkoutViewModelFactory
import com.example.homeworkout.utils.connectivity.ConnectivityObserver
import com.example.homeworkout.utils.ToastUtil.Companion.makeToast
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkConnectivityObserver: ConnectivityObserver

    @Inject
    lateinit var viewModelFactory: WorkoutViewModelFactory

    private lateinit var viewModel: MainViewModel

    private val component by lazy {
        (application as AppWorkout).component
    }

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //SHOW SPLASH SCREEN BEFORE LOAD THE MAIN ACTIVITY
        installSplashScreen()
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = getRootNavController()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        collectNetworkStateIfUsingRemoteDatabase()
        collectUIState()
        checkSignedIn()
    }

    private fun collectNetworkStateIfUsingRemoteDatabase() {
        lifecycleScope.launchWhenStarted {
            networkConnectivityObserver.observe().collect { state ->
                when (state) {
                    ConnectivityObserver.ConnectivityStatus.Available -> {
                        binding.tvInternetConnection.text =
                            getString(R.string.network_connection_available)
                        binding.tvInternetConnection.visibility = View.GONE
                    }
                    ConnectivityObserver.ConnectivityStatus.Unavailable -> {
                        binding.tvInternetConnection.text =
                            getString(R.string.network_connection_unavailable)
                        binding.tvInternetConnection.visibility = View.VISIBLE
                    }
                    ConnectivityObserver.ConnectivityStatus.Lost -> {
                        binding.tvInternetConnection.text =
                            getString(R.string.network_connection_lost)
                        binding.tvInternetConnection.visibility = View.VISIBLE
                    }
                    ConnectivityObserver.ConnectivityStatus.Losing -> {
                        binding.tvInternetConnection.text =
                            getString(R.string.network_connection_losing)
                        binding.tvInternetConnection.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun collectUIState() {

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is IsSignedIn -> {
                        DATABASE_TO_USE = FIRESTORE_DATABASE
                        setStartDestination(state.isSignedIn, navController)
                    }
                    is Failure -> {
                        makeToast(applicationContext, state.message)
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

}