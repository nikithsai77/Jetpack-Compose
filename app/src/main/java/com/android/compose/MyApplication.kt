package com.android.compose

import coil.ImageLoader
import android.app.Application
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger

class MyApplication() : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(context = this).newBuilder()
            .memoryCachePolicy(policy = CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(context = this)
                    .maxSizePercent(percent = 0.1)
                    .strongReferencesEnabled(enable = true)
                    .build()
            }
            .diskCachePolicy(policy = CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(percent = 0.3)
                    .directory(directory = cacheDir)
                    .build()
            }
            .logger(logger = DebugLogger())
            .build()
    }

}