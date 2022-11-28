package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homeworkout.data.database.room.converters.ExerciseConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "workout_models")
data class WorkoutModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "image")
    val image: ByteArray,
    @ColumnInfo(name = "duration")
    val duration: Float = 0.0f,
    @TypeConverters(ExerciseConverter::class)
    @ColumnInfo(name = "listExercises")
    val listExercises: List<ExerciseModel> = mutableListOf(),
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkoutModel

        if (id != other.id) return false
        if (title != other.title) return false
        if (!image.contentEquals(other.image)) return false
        if (duration != other.duration) return false
        if (listExercises != other.listExercises) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + listExercises.hashCode()
        return result
    }

}