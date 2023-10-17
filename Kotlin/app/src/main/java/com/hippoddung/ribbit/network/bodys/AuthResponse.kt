package com.hippoddung.ribbit.network.bodys

data class AuthResponse (
    val jwt: String,
    val status: Boolean
)