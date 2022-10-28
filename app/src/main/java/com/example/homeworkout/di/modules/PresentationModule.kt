package com.example.homeworkout.di.modules

import android.app.Application
import com.example.homeworkout.R
import com.example.homeworkout.SIGN_IN_REQUEST
import com.example.homeworkout.SIGN_UP_REQUEST
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Named

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