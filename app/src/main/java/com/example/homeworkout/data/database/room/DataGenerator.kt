package com.example.homeworkout.data.database.room

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.homeworkout.R
import com.example.homeworkout.UNKNOWN_ID
import com.example.homeworkout.domain.models.ExerciseModel
import com.example.homeworkout.domain.models.WorkoutModel
import java.io.ByteArrayOutputStream

class DataGenerator {


    fun getWorkouts(application: Application) = listOf(
        WorkoutModel(
            id = UNKNOWN_ID,
            title = "TEST WORKOUT",
            image = compressImage(intResourcesToByteArray(R.drawable.fullbody, application)),
            duration = 0f,
            listExercises = listOf(
                ExerciseModel(
                    title = "PUSH UP",
                    reps = 10,
                    description = "PERFORM THE EXERCISE KEEPING THE CORE MUSCLES IN TENSION",
                    exerciseGif = animatedToByteArray(R.drawable.pushup, application)
                )
            )
        )
    )

    private fun intResourcesToByteArray(intResources: Int, application: Application): ByteArray {
        val drawable = ResourcesCompat.getDrawable(application.resources, intResources, null)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun animatedToByteArray(intResources: Int, application: Application): ByteArray {
        return Glide.with(application)
            .`as`(ByteArray::class.java)
            .load(intResources)
            .submit()
            .get()
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

}