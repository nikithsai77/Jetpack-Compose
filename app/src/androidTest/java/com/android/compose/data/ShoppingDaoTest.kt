package com.android.compose.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.compose.data.local.ShoppingItem
import com.android.compose.data.local.ShoppingItemDatabase
import com.android.compose.data.remote.local.local.ShoppingDao
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ShoppingDaoTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setUp() {
        hiltRule.inject()
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