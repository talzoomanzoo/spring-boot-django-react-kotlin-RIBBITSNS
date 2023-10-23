package com.hippoddung.ribbit.network.bodys.responsebody

data class AuthResponse (
    val jwt: String,
    val status: Boolean
)