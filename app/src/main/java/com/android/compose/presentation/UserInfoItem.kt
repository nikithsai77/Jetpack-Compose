package com.android.compose.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.compose.data.UserInfo

/**
 * Displays user information with a title, description, and completion checkbox.
 *
 * This composable arranges the user details in a row layout and allows
 * toggling the completion state using a checkbox. The updated checkbox
 * state is returned through the [onDoneChange] callback.
 *
 * @param modifier Modifier used to style and position the layout.
 * @param userInfo Contains the user title, description, and checkbox state.
 * @param onDoneChange Callback triggered when the checkbox state changes.
 */
@Composable
fun BindUserInfo(
    modifier: Modifier = Modifier,
    userInfo: UserInfo,
    onDoneChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = userInfo.title,
                fontSize = 16.sp
            )
            Text(
                text = userInfo.description,
                fontSize = 10.sp
            )
        }
        Checkbox(
            checked = userInfo.isDone,
            onCheckedChange = onDoneChange
        )
    }
}
