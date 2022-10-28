package com.example.homeworkout.data.database.room.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapConverter {

    @TypeConverter
    fun fromBitmap(value: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(value: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(value, 0, value.size)
    }

}