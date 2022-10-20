package com.example.homeworkout.domain.models

import android.graphics.Bitmap

data class UserInfoModel(
    val date: String,
    val weight: String,
    val photo: Bitmap
)