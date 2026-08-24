package me.gimmesomepeace.buywise.web.user.list

import me.gimmesomepeace.buywise.web.user.UserListItemResponse

data class ListUsersResponse(
    val users: List<UserListItemResponse>,
    val nextPageToken: String?,
)
