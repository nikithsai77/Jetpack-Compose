package com.android.compose.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

/**
 * Converts the receiver [Flow] into a [StateFlow] tied to the current [ViewModel] scope.
 * and this method only will be callable when the ViewModel context is available.
 *
 * The upstream flow remains active while there are active collectors and stops
 * after a 5-second timeout when no subscribers remain. This helps preserve state
 * across short-lived UI changes while avoiding unnecessary upstream work.
 *
 * @param initialValue The initial value emitted by the resulting [StateFlow].
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.stateInWhileSubscribed(initialValue: T) : StateFlow<T> {
    return this.stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = initialValue
    )
}
