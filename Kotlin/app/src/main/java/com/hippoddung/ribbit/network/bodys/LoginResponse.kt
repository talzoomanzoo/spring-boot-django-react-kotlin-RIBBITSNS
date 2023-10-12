package com.hippoddung.ribbit.network.bodys

data class LoginResponse (
    val jwt: String,
    val status: Boolean
)