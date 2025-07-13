package com.android.compose.service.boundService.localBinding

import android.os.Binder
import android.os.IBinder
import android.app.Service
import android.widget.Toast
import android.content.Intent
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.TranslateLanguage

class TranslatorBoundService : Service() {
    var binder : CustomIBinder? = CustomIBinder()

    inner class CustomIBinder : Binder() {
        fun getService() : TranslatorBoundService { return this@TranslatorBoundService }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Local Bound Service Started....", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun translate(text: String, onResult: (String) -> Unit, onFailure: (String) -> Unit) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.TELUGU)
            .build()

        //This creates a client based upon the provided options, so he client will knows from which language to another language to Translate.
        val client = Translation.getClient(options)

        //each language has a pair to translate the one lang to another lang
        //so client needs a this model to translate.
        client.downloadModelIfNeeded().addOnSuccessListener {
            client.translate(text)
                .addOnSuccessListener {
                    onResult(it)
                }
                .addOnFailureListener {
                    onFailure(it.message?:"Some")
                }
        }.addOnFailureListener {
            println("kk Failure To Downloaded The Model: $it")
        }
    }

}