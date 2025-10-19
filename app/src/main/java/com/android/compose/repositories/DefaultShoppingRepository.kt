package com.android.compose.repositories

import androidx.lifecycle.LiveData
import com.android.compose.data.remote.ImageResponse
import com.android.compose.data.remote.PixarAPI
import com.android.compose.data.remote.local.local.ShoppingDao
import com.android.compose.data.local.ShoppingItem
import com.android.compose.other.Resource

class DefaultShoppingRepository(private val dao: ShoppingDao, private val pixarAPI: PixarAPI) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
       dao.insertShoppingItem(shoppingItem = shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        dao.deleteShoppingItem(shoppingItem = shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return dao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return dao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixarAPI.searchForImage(searchQuery = imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(data = it)
                } ?: Resource.error(msg = "An unknown error occurred")
            }
            else Resource.error(msg = "An unknown error occurred")
        } catch (e: Exception) {
            Resource.error(msg = e.message ?: "Couldn't reach the server. Check your internet connection")
        }
    }

}
