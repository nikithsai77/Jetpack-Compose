package com.android.compose.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.android.compose.data.UserInfo

/**
 * Displays a searchable list of user information items with checkbox support.
 *
 * This composable contains a search field and a lazy column to render
 * all user items efficiently. Users can filter the list using the search
 * input and update each item's completion state through the checkbox.
 *
 * @param modifier Modifier used to style and position the layout.
 * @param userInfoList List of user information items to display.
 * @param search Current search query text.
 * @param onSearchChange Callback triggered when search text changes.
 * @param onDoneChange Callback triggered when an item's checkbox state changes.
 */
@Composable
fun BindAllUserInfoItems(
    modifier: Modifier = Modifier,
    userInfoList: List<UserInfo>,
    search: String,
    onSearchChange: (String) -> Unit,
    onDoneChange: (UserInfo, Boolean) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        TextField(
            value = search,
            onValueChange = onSearchChange,
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f),
            contentPadding = PaddingValues(all = 8.dp)
        ) {
            items(
                items = userInfoList
            ) {
                BindUserInfo(userInfo = it) { isDone ->
                    onDoneChange(it, isDone)
                }
            }
        }
    }
}
