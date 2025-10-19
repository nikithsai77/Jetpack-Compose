package com.android.compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.compose.data.remote.local.local.ShoppingDao

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingItemDatabase : RoomDatabase() {
    abstract fun shoppingDao(): ShoppingDao
}