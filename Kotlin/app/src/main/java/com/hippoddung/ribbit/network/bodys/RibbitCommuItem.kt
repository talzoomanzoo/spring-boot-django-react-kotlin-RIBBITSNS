package com.hippoddung.ribbit.network.bodys


data class RibbitCommuItem(
    var backgroundImage: String? = null,
    var comName: String? = null,
    val comTwits: List<RibbitPost>? = null,
    var description: String? = null,
    val followingsc: List<User?> = listOf(),
    val followingscReady: List<User> = listOf(),
    val id: Int? = null,
    var privateMode: Boolean? = null,
    val user: User? = null
)