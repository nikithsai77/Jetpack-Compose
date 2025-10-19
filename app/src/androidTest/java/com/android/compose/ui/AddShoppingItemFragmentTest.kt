package com.android.compose.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.android.compose.R
import com.android.compose.data.local.ShoppingItem
import com.android.compose.ext.launchFragmentInHiltContainer
import com.android.compose.other.Status
import com.android.compose.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var factory: ShoppingFragmentFactory

    @Before
    fun setUp() {
        hilt.inject()
    }

    @Test
    fun clickInsertIntoDb() {
        val navController = mock(NavController::class.java)

        val viewModel = ShoppingViewModel(repository = FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory = factory) {
            (this as AddShoppingItemFragment).navController = navController
            this.viewModel = viewModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("ShoppingI"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))

        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(viewModel.insertShoppingItemStatus.value?.peekContent()?.status).isEqualTo(Status.SUCCESS)
        assertThat(viewModel.insertShoppingItemStatus.value?.peekContent()?.data).isEqualTo(
            ShoppingItem(name = "ShoppingI", amount = 5, price = 5.5f, imageUrl = "")
        )
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            (this as AddShoppingItemFragment).navController = navController
        }

        pressBack()
        verify(navController).popBackStack()
    }

}
