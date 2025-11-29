package com.android.compose.presentation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.compose.core.TestTags
import com.android.compose.di.AppModule
import com.android.compose.domain.repository.NoteRepository
import com.android.compose.presentation.notes.components.NotesScreen
import com.android.compose.presentation.notes.components.NotesViewModel
import com.android.compose.presentation.ui.ComposeTheme
import com.android.compose.presentation.util.Screen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject lateinit var repository: NoteRepository

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        //is used to tell the hilt to inject the dependencies on this test class as well on provided activity instance.
        hiltRule.inject()
        //activityRule is the rule to launch the specified activity.
        //scenario is the Activity scenario instance is used to control the currently running activity instance.
        //onActivity is used to execute the block of code on UI thread of running activity
        //Use case is when you want to override the setContent & call its functions
        composeRule.activityRule.scenario.onActivity { activity ->
            //activity is the running activity instance.
            activity.setContent {
                ComposeTheme {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.NotesScreen) {
                        composable<Screen.NotesScreen> {
                            val viewModel: NotesViewModel = hiltViewModel()
                            val state by viewModel.state
                            NotesScreen(
                                state = state,
                                onEvent = {
                                    viewModel.onEvent(notesEvent = it)
                                }
                            ) { noteId ->
                                navController.navigate(
                                    route = Screen.AddEditNoteScreen(
                                        noteId = noteId
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun clickToggleOrderSection_isVisible() {
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertIsNotDisplayed()
        composeRule.onNodeWithContentDescription(label = "Sort").performClick()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertIsDisplayed()
    }

}
