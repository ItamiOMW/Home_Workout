package com.example.homeworkout.domain.models

import com.example.homeworkout.domain.models.Status.*

data class Response<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Response<T> {
            return Response(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Response<T> {
            return Response(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Response<T> {
            return Response(LOADING, data, null)
        }

    }

}