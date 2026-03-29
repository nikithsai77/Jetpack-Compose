package com.android.compose.symetric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

private const val ALIAS = "Alias"
private const val ANDROID_KEYSTORE = "AndroidKeyStore"

object Symmetric {

    // Hold's the KeyStore Instance
    val keyStoreInstance : KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        // null returns the android os AndroidKeyStore system
        // if we want the database or file manager key store instance then
        // pass required info here.
        load(null)
    }

    // Generate The Key
    fun generateSecretKey() : SecretKey {
        // Here I want to generate the symmetric cryptographic key by using with AES
        // and wants to store in android key store and key store protects it.
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        // I want to store the symmetric key of keyGenerator under alias
        // and adding purpose of the key in android key store and Internally it stores the key with alias.
        // The Alias is a unique identifier string it allows you to retrieve the key later from the key store.
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            // How should symmetric Algorithm (AES) process on multiple block's.
            // GCM Makes the symmetric Algorithm (AES) to Behave like Stream Cipher. (So it works any length of the data)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            // If data block has unfill space then padding will add extra bytes to fill the data block
            // and later decryption, key removes the extra added bytes and do the decryption.
            // Here GCM mode applied so padding is not required, bez it works with any length of the data.
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            // Enable new iv for each new key.
            .setRandomizedEncryptionRequired(true)
            // Here I don't want the authentication to use the key.
            .setUserAuthenticationRequired(false)
            // Create The Alias with these required Info.
            .build()

        // Passing This Info To The Key Store While Creating the Symmetric Key With AES.
        keyGenerator.init(keyGenParameterSpec)

        // Generator the key under alias in keyStore.
        return keyGenerator.generateKey()
    }

    // Returns the key which is in stored in Alias.
    fun getSecretKey() : SecretKey {
//        val okay = keyStoreInstance.getKey(ALIAS, null) as? SecretKey
//        val okay123 = okay ?: generateSecretKey()
//        println(okay123)
        val key = keyStoreInstance.getEntry(ALIAS, null) as? KeyStore.SecretKeyEntry
        return key?.secretKey ?: generateSecretKey()
    }

    // This Cipher is used to do the encryption and it must and should matches the key properties for encryption.
    private val encryptCipher get() = Cipher.getInstance("AES/GCM/NoPadding").apply {
        init(Cipher.ENCRYPT_MODE, getSecretKey())
    }

    // This Cipher is used to do the decryption and it must and should matches the key properties for decryption.
    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance("AES/GCM/NoPadding").apply {
            val spec = GCMParameterSpec(128, iv)
            init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        }
    }

    // Converting The Data To Unreadable Format.
    fun encryptData(data: String) : EncryptedData {
        val encryptionKey = encryptCipher
        val cipherText = encryptionKey.doFinal(data.toByteArray(Charsets.UTF_8))
        return EncryptedData(cipherText = cipherText, iv = encryptionKey.iv)
    }

    // Converting the Data To Original And Readable Format.
    fun decryptData(encryptedData: EncryptedData) : String {
        val decrypt = getDecryptCipherForIv(iv = encryptedData.iv)
        val text = decrypt.doFinal(encryptedData.cipherText).decodeToString()
        return text
    }

}
