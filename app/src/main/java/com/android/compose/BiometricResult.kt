package com.android.compose

sealed interface BiometricResult {
    data object HardwareUnavailableInTheDevice : BiometricResult
    data object FeatureUnAvailableInTheDevice : BiometricResult
    data class AuthenticationError(val error: String) : BiometricResult
    data object AuthenticationSuccess : BiometricResult
    data object AuthenticationFailed : BiometricResult
    data object AuthenticationNoSet : BiometricResult
}

fun BiometricResult.getText() : String {
    return when(this) {
        BiometricResult.HardwareUnavailableInTheDevice -> "Hardware UnAvailable"
        BiometricResult.FeatureUnAvailableInTheDevice -> "Feature UnAvailable"
        BiometricResult.AuthenticationSuccess -> "Success"
        BiometricResult.AuthenticationFailed -> "Failed Try Again Later!"
        BiometricResult.AuthenticationNoSet -> "Authentication Not Set"
        is BiometricResult.AuthenticationError -> this.error
    }
}