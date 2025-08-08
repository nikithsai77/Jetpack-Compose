package com.android.compose

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.android.compose.basic.ResourceComparer
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ResourceComparerTest {
    private var resourceComparer: ResourceComparer? = null

    @Before
    fun setUp() {
        resourceComparer = ResourceComparer()
    }

    @After
    fun cleanUp() {
        resourceComparer = null
    }

    @Test
    fun ifPassedContentIsEqualThenReturnTrue() {
        resourceComparer?.let {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val isEqual = it.isEqual(context, resId = R.string.app_name, content = "Testing")
            assertThat(isEqual).isTrue()
        } ?: assertThat(false).isTrue()
    }

    @Test
    fun ifPassedContentIsEqualThenReturnFalse() {
        resourceComparer?.let {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val isEqual = it.isEqual(context, resId = R.string.app_name, content = "Testing123")
            assertThat(isEqual).isFalse()
        } ?: assertThat(false).isTrue()
    }

}