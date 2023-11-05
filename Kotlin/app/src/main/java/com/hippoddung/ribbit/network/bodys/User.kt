package com.hippoddung.ribbit.network.bodys


import com.google.gson.annotations.SerializedName

data class User(
    val backgroundImage: String? = null,
    val bio: String? = null,
    val birthDate: String? = null,
    val education: String? = null,
    val email: String,
    val followed: Boolean =false,
    val followers: List<String?>? = null,
    val followings: List<String?>? = null,
    val fullName: String? = null,
    val hasFollowedLists: Boolean = false,
    val id: Int? = null,
    val image: String? = null,
    val joinedAt: String? = null,
    val location: String? = null,
    @SerializedName("login_with_google")
    val loginWithGoogle: Boolean = false,
    val mobile: String? = null,
    @SerializedName("req_user")
    val reqUser: Boolean = false,
    val verified: Boolean = false,
    val website: String? = null
)