package com.hippoddung.ribbit.network.bodys

data class RibbitListItem(
    val backgroundImage: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val followingsl: List<User?>? = null,
    val id: Int = 0,
    val listName: String? = null,
    val privateMode: Boolean? = false,
    val user: User? = null
)
