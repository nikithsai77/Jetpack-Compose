package com.android.compose.locationTracking

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.compose.R
import com.android.compose.musicPlayer.CHANNEL_ID
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationService: Service() {

    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setIntervalMillis(1000)
            .build()
    }

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                location.lastLocation?.let {
                    val lat = it.latitude.toString()
                    val long = it.longitude.toString()
                    startServiceForeground(lat = lat, lng = long)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationUpdates()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun locationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) return
        else {
            val fusedLocation = LocationServices.getFusedLocationProviderClient(this)
            fusedLocation.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun startServiceForeground(lat: String, lng: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Location Update")
            .setContentText("$lat, $lng")
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) startForeground(1, notification)
        else startForeground(1, notification)
    }

}