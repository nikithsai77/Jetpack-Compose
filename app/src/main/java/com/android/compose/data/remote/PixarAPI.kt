package com.android.compose.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixarAPI {
    @GET("/api/")
    suspend fun searchForImage(@Query("q") searchQuery: String, @Query("key") apiKey: String = ""): Response<ImageResponse>
}