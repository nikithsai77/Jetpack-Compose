package com.android.compose.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.compose.MainDispatcherRule
import com.android.compose.getTheValueOfLiveData
import com.android.compose.other.Constant
import com.android.compose.other.Status
import com.android.compose.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineDispatcher = MainDispatcherRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(repository = FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        val result = viewModel.insertShoppingItemStatus.getTheValueOfLiveData {
            viewModel.insertShoppingItem(name = "", amountString = "", priceString = "3.0f")
        }
        assertThat(result.getContent()?.status).isEqualTo(Status.ERROR)
        assertThat(result.peekContent().error).isEqualTo("The Fields must not be empty")
    }

    @Test
    fun `insert shopping item with too long name then it should returns error`() {
        val name = buildString {
            repeat(times = Constant.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        val result = viewModel.insertShoppingItemStatus.getTheValueOfLiveData {
            viewModel.insertShoppingItem(name = name, amountString = "3.0f", priceString = "3.0f")
        }
        assertThat(result.getContent()?.status).isEqualTo(Status.ERROR)
        assertThat(result.peekContent().error).isEqualTo("The name of the item must not exceed ${Constant.MAX_NAME_LENGTH} characters")
    }

    @Test
    fun `insert shopping item with too long price then it should returns error`() {
        val price = buildString {
            (1..Constant.MAX_PRICE_LENGTH + 1).forEach { _ ->
                append(1)
            }
        }
        val result = viewModel.insertShoppingItemStatus.getTheValueOfLiveData {
            viewModel.insertShoppingItem(name = "name", amountString = "5", priceString = price)
        }
        assertThat(result.getContent()?.status).isEqualTo(Status.ERROR)
        assertThat(result.peekContent().error).isEqualTo("The Price of the item must no exceed ${Constant.MAX_PRICE_LENGTH} characters")
    }

    @Test
    fun `insert shopping item with too high amount (or) non digit then returns error`() {
        val result = viewModel.insertShoppingItemStatus.getTheValueOfLiveData {
            viewModel.insertShoppingItem(
                name = "name",
                amountString = "9999999999999999999",
                priceString = "3.0"
            )
        }
        assertThat(result.getContent()?.status).isEqualTo(Status.ERROR)
        assertThat(result.peekContent().error).isEqualTo("Please Enter a valid number")
    }

    @Test
    fun `insert shopping item with all valid input then return success`() {
        val result = viewModel.insertShoppingItemStatus.getTheValueOfLiveData {
            viewModel.insertShoppingItem(name = "IPhone", amountString = "5", priceString = "3.0")
        }
        assertThat(result.getContent()?.status).isEqualTo(Status.SUCCESS)
        assertThat(result.peekContent().data).isNotNull()
        assertThat(result.peekContent().data!!.name).isEqualTo("IPhone")
        assertThat(result.peekContent().data!!.price).isEqualTo(3.0f)
        assertThat(result.peekContent().data!!.amount).isEqualTo(5)
        val str = viewModel.imageUrl.value
        assertThat(str).isNotNull()
        assertThat(str).isEmpty()
    }

}