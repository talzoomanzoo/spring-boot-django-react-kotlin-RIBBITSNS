package com.hippoddung.ribbit.network.bodys


import com.google.gson.annotations.SerializedName

data class User(
    val backgroundImage: String,
    val bio: String,
    val birthDate: String,
    val email: String,
    val followed: Boolean,
    val followers: List<String>,
    val followings: List<String>,
    val fullName: String,
    val id: Int,
    val image: String,
    val location: String,
    @SerializedName("login_with_google")
    val loginWithGoogle: Boolean,
    val mobile: String,
    @SerializedName("req_user")
    val reqUser: Boolean,
    val verified: Boolean,
    val website: String
)