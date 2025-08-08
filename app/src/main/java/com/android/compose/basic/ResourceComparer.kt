package com.android.compose.basic

import android.content.Context

class ResourceComparer {

    fun isEqual(context: Context, resId: Int, content: String): Boolean {
        return context.getString(resId) == content
    }

}