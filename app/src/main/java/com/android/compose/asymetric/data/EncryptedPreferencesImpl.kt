package com.android.compose.asymetric.data

import android.content.Context
import androidx.datastore.dataStore
import com.android.compose.asymetric.domain.EncryptedPreferences
import kotlinx.coroutines.flow.firstOrNull

private val Context.dataStore by dataStore(
    fileName = EncryptedPreferencesImpl.PREF_NAME,
    serializer = KeysSerializer
)

class EncryptedPreferencesImpl(context: Context) : EncryptedPreferences {
    private val dataStore = context.dataStore

    override suspend fun saveEncryptedData(keys: Keys): Boolean {
        return try {
            dataStore.updateData { keys }
            true
        } catch (e: Exception) {
            println("kk error during saveEncryptedData: ${e.message}")
            false
        }
    }

    override suspend fun readEncryptedData(): Keys? {
        return dataStore.data.firstOrNull()
    }

    override suspend fun areApiKeysReady(): Boolean {
        return try {
            val keys = readEncryptedData()
            keys != null && keys.firstKey.isNotEmpty() && keys.secondKey.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    companion object {
        const val PREF_NAME = "apiKeys"
    }

}
