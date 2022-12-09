package com.example.homeworkout.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "user_info")
data class UserInfoModel(
    @PrimaryKey
    val date: Long = 0,
    val weight: String = "",
    val photo: String = "",
    var firebaseId: String = ""
): Parcelable
