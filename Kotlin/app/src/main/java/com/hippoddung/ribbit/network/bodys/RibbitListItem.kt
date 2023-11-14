package com.hippoddung.ribbit.network.bodys

data class RibbitListItem(
    var backgroundImage: String? = null,
    val createdAt: String? = null,
    var description: String? = null,
    val followingsl: List<User?>? = null,
    val id: Int = 0,
    var listName: String? = null,
    var privateMode: Boolean? = false,
    val user: User? = null
)