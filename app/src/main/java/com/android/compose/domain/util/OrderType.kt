package com.android.compose.domain.util

sealed interface OrderType {
    data object ASCENDING : OrderType
    data object DESCENDING : OrderType
}
