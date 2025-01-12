package com.android.compose.typeSafeNavigation.no_routes.nonSubGraph

import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen) {
        composable<Screen.HomeScreen> {
            ScreenDetail(name = "Home Screen") {
                navController.navigate(Screen.DetailScreen(first = "Himanshu", age = 25))
            }
        }

        composable<Screen.DetailScreen> {
            val data = it.toRoute<Screen.DetailScreen>()
            val nameAndAge = "First Name: ${data.first} and age: ${data.age}"
            ScreenDetail(name = nameAndAge) {
                navController.navigate(Screen.DetailScreen2(dummy = Dummy(nameAndAge = nameAndAge.replace(oldValue = "and", newValue = "&&"))))
            }
        }

        composable<Screen.DetailScreen2>(typeMap = mapOf(typeOf<Dummy>() to DummyType)) {
            val data = it.toRoute<Screen.DetailScreen2>()
            ScreenDetail(name = data.dummy.nameAndAge) { }
        }
    }
}

@Composable
fun ScreenDetail(name: String, onClick: () -> Unit) {
   Text(text = name, modifier = Modifier
       .fillMaxSize()
       .wrapContentSize()
       .clickable { onClick() })
}

val DummyType = object : NavType<Dummy>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Dummy? {
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(key, Dummy::class.java)
        else bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Dummy {
        return Json.decodeFromString<Dummy>(value)
    }

    override fun put(bundle: Bundle, key: String, value: Dummy) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Dummy): String {
        return Json.encodeToString(value)
    }
}