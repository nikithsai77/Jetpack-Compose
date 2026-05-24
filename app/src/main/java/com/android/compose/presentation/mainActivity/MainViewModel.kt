package com.android.compose.presentation.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.compose.SearchManager
import com.android.compose.data.UserInfo
import com.android.compose.presentation.stateInWhileSubscribed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

/**
 * ViewModel responsible for managing user search and update operations.
 *
 * This ViewModel handles AppSearch database interactions, maintains the
 * UI state using StateFlow, performs debounced search queries, and updates
 * user completion status asynchronously. It also manages lifecycle-aware
 * resource cleanup for the active search session.
 */
class MainViewModel(
    private val searchManager: SearchManager
) : ViewModel() {
    /**
     * Holds the active search coroutine job.
     *
     * This job is used to debounce search requests by cancelling any
     * previously running search operation before starting a new one.
     * It helps prevent unnecessary database queries while the user is typing.
     */
    private var searchJob: Job? = null
    /**
     * Holds the database insertion coroutine job.
     *
     * This ensures that the sample data insertion process is triggered
     * only once during the lifecycle of the ViewModel, avoiding duplicate
     * insert operations.
     */
    private var insertOperationJob: Job? = null
    /**
     * Internal mutable state holder for the user info screen.
     *
     * This state flow maintains the latest UI state and should only be
     * modified within the ViewModel.
     */
    private val _state = MutableStateFlow(value = UserInfoListState())
    /**
     * Public immutable UI state exposed to observers.
     *
     * When the state flow starts collecting:
     * - The AppSearch database insertion operation is triggered once.
     * - The state is converted into a lifecycle-aware StateFlow
     *   using [stateInWhileSubscribed].
     *
     * The upstream flow remains active while there are active subscribers
     * and automatically stops after a short timeout when no collectors remain.
     */
    val state = _state
        .onStart {
            if (insertOperationJob == null) {
                insertOperationJob = insertIntoAppSearchDatabase()
            }
        }
        .stateInWhileSubscribed(initialValue = _state.value)

    /**
     * Inserts sample user data into the AppSearch database if the database is empty.
     *
     * This function initializes the search manager and checks whether any data
     * already exists in the database. If no records are found, it generates
     * a list of sample [com.android.compose.data.UserInfo] items and stores them using the search manager.
     *
     * The insertion is performed inside [viewModelScope] to ensure the operation
     * is lifecycle-aware and executed asynchronously.
     */
    private fun insertIntoAppSearchDatabase() = viewModelScope.launch {
        searchManager.init()
        if (searchManager.isDataIsEmptyInTheDatabase()) {
            val userInfos = (1..100).map {
                UserInfo(
                    nameSpace = "Work_Related",
                    id = UUID.randomUUID().toString(),
                    title = "User Title $it",
                    description = "User Description $it",
                    isDone = Random.Default.nextBoolean()
                )
            }
            searchManager.putItems(userInfos = userInfos)
        }
    }

    /**
     * Updates the current search query and performs a debounced search operation.
     *
     * Whenever the query changes:
     * - The UI state is updated immediately with the latest query.
     * - Any previously running search job is cancelled.
     * - A new search request is triggered after a short delay to avoid
     *   unnecessary frequent database calls while typing.
     *
     * The resulting search data is then stored in the UI state.
     *
     * @param query The latest search text entered by the user.
     */
    fun onSearchQueryChange(query: String) {
        _state.update {
            it.copy(searchQuery = query)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(timeMillis = 500L)
            val userInfoResult = searchManager.search(query = query)
            _state.update {
                it.copy(userInfoResult = userInfoResult)
            }
        }
    }

    /**
     * Updates the completion status of a specific [UserInfo] item.
     *
     * This function:
     * - Persists the updated item in the AppSearch database.
     * - Updates the in-memory UI state to immediately reflect the latest value.
     *
     * The operation is executed asynchronously using [viewModelScope].
     *
     * @param userInfo The target user item to update.
     * @param isDone The updated completion state.
     */
    fun onDoneChange(userInfo: UserInfo, isDone: Boolean) = viewModelScope.launch {
        searchManager.putItems(
            userInfos = listOf(userInfo.copy(isDone = isDone))
        )
        _state.update {
            it.copy(
                userInfoResult = it.userInfoResult.map { userInfoItem ->
                    if (userInfoItem.id == userInfo.id) {
                        userInfoItem.copy(isDone = isDone)
                    } else {
                        userInfoItem
                    }
                }
            )
        }
    }

    /**
     * Clears and releases resources associated with the search session.
     *
     * This callback is invoked when the ViewModel is about to be destroyed.
     * It ensures the AppSearch session is properly closed to prevent
     * resource leaks and maintain clean lifecycle management.
     */
    override fun onCleared() {
        super.onCleared()
        searchManager.closeSession()
    }

}