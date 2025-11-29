package com.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo = when {
            configuration.screenWidthDp < 600 -> WindowInfo.WindowType.COMPACT
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.MEDIUM
            else -> WindowInfo.WindowType.EXPANDED
        },
        screenHeightInfo = when {
            configuration.screenHeightDp < 480 -> WindowInfo.WindowType.COMPACT
            configuration.screenHeightDp < 900 -> WindowInfo.WindowType.MEDIUM
            else -> WindowInfo.WindowType.EXPANDED
        }
    )
}

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType
) {
    sealed interface WindowType {
        object COMPACT : WindowType
        object MEDIUM : WindowType
        object EXPANDED : WindowType
    }
}
