package com.example.homeworkout.di.modules

import android.app.Application
import com.example.homeworkout.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {


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
    }
}