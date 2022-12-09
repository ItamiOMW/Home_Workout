package com.example.homeworkout

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.time.LocalDate

const val ROOM_DATABASE = "room"

const val FIRESTORE_DATABASE = "firestore"

var DATABASE_TO_USE = ROOM_DATABASE

const val ZERO_WORKOUTS_COMPLETED = 0

const val UNKNOWN_ID = 0

const val WORKOUT_NOT_COMPLETED = false

const val WORKOUT_COMPLETED = true

const val MILLIS_IN_SECOND = 999L

const val SECONDS_IN_HOUR = 3600

const val SECONDS_IN_MINUTE = 60

const val BAR_CHART_ANIMATION_DURATION = 1000

const val USERS_PATH = "users"

const val USER_INF0_PATH = "userinfo"

const val PLANNED_WORKOUT_PATH = "planned_workouts"

const val WORKOUT_PATH = "workouts"

const val COUNT_OF_COMPLETED_WORKOUTS_PATH = "countOfCompletedWorkouts"

const val COUNT_OF_COMPLETED_WORKOUTS_DOCUMENT ="countOfCompletedWorkoutsDocument"

const val FIREBASE_ID = "firebaseId"

const val DATE_MILLIS = "date"

const val IMAGES = "results_images/"

const val DATE_FIELD = "date"

const val FIREBASE_ID_FIELD = "firebaseId"

const val PHOTO_FIELD = "photo"

const val WEIGHT_FIELD = "weight"

const val COUNT_FIELD = "count"

const val IS_COMPLETED_FIELD = "completed"

fun getRealPathFromURI(contentURI: Uri, application: Application): String? {
    val cursor: Cursor? = application.contentResolver.query(contentURI, null, null, null, null)
    return if (cursor == null) {
        contentURI.path
    } else {
        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        cursor.getString(idx)
    }
}

fun getUriFromDrawable(application: Application, resId: Int): String {
    return (Uri.Builder())
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(application.resources.getResourcePackageName(resId))
        .appendPath(application.resources.getResourceTypeName(resId))
        .appendPath(application.resources.getResourceEntryName(resId))
        .build()
        .toString()
}

fun timeToLong(time: LocalDate): Long {
    return time.toEpochDay()
}

fun longToTime(long: Long): LocalDate {
    return LocalDate.ofEpochDay(long)
}

fun formatDateFromDatePicker(day: Int, month: Int, year: Int): String {
    //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
    return "${day}-${month + 1}-$year"
}

fun formatDateFromCalendarView(day: Int, month: Int, year: Int): String {
    //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
    return "${day}-${month + 1}-$year"
}

fun formatDate(day: Int, month: Int, year: Int): String {
    return "${day}-${month}-$year"
}



