package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_info")
data class UserInfoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = 0,
    val weight: String = "",
    val photo: String = "",
    var firebaseId: String = ""
): Parcelable
