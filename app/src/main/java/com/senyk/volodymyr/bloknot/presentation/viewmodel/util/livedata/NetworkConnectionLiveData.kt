package com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class NetworkConnectionLiveData(private val context: Context) : LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            preLollipopConnectionUpdate()
        }
    }

    private val connectivityManagerCallback: ConnectivityManager.NetworkCallback =
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                postValue(true)
            }

            override fun onLost(network: Network?) {
                postValue(false)
            }
        }

    override fun onActive() {
        super.onActive()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> nougatNetworkAvailableRequest()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
            else -> {
                preLollipopConnectionUpdate()
                preLollipopNetworkAvailableRequest()
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        } else {
            context.unregisterReceiver(networkReceiver)
        }
    }

    private fun preLollipopConnectionUpdate() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }

    private fun preLollipopNetworkAvailableRequest() {
        context.registerReceiver(networkReceiver, IntentFilter(ACTION_CONNECTIVITY_CHANGE))
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun nougatNetworkAvailableRequest() {
        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
        connectivityManager.registerNetworkCallback(builder.build(), connectivityManagerCallback)
    }

    companion object {
        private const val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}
