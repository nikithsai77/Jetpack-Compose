package com.android.compose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.compose.presentation.add_edit_note.components.AddEditNoteScreen
import com.android.compose.presentation.notes.components.NotesScreen
import com.android.compose.presentation.notes.components.NotesViewModel
import com.android.compose.presentation.ui.ComposeTheme
import com.android.compose.presentation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = Screen.NotesScreen
                ) {
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

                    composable<Screen.AddEditNoteScreen> {
                        AddEditNoteScreen {
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }

}
