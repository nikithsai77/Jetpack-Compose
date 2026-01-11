package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.android.compose.composable.DetailScreen
import com.android.compose.composable.ListScreen
import com.android.compose.ui.theme.ComposeTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    sealed interface Dest : NavKey {
        @Serializable
        data object ListScreen : Dest

        @Serializable
        data class DetailScreen(val content: String) : Dest
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backStack = rememberNavBackStack(Dest.ListScreen)
                    NavDisplay(
                        modifier = Modifier.padding(paddingValues = innerPadding),
                        backStack = backStack,
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        sceneStrategy = rememberAdaptiveSceneStrategy(),
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            entry<Dest.ListScreen>(
                                metadata = TwoPaneScene.twoPane() + NavDisplay.transitionSpec {
                                    // Animation for Navigation.
                                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                                } + NavDisplay.popTransitionSpec {
                                    // Animation for pop up.
                                    slideInVertically { it } togetherWith slideOutVertically { it }
                                } + NavDisplay.predictivePopTransitionSpec {
                                    // Animation for when user press back button.
                                    slideInVertically { it } togetherWith slideOutVertically { it }
                                }
                            ) {
                                val viewModel = viewModel<MainViewModel>()

                                LaunchedEffect(key1 = Unit) {
                                    viewModel.updateTag(tag = "List Screen")
                                }

                                ListScreen {
                                    backStack.add(Dest.DetailScreen(content = it))
                                }
                            }

                            entry<Dest.DetailScreen>(
                                metadata = TwoPaneScene.twoPane() + NavDisplay.transitionSpec {
                                    // Animation for Navigation.
                                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                                } + NavDisplay.popTransitionSpec {
                                    // Animation for pop up.
                                    slideInVertically { it } togetherWith slideOutVertically { it }
                                } + NavDisplay.predictivePopTransitionSpec {
                                    // Animation for when user press back button.
                                    slideInVertically { it } togetherWith slideOutVertically { it }
                                }
                            ) { key ->
                                val mainViewModel = viewModel<MainViewModel>()

                                LaunchedEffect(key1 = Unit) {
                                    mainViewModel.updateTag(tag = "Detail Screen")
                                }

                                DetailScreen(content = key.content)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun rememberAdaptiveSceneStrategy(): TwoPaneSceneStrategy<Any> {
    val windowInfo = currentWindowAdaptiveInfo()
    return retain(windowInfo.windowSizeClass) {
        TwoPaneSceneStrategy(windowSizeClass = windowInfo.windowSizeClass)
    }
}
