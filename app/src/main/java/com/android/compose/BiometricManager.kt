package com.android.compose

import android.os.Build
import androidx.biometric.BiometricPrompt
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.appcompat.app.AppCompatActivity
import android.hardware.biometrics.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL

class BiometricManager(private val context: AppCompatActivity) {
    private val resultChannel = Channel<BiometricResult>()
    val promptResult = resultChannel.receiveAsFlow()

    fun showBiometric(title: String, description: String) {
        val manager = androidx.biometric.BiometricManager.from(context)
        val authenticators = if(Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        when(manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(element = BiometricResult.FeatureUnAvailableInTheDevice)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(element = BiometricResult.HardwareUnavailableInTheDevice)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(element = BiometricResult.AuthenticationNoSet)
                return
            }
            else -> Unit
        }

        //Used to display the dialog with authentication and setting the callback.
        val dialogBioMetric = BiometricPrompt(context, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    resultChannel.trySend(element = BiometricResult.AuthenticationSuccess)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    resultChannel.trySend(element = BiometricResult.AuthenticationFailed)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    resultChannel.trySend(element = BiometricResult.AuthenticationError(error = errString.toString()))
                }
            })

        //setting the title and description, authentication to the dialog.
        //simply binding all information to the dialog.
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
            .build()

        //displays the dialog with title & authentication details.
        dialogBioMetric.authenticate(promptInfo)
    }

}