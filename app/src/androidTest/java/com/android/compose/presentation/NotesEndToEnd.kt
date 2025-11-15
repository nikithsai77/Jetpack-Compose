package com.android.compose.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.android.compose.core.TestTags
import com.android.compose.di.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEnd {
    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun saveNewNodeAndEditTheExisitingNode() {
        //Save New Node.
        composeRule.onNodeWithContentDescription(label = "Add Note").performClick()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput(text = "Test-Title")
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_TEXT_FIELD).performTextInput(text = "Test-Description")
        composeRule.onNodeWithContentDescription(label = "Save Note").performClick()
        composeRule.onNodeWithText(text = "Test-Title").assertIsDisplayed()
        composeRule.onNodeWithText(text = "Test-Description").assertIsDisplayed()

        //Edit Node.
        composeRule.onNodeWithText(text = "Test-Title").performClick()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("Test-Title")
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_TEXT_FIELD).assertTextEquals("Test-Description")
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextClearance()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput(text = "Test-Title1")
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_TEXT_FIELD).performTextReplacement(text = "Test-Description1")
        composeRule.onNodeWithContentDescription(label = "Save Note").performClick()
        composeRule.onNodeWithText(text = "Test-Title1").assertIsDisplayed()
        composeRule.onNodeWithText(text = "Test-Description1").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_OrderByTitleDescending() {
        for (i in 1..3) {
            composeRule.onNodeWithContentDescription(label = "Add Note").performClick()

            composeRule.onNodeWithTag(testTag = TestTags.TITLE_TEXT_FIELD).performTextInput(text = i.toString())
            composeRule.onNodeWithTag(testTag = TestTags.DESCRIPTION_TEXT_FIELD).performTextInput(text = i.toString())

            composeRule.onNodeWithContentDescription(label = "Save Note").performClick()

            composeRule.onNodeWithText(text = i.toString()).assertIsDisplayed()
            composeRule.onNodeWithText(text = i.toString()).assertIsDisplayed()
        }

        composeRule.onNodeWithText(text = "1").assertIsDisplayed()
        composeRule.onNodeWithText(text = "2").assertIsDisplayed()
        composeRule.onNodeWithText(text = "3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription(label = "Sort").performClick()

        composeRule.onNodeWithTag(testTag = TestTags.TITLE).performClick()
        composeRule.onNodeWithTag(testTag = TestTags.DESCENDING).performClick()

        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[0].assertTextEquals("3")
        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[1].assertTextEquals("2")
        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[2].assertTextEquals("1")

        composeRule.onNodeWithContentDescription(label = "Delete Note2").performClick()

        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[0].assertTextEquals("3")
        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[1].assertTextEquals("1")

        composeRule.onNodeWithText(text = "Note Deleted!").assertIsDisplayed()
        composeRule.onNodeWithText(text = "Undo").performClick()

        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[0].assertTextEquals("3")
        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[1].assertTextEquals("2")
        composeRule.onAllNodesWithTag(testTag = TestTags.NOTE_ITEM)[2].assertTextEquals("1")
    }

}
