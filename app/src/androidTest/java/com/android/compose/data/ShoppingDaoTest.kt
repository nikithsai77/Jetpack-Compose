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
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
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
        val allItems : List<ShoppingItem>? = dao.observeAllShoppingItems().getTheValue {
            dao.insertShoppingItem(shoppingItem = shoppingItem)
        }
        assertThat(allItems).isNotNull()
        assertThat(allItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(id = 1, name = "item", amount = 1, price = 0f, imageUrl = "")
        val allItems : List<ShoppingItem>? = dao.observeAllShoppingItems().getTheValue {
            dao.insertShoppingItem(shoppingItem = shoppingItem)
            dao.deleteShoppingItem(shoppingItem = shoppingItem)
        }
        assertThat(allItems).isNotNull()
        assertThat(allItems).isEmpty()
        assertThat(allItems).doesNotContain(shoppingItem)
    }

    @Test
    fun totalPriceSum() = runTest {
        val shoppingItem1 = ShoppingItem(id = 1, name = "item", amount = 1, price = 10f, imageUrl = "")
        val shoppingItem2 = ShoppingItem(id = 2, name = "item", amount = 2, price = 20f, imageUrl = "")
        val shoppingItem3 = ShoppingItem(id = 3, name = "item", amount = 3, price = 30f, imageUrl = "")
        val totalPriceSum = dao.observeTotalPrice().getTheValue {
            dao.insertShoppingItem(shoppingItem = shoppingItem1)
            dao.insertShoppingItem(shoppingItem = shoppingItem2)
            dao.insertShoppingItem(shoppingItem = shoppingItem3)
        }
        assertThat(totalPriceSum).isEqualTo( 1 * 10f + 2 * 20f + 3 * 30f)
    }

}