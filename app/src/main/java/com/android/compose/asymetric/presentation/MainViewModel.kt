package com.android.compose.asymetric.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import retrofit2.Retrofit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.compose.asymetric.KeyPairHandler
import com.android.compose.asymetric.data.Keys
import com.android.compose.asymetric.domain.EncryptedPreferences
import com.android.compose.asymetric.domain.KeyProviderService
import com.android.compose.asymetric.presentation.util.RequestState
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import java.lang.Exception

private const val BASE_URL = "http://10.0.2.2:8080"

class MainViewModel(
    private val preferences: EncryptedPreferences
) : ViewModel() {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val _apiKeysReady: MutableState<RequestState<Boolean>> = mutableStateOf(value = RequestState.Idle)
    val apiKeysReady: State<RequestState<Boolean>> = _apiKeysReady

    private val _apiKeys: MutableState<Keys?> = mutableStateOf(value = null)
    val apiKeys: State<Keys?> = _apiKeys

    init {
        fetchData()
    }

    private suspend fun fetchApiKeysAndStoreThemSecurely(): RequestState<Boolean> {
        return try {
            KeyPairHandler.generateKey()
            val publicKey = KeyPairHandler.getPublicKeyString()
            val fetchedData = sendToServer(publicKey = publicKey)
            fetchedData?.let {
                val decryptedData = KeyPairHandler.decryption(data = it)
                val keys = Json.decodeFromString<Keys>(string = decryptedData)
                val result = preferences.saveEncryptedData(keys = keys)
                _apiKeys.value = preferences.readEncryptedData()
                RequestState.Success(data = result)
            } ?: throw ApiKeysException(message = "Failed to Fetch API Key's.")
        } catch (e: Exception) {
            RequestState.Error(message = "$e.message")
        }
    }

    private suspend fun sendToServer(publicKey: String): String? {
        val response = retrofit.create(KeyProviderService::class.java).getEncryptedAPiKeys(publicKey = publicKey)
        return if (response.isSuccessful) response.body()
        else throw ApiKeysException(message = response.message())
    }

    fun fetchData() = viewModelScope.launch {
        _apiKeysReady.value = RequestState.Loading
        delay(timeMillis = 1000)
        _apiKeysReady.value = fetchApiKeysAndStoreThemSecurely()
    }

}

class ApiKeysException(message: String) : kotlin.Exception(message)
