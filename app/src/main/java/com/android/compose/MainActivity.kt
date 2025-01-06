package com.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.compose.sharedElementTransition.DetailScreen
import com.android.compose.sharedElementTransition.ListScreen
import com.android.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {

    @ExperimentalSharedTransitionApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                val controller = rememberNavController()
                SharedTransitionLayout {
                    NavHost(navController = controller, startDestination = "list") {
                        composable(route = "list") {
                            ListScreen(animatedVisibilityScope = this, onItemClick =  { resId, title ->
                                controller.navigate(route = "Detail/$resId/$title")
                            })
                        }
                        composable(route = "Detail/{resId}/{text}", arguments = listOf(
                            navArgument(name = "resId") {
                                type = NavType.IntType
                            },
                            navArgument(name = "text") {
                                type = NavType.StringType
                            }
                        )) {
                            val resId = it.arguments?.getInt("resId") ?: 0
                            val title = it.arguments?.getString("text") ?: ""
                            DetailScreen(resId = resId, title = title, animatedVisibilityScope = this)
                        }
                    }
                }
            }
        }
    }

}