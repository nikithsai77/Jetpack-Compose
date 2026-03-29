package com.android.compose.symetric

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val symmetricKey = Symmetric
            val str = "Hello World!"
            println("kk aa actual data : $str")

            val encryptedData = symmetricKey.encryptData(data = str)
            println("kk aa encrypt: $encryptedData")

            val decryptData = symmetricKey.decryptData(encryptedData = encryptedData)
            println("kk aa decrypt: $decryptData")

            println("kk aa --------------------------")

            val str1 = "Hello World!"
            println("kk aa actual data : $str1")

            val encryptedData123 = symmetricKey.encryptData(data = str1)
            println("kk aa encrypt: $encryptedData123")

            val decryptData123 = symmetricKey.decryptData(encryptedData = encryptedData123)
            println("kk aa decrypt: $decryptData123")
        }
    }

}