package com.android.compose.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.compose.other.Resource
import com.android.compose.data.remote.ImageResponse
import com.android.compose.data.local.ShoppingItem

class FakeShoppingRepository : ShoppingRepository {
    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val liveDataShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val liveDataTotalPrice = MutableLiveData<Float>()
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        this.shouldReturnNetworkError = value
    }

    private fun refresh() {
        liveDataShoppingItems.postValue(shoppingItems)
        liveDataTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice() : Float {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(element = shoppingItem)
        refresh()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(element = shoppingItem)
        refresh()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
       return liveDataShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return liveDataTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) Resource.error(msg = "SomeThing Went Wrong Try Again Later!")
        else Resource.success(data = ImageResponse(hits = listOf(), total = 0, totalHits = 0))
    }

}
