package com.example.homeworkout.di.modules

import android.app.Application
import android.net.ConnectivityManager
import com.example.homeworkout.R
import com.example.homeworkout.utils.connectivity.ConnectivityObserver
import com.example.homeworkout.utils.connectivity.NetworkConnectivityObserver
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {

    @Binds
    fun bindNetworkConnectivityObserver(
        connectivityObserver: NetworkConnectivityObserver
    ): ConnectivityObserver


    companion object {

        @Provides
        fun provideGoogleSignInClient(
            application: Application,
        ): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(application, gso)
        }

        @Provides
        fun provideConnectivityManager(
            application: Application,
        ): ConnectivityManager {
            return application.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

    }
}