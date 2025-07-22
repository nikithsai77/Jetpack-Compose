package com.android.compose.locationTracking

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.compose.compose.MButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class ForeGroundLocationActivity: ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val permissions = rememberMultiplePermissionsState(permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                else listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
            LaunchedEffect(key1 = Unit) {
                permissions.launchMultiplePermissionRequest()
            }
            when {
                permissions.allPermissionsGranted -> {
                    LocationGranted(context = this@ForeGroundLocationActivity)
                }

                permissions.shouldShowRationale -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MButton(name = "Give Permission") {
                            permissions.launchMultiplePermissionRequest()
                        }
                    }
                }
            }
        }
    }

}