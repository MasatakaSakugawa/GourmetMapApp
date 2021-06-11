package com.example.gourmetmapapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private val REQUEST_CODE_PERMISSIONS = 1
    //必要な権限の種類が格納される
    private var latitude: Double? = null
    //ユーザーの緯度
    private var longitude: Double? = null
    //ユーザーの経度
    private var user_location: LatLng? = null

    private lateinit var mMap: GoogleMap
    private val locationManager: LocationManagerUtil by lazy {
        val manager = LocationManagerUtil(this)
        manager.listener = locationListener
        manager
    }

    private val locationListener: LocationManagerUtil.Listener = object : LocationManagerUtil.Listener {
        // 位置情報に更新があると呼ばれる
        override fun onLocationUpdated(location: Location) {
            Log.d("Mainactivity","""
                provider: ${location.provider} 
                lat: ${location.latitude} 
                lon: ${location.longitude} 
                acc: ${location.accuracy}
                """)

            latitude = location.latitude
            longitude = location.longitude
            user_location = LatLng(latitude!!,longitude!!)
        }
    }

    private var isPermissionDenied = false
        set(value) {
            field = value
            if (!value) {
                // ユーザに権限が与えられているならば位置測位開始
                locationManager.start()
            }
            else {
                locationManager.stop()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        isPermissionDenied = !checkSelfPermissions()
    }

    override fun onPause() {
        super.onPause()
        // 位置測位の終了
        locationManager.stop()
    }

    private fun checkSelfPermissions(): Boolean {
        val deniedPermissions = arrayListOf<String>()
        // 位置情報の権限が与えられているか
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            deniedPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (deniedPermissions.size > 0) {
            // 権限が無い場合はユーザに認可してもらえるようにリクエスト（ダイアログが表示される）
            ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), REQUEST_CODE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty()) {
                val deniedPermissions = arrayListOf<String>()
                grantResults.forEachIndexed { index, result ->
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permissions[index])
                    }
                }

                isPermissionDenied = deniedPermissions.isNotEmpty()
                if (isPermissionDenied) {
                    // TODO: ユーザに権限が与えられなかったときの処理
                }
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val user_location = LatLng(latitude!!,longitude!!)
        //mMap.addMarker(MarkerOptions().position(user_location).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location))
        Log.d("日ああああああ", "x=" + user_location);
    }
}