package com.android.compose.typeSafeNavigation.no_routes.subGraph

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class Graphs {
    @Serializable
    data object GraphOne: Graphs()

    @Serializable
    data object GraphTwo: Graphs()
}

sealed class Destination {
    @Serializable
    data object ScreenOne: Destination()

    @Serializable
    data object ScreenTwo: Destination()

    @Serializable
    data object ScreenThree: Destination()

    @Serializable
    data class ScreenFour(val name: String): Destination()

    @Serializable
    data class ScreenFive(val mainGraph: MainGraph): Destination()
}

@Parcelize
@Serializable
data class MainGraph(val nameOfTheGraph: String) : Parcelable