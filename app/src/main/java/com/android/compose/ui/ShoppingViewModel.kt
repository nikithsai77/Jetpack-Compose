package com.android.compose.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.compose.data.remote.ImageResponse
import com.android.compose.data.local.ShoppingItem
import com.android.compose.other.Constant
import com.android.compose.other.Event
import com.android.compose.other.Resource
import com.android.compose.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {
    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl = _imageUrl as LiveData<String>

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setImageUrl(url: String) {
        _imageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem = shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem = shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.trim().isEmpty() || amountString.trim().isEmpty() || priceString.trim().isEmpty())
            _insertShoppingItemStatus.postValue(Event(content = Resource.error(msg = "The Fields must not be empty")))
        else if (name.length > Constant.MAX_NAME_LENGTH)
            _insertShoppingItemStatus.postValue(Event(content = Resource.error(msg = "The name of the item must not exceed ${Constant.MAX_NAME_LENGTH} characters")))
        else if (priceString.length > Constant.MAX_PRICE_LENGTH)
            _insertShoppingItemStatus.postValue(Event(content = Resource.error(msg = "The Price of the item must no exceed ${Constant.MAX_PRICE_LENGTH} characters")))
        else {
            val amount = try {
                amountString.toInt()
            } catch (e: Exception) {
                _insertShoppingItemStatus.postValue(Event(content = Resource.error(msg = "Please Enter a valid number")))
                return
            }
            val shoppingItem = ShoppingItem(name = name, amount = amount, price = priceString.toFloat(), imageUrl = _imageUrl.value ?: "")
            insertShoppingItemIntoDb(shoppingItem)
            setImageUrl("")
            _insertShoppingItemStatus.postValue(Event(content = Resource.success(data = shoppingItem)))
        }
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) return
        _images.value = Event(content = Resource.loading())
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery = imageQuery)
            _images.value = Event(content = response)
        }
    }

}
