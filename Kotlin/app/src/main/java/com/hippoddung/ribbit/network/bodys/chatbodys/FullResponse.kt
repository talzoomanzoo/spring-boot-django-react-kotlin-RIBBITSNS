package com.hippoddung.ribbit.network.bodys.chatbodys

import kotlinx.serialization.Serializable

@Serializable
data class FullResponse(
    val headers: Map<String, String>,
    val body: MessageBody,
    val statusCode: String,
    val statusCodeValue: Int
)