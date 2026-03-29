package com.android.compose.asymetric.domain

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers

interface KeyProviderService {
    @Headers("Content-Type: application/json")
    @POST("api/provide")
    suspend fun getEncryptedAPiKeys(@Body publicKey: String) : Response<String>
}
