package com.example.homeworkout

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

const val UNKNOWN_ID = 0

const val WORKOUT_NOT_COMPLETED = false

const val MILLIS_IN_SECOND = 999L

const val SECONDS_IN_HOUR = 3600

const val SECONDS_IN_MINUTE = 60

const val BAR_CHART_ANIMATION_DURATION = 1000

const val SIGN_IN_REQUEST = "signInRequest"

const val SIGN_UP_REQUEST = "signUpRequest"

const val USER_INF0_PATH = "userInfoModel"

const val PLANNED_WORKOUT_PATH = "plannedWorkoutModel"

const val USERS_PATH = "users"

const val WORKOUT_PATH = "workoutModel"

const val FIREBASE_ID = "firebaseId"

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

fun fromBitmapToByteArray(value: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    value.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
    outputStream.close()
    return outputStream.toByteArray()
}

fun fromByteArrayToBitmap(value: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(value, 0, value.size)
}

fun compressImage(image: ByteArray): ByteArray {
    var compressImage = image
    while (compressImage.size > 500000) {
        val bitmap = BitmapFactory.decodeByteArray(compressImage, 0, compressImage.size)
        val resized = Bitmap.createScaledBitmap(
            bitmap, (bitmap.width * 0.8).toInt(),
            (bitmap.height * 0.8).toInt(),
            true
        )
        val outputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        compressImage = outputStream.toByteArray()
    }
    return compressImage
}


