package com.hippoddung.ribbit.network.bodys

import com.google.gson.annotations.SerializedName

data class SignUpRequestBody(
    val backgroundImage: String? = null,
    val bio: String? = null,
    val birthDate: String,
    val email: String,
    val fullName: String,
    val id: Int? = null,
    val image: String? = null,
    @SerializedName("is_req_user")
    val isReqUser: Boolean = true,
    val location: String? = null,
    @SerializedName("login_with_google")
    val loginWithGoogle: Boolean = true,
    val mobile: String? = null,
    val password: String,
    @SerializedName("req_user")
    val reqUser: Boolean = true,
    val verification: Verification,
    val website: String? = null
)