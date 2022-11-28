package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_info")
data class UserInfoModel(
    @PrimaryKey
    val date: String = "",
    val weight: String = "",
    val photo: ByteArray,
    val firebaseId: String = ""
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInfoModel

        if (date != other.date) return false
        if (weight != other.weight) return false
        if (!photo.contentEquals(other.photo)) return false
        if (firebaseId != other.firebaseId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + firebaseId.hashCode()
        return result
    }
}