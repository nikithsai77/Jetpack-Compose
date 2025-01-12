package com.android.compose.typeSafeNavigation.no_routes.subGraph

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlin.reflect.typeOf

@Composable
fun SubGraphNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Graphs.GraphOne) {

        navigation<Graphs.GraphOne>(startDestination = Destination.ScreenOne) {
            composable<Destination.ScreenOne> {
                DisplayContent(text = "Screen 1") {
                    navController.navigate(Destination.ScreenTwo)
                }
            }

            composable<Destination.ScreenTwo> {
                DisplayContent(text = "Screen 2") {
                    navController.navigate(Destination.ScreenFour(name = "Welcome To Screen 4"))
                }
            }
        }

        navigation<Graphs.GraphTwo>(startDestination = Destination.ScreenThree) {
            composable<Destination.ScreenThree> {
                DisplayContent(text = "Screen 3") {
                    navController.navigate(Destination.ScreenFour(name = "Welcome To Screen 4"))
                }
            }

            composable<Destination.ScreenFour> {
                val data = it.toRoute<Destination.ScreenFour>().name
                DisplayContent(text = data) {
                    navController.navigate(Destination.ScreenFive(mainGraph = MainGraph(nameOfTheGraph = "WelCome To Screen 5 Graph")))
                }
            }

            composable<Destination.ScreenFive>(typeMap = mapOf(typeOf<MainGraph>() to customNavType)) {
                val stringText = it.toRoute<Destination.ScreenFive>().mainGraph.nameOfTheGraph
                DisplayContent(text = stringText) {}
            }
        }
    }
}

@Composable
fun DisplayContent(text: String, onClick: ()-> Unit) {
    Text(text = text, modifier = Modifier.fillMaxSize().wrapContentSize().clickable { onClick() })
}