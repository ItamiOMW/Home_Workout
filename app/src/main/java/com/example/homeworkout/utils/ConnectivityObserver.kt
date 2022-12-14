package com.example.homeworkout.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<ConnectivityStatus>

    enum class ConnectivityStatus {
        Available, Unavailable, Losing, Lost
    }
}