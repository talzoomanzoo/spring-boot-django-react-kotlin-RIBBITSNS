package com.hippoddung.ribbit.network.bodys


import com.google.gson.annotations.SerializedName

data class User(
    val backgroundImage: String?,
    val bio: String?,
    val birthDate: String?,
    val education: String?,
    val email: String,
    val followed: Boolean =false,
    val followers: List<String?>?,
    val followings: List<String?>?,
    val fullName: String?,
    val hasFollowedLists: Boolean = false,
    val id: Int?,
    val image: String?,
    val joinedAt: String?,
    val location: String?,
    @SerializedName("login_with_google")
    val loginWithGoogle: Boolean = false,
    val mobile: String?,
    @SerializedName("req_user")
    val reqUser: Boolean = false,
    val verified: Boolean = false,
    val website: String?
)