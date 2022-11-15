package com.example.homeworkout.domain.models

sealed class Response<out T> {

    //Loading State
    class Loading<T> : Response<T>()

    //Success state
    data class Success<T>(val data: T) : Response<T>()

    //Failed state
    data class Failed<T>(val message: String) : Response<T>()


    companion object {

        //GET LOADING STATE
        fun <T> loading() = Loading<T>()

        //GET SUCCESS STATE
        fun <T> success(data: T) = Success(data)

        //GET FAILED STATE
        fun <T> failed(message: String) = Failed<T>(message)

    }

}