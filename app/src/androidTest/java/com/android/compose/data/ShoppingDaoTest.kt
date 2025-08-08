package com.android.compose.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.compose.local.ShoppingDao
import com.android.compose.local.ShoppingItem
import com.android.compose.local.ShoppingItemDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingDaoTest {
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), ShoppingItemDatabase::class.java).allowMainThreadQueries().build()
        dao = database.shoppingDao()
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(id = 1, name = "item", amount = 1, price = 0f, imageUrl = "")
        dao.insertShoppingItem(shoppingItem = shoppingItem)
        val allItems : List<ShoppingItem>? = dao.observeAllShoppingItems().getValue()
        if (allItems == null) assertThat(false).isTrue()
        else assertThat(allItems).contains(shoppingItem)
    }












}