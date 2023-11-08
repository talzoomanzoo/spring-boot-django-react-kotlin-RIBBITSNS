package com.hippoddung.ribbit.network.bodys

data class RibbitListItem(
    val backgroundImage: String?,
    val createdAt: String?,
    val description: String?,
    val followingsl: List<User?>?,
    val id: Int = 0,
    val listName: String?,
    val privateMode: Boolean?,
    val user: User?
)
