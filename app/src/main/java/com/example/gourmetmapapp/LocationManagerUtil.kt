package com.example.gourmetmapapp

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

// 位置情報更新の最小間隔
private const val MIN_INTERVAL = 10 * 1000L // [ms]
// 位置情報更新の最小距離
private const val MIN_DISTANCE = 3.0F   // [meter]

class LocationManagerUtil(context: Context) {
    interface Listener {
        fun onLocationUpdated(location: Location)
    }

    var listener: Listener? = null
    var currentLocation: Location? = null
        private set
    private var isStarted = false

    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationListener: LocationListener = object : LocationListener {

        // GPS測位またはNW測位で位置情報が更新されると呼ばれる
        override fun onLocationChanged(location: Location) {
            // TODO: 更新時間や精度などからGPS測位結果とNW測位結果のどちらかより良い方を採用する処理

            currentLocation = location

            location?.apply { listener?.onLocationUpdated(this) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }

    @SuppressLint("MissingPermission")
    fun start() {
        if (!isStarted) {
            isStarted = true

            // GPS測位とNW測位を開始する
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_INTERVAL, MIN_DISTANCE, locationListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_INTERVAL, MIN_DISTANCE, locationListener)
        }
    }

    fun stop() {
        if (isStarted) {
            isStarted = false
            locationManager.removeUpdates(locationListener)
        }
    }
}