package com.android.compose.presentation.add_edit_note.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.compose.core.TestTags
import com.android.compose.domain.model.Note
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navigateBack: () -> Unit
) {
    val viewModel: AddEditNoteViewModel = hiltViewModel()

    val titleState by viewModel.noteTitle
    val contentState by viewModel.noteContent
    val noteColorState by viewModel.noteColor

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val noteColorAnimate = remember(key1 = noteColorState) {
        Animatable(
            initialValue = Color(color = noteColorState)
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.DisplaySnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                }

                is UiEvent.SaveNote -> navigateBack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(event = AddEditNoteEvent.SaveNote)
                }
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = noteColorAnimate.value)
                .padding(paddingValues = innerPadding)
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(size = 50.dp)
                            .shadow(elevation = 15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (noteColorState == colorInt) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteColorAnimate.animateTo(
                                        targetValue = Color(color = colorInt),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                    viewModel.onEvent(event = AddEditNoteEvent.ChangeColor(color = colorInt))
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(height = 16.dp))

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.EnteredTitle(text = it))
                },
                onFocusChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.ChangeTitleFocus(focusState = it))
                },
                singleLine = true,
                testTag = TestTags.TITLE_TEXT_FIELD,
                isHintVisible = titleState.isHintVisible,
                textStyle = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.EnterDescription(text = it))
                },
                onFocusChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.ChangeDescriptionFocus(focusState = it))
                },
                singleLine = true,
                modifier = Modifier.fillMaxHeight(),
                testTag = TestTags.DESCRIPTION_TEXT_FIELD,
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.headlineSmall
            )
        }
    }

}
