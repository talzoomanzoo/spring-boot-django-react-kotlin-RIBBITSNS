package com.hippoddung.ribbit.network.bodys.responsebody

data class ErrorResponse(
    val code: Int,
    val message: String
)