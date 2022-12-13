package com.example.homeworkout.utils

import android.app.Application
import android.content.ContentResolver
import android.net.Uri

class UriFromDrawableUtil {

    companion object {

        fun getUriFromDrawable(application: Application, resId: Int): String {
            return (Uri.Builder())
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(application.resources.getResourcePackageName(resId))
                .appendPath(application.resources.getResourceTypeName(resId))
                .appendPath(application.resources.getResourceEntryName(resId))
                .build()
                .toString()
        }

    }
}