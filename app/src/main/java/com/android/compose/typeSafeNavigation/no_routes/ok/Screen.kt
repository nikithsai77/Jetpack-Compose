package com.android.compose.typeSafeNavigation.no_routes.ok

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object HomeScreen: Screen()

    @Serializable
    data class DetailScreen(val first: String, val age: Int): Screen()

    @Serializable
    data class DetailScreen2(val dummy: Dummy) : Screen()
}

@Parcelize
@Serializable
data class Dummy(val nameAndAge: String): Parcelable