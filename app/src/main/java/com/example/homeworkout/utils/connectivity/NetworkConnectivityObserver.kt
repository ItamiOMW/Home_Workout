package com.example.homeworkout.utils.connectivity

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkConnectivityObserver @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) : ConnectivityObserver {

    override fun observe(): Flow<ConnectivityObserver.ConnectivityStatus> = callbackFlow {

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectivityObserver.ConnectivityStatus.Available) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch { send(ConnectivityObserver.ConnectivityStatus.Losing) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectivityObserver.ConnectivityStatus.Lost) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch { send(ConnectivityObserver.ConnectivityStatus.Unavailable) }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }

    }.distinctUntilChanged()

}