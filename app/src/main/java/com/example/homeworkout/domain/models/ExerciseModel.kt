package com.example.homeworkout.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseModel(
    val title: String,
    val reps: Int,
    val description: String,
    val exerciseGif: ByteArray
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseModel

        if (title != other.title) return false
        if (reps != other.reps) return false
        if (description != other.description) return false
        if (!exerciseGif.contentEquals(other.exerciseGif)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + reps
        result = 31 * result + description.hashCode()
        result = 31 * result + exerciseGif.contentHashCode()
        return result
    }
}
