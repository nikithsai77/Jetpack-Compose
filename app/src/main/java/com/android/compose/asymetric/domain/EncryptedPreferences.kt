package com.android.compose.asymetric.domain

import com.android.compose.asymetric.data.Keys

interface EncryptedPreferences {
    suspend fun saveEncryptedData(keys: Keys) : Boolean
    suspend fun readEncryptedData() : Keys?
    suspend fun areApiKeysReady() : Boolean
}
