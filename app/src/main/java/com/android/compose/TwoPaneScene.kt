package com.android.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass

/** Scene is used to represents the Adaptive UI */
class TwoPaneScene<T : Any>(
    /** is the unique ID Of Scene*/
    override val key: Any,
    /** first composable */
    val firstEntry: NavEntry<T>,
    /** second composable */
    val secondEntry: NavEntry<T>,
    /** is the used to show the composable when user press the back button */
    override val previousEntries: List<NavEntry<T>>
) : Scene<T> {

    /** It is used to contains the all composable's */
    override val entries: List<NavEntry<T>> = listOf(firstEntry, secondEntry)

    /** It is used to render the all composable's on UI */
    override val content: @Composable (() -> Unit) = {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(weight = 0.5f)) {
                firstEntry.Content()
            }
            Column(modifier = Modifier.weight(weight = 0.5f)) {
                secondEntry.Content()
            }
        }
    }

    companion object {
        const val TWO_PANE = "TwoPane"
        fun twoPane() = mapOf(TWO_PANE to true)
    }

}

/** Is Used to tell the navigation 3 to use the Adaptive UI*/
class TwoPaneSceneStrategy<T : Any>(
    private val windowSizeClass: WindowSizeClass
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (windowSizeClass.isWidthAtLeastBreakpoint(widthDpBreakpoint = 600).not()) {
            return null
        }

        val firstEntry = entries.first()
        val secondEntry = entries.last()

        val sceneKey = Pair(first = firstEntry, second = secondEntry)

        return if (entries.size == 2 && firstEntry.metadata.contains(TwoPaneScene.TWO_PANE)
            && secondEntry.metadata.contains(TwoPaneScene.TWO_PANE)
        ) {
            TwoPaneScene(
                key = sceneKey,
                firstEntry = firstEntry,
                secondEntry = secondEntry,
                previousEntries = entries.drop(n = 1)
            )
        }
        else  null
    }

}
