package com.android.compose.asymetric.data

import androidx.datastore.core.Serializer
import com.android.compose.asymetric.KeyPairHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

@Serializable
data class Keys(
    val firstKey: String = "",
    val secondKey: String = "",
)

object KeysSerializer : Serializer<Keys> {
    override val defaultValue: Keys get() = Keys()

    override suspend fun readFrom(input: InputStream): Keys {
        return try {
            // Read bytes from input stream
            val encryptedBytes = withContext(Dispatchers.IO) {
                input.use { it.readBytes() }
            }

            // Decode and decrypt the bytes
            val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
            val decryptedBytes = KeyPairHandler.decrypt(bytes = encryptedBytesDecoded)

            // Convert decrypted bytes to string and deserialize JSON
            val decodedJsonString = decryptedBytes.decodeToString()
            Json.decodeFromString<Keys>(decodedJsonString)
        } catch (e: Exception) {
            println("kk error during readFrom: ${e.message}")
            defaultValue
        }
    }

    override suspend fun writeTo(t: Keys, output: OutputStream) {
        try {
            // Convert Keys object to JSON string
            val json = Json.encodeToString(t)
            val bytes = json.toByteArray()

            // Encrypt the bytes and encode them in Base64
            val encryptedBytes = KeyPairHandler.encrypt(bytes)
            val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)

            // Write the Base64-encoded encrypted bytes to the output stream
            withContext(Dispatchers.IO) {
                output.use { it.write(encryptedBytesBase64) }
            }
        } catch (e: Exception) {
            println("kk Error during writeTo: ${e.message}")
        }
    }

}
