package com.example.homeworkout.data.database.room.room_db_models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoDbModel(
    @PrimaryKey
    val date: String,
    val weight: String,
    val photo: Bitmap
)