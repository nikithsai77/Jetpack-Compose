package com.android.compose.typeSafeNavigation.no_routes.subGraph

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

val customNavType = object : NavType<MainGraph>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): MainGraph? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(key, MainGraph::class.java)
        else bundle.getParcelable(key)
    }

    override fun parseValue(value: String): MainGraph {
       return Json.decodeFromString<MainGraph>(value)
    }

    override fun put(bundle: Bundle, key: String, value: MainGraph) {
       bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: MainGraph): String {
        return Json.encodeToString(value)
    }
}