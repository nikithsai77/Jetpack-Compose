package com.android.compose.presentation.mainActivity

import com.android.compose.data.UserInfo

/**
 * Represents the UI state for the user information list screen.
 *
 * @property searchQuery The current search text entered by the user.
 * @property userInfoResult The list of user information results matching the search query.
 */
data class UserInfoListState(
    val searchQuery: String = "",
    val userInfoResult: List<UserInfo> = emptyList()
)