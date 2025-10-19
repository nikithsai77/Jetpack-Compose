package com.android.compose.repositories

import androidx.lifecycle.LiveData
import com.android.compose.other.Resource
import com.android.compose.data.remote.ImageResponse
import com.android.compose.data.local.ShoppingItem

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}
