package com.hippoddung.ribbit.network.bodys

data class RibbitCommuItem(
    var backgroundImage: String? = null,
    var comName: String? = null,
    val comTwits: List<RibbitPost?>? = null,
    val createdAt: String? = null,
    var description: String? = null,
    val id: Int? = null,
    var privateMode: Boolean? = null,
    val user: User? = null
)