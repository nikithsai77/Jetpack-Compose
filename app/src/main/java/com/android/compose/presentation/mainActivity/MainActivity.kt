package com.android.compose.presentation.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.android.compose.SearchManager
import com.android.compose.presentation.BindAllUserInfoItems
import com.android.compose.ui.theme.LocalAppSearchTheme

/**
 * Main entry activity of the application that hosts the Compose UI content.
 *
 * This activity initializes the [MainViewModel] using a custom factory and
 * provides the required dependencies for handling search operations.
 * It also manages lifecycle-aware state collection and binds UI events
 * with ViewModel actions for updating search and user information states.
 */
class MainActivity : ComponentActivity() {
    /**
     * ViewModel instance used for managing UI state and user actions.
     *
     * The ViewModel is created using a custom [androidx.lifecycle.ViewModelProvider.Factory]
     * so that the required [com.android.compose.SearchManager] dependency can be injected
     * during initialization. This ensures proper separation of concerns
     * between UI rendering and business logic handling.
     */
    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory {
            initializer {
                MainViewModel(
                    searchManager = SearchManager(appContext = applicationContext)
                )
            }
        }
    }

    /**
     * Called when the activity is first created.
     *
     * This method sets up edge-to-edge support and initializes the Compose UI
     * content using the application theme. It observes the ViewModel state
     * in a lifecycle-aware manner and binds user interactions such as search
     * updates and completion status changes to the corresponding ViewModel methods.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /**
             * Applies the application theme to all Compose UI content.
             *
             * The theme ensures consistent styling, typography, and
             * color usage across the application screens while hosting
             * the main scaffold layout and user information content.
             */
            LocalAppSearchTheme {
                /**
                 * Base layout structure used for the screen content.
                 *
                 * Scaffold provides support for handling layout insets,
                 * padding values, and arranging the main UI components
                 * in a structured and material-compliant manner.
                 */
                Scaffold {
                    /**
                     * Collects the latest UI state from the ViewModel.
                     *
                     * The state is observed in a lifecycle-aware way
                     * using [collectAsStateWithLifecycle] to ensure
                     * updates are only received while the activity
                     * is in an active lifecycle state.
                     */
                    val state by mainViewModel.state.collectAsStateWithLifecycle()
                    /**
                     * Displays and binds all user information items.
                     *
                     * This composable renders the searchable list of
                     * user information and forwards user interactions
                     * such as search updates and completion changes
                     * back to the ViewModel for processing.
                     */
                    BindAllUserInfoItems(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(paddingValues = it),
                        search = state.searchQuery,
                        userInfoList = state.userInfoResult,
                        onSearchChange = mainViewModel::onSearchQueryChange,
                        onDoneChange = { userInfo, isDone ->
                            mainViewModel.onDoneChange(userInfo = userInfo, isDone = isDone)
                        }
                    )
                }
            }
        }
    }

}