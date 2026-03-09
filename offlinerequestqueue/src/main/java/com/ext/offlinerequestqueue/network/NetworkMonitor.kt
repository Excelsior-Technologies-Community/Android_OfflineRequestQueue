package com.ext.offlinerequestqueue.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.annotation.RequiresPermission
import com.ext.offlinerequestqueue.core.QueueManager

class NetworkMonitor(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun startMonitoring(context: Context) {

        connectivityManager.registerDefaultNetworkCallback(

            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {

                    QueueManager.getQueue().forEach {

                        QueueManager.enqueue(context, it)
                    }
                }
            }
        )
    }
}