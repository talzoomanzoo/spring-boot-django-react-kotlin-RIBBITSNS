package com.hippoddung.ribbit.data.local

data class LoginUiState(
    var jwt: String = "",
    val isLogin: Boolean =
        if (jwt.length != 0) true else false
)
