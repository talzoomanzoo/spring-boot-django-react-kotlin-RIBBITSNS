package com.hippoddung.ribbit.network.bodys


import com.google.gson.annotations.SerializedName

data class RibbitCommuItem2(
    val backgroundImage: String?,
    val comName: String?,
    val comTwits: List<RibbitPost?>?,
    val description: String?,
    val followingsc: List<User?>?,
    val followingscReady: List<User?>?,
    val id: Int?,
    val privateMode: Boolean?,
    val user: User?
) {
    data class ComTwit(
        val com: Boolean?,
        val community: Community?,
        val content: String?,
        val createdAt: String?,
        val edited: Boolean?,
        val editedAt: String?,
        val ethiclabel: Int?,
        val ethicrate: String?,
        val ethicrateMAX: Int?,
        val id: Int?,
        val image: String?,
        @SerializedName("is_liked")
        val isLiked: Boolean?,
        @SerializedName("is_notification")
        val isNotification: Boolean?,
        @SerializedName("is_retwit")
        val isRetwit: Boolean?,
        val likes: List<Like?>?,
        val location: String?,
        val reply: Boolean?,
        val replyFor: String?,
        val replyTwits: List<String?>?,
        val retwitAt: String?,
        val retwitUser: List<RetwitUser?>?,
        val thumbnail: String?,
        val twit: Boolean?,
        val user: User?,
        val video: String?,
        val viewCount: Int?
    ) {
        data class Community(
            val backgroundImage: String?,
            val comName: String?,
            val comTwits: List<String?>?,
            val createdAt: String?,
            val description: String?,
            val id: Int?,
            val privateMode: Boolean?,
            val user: User?
        ) {
            data class User(
                val backgroundImage: String?,
                val bio: String?,
                val birthDate: String?,
                val education: String?,
                val email: String?,
                val fullName: String?,
                val id: Int?,
                val image: String?,
                @SerializedName("is_req_user")
                val isReqUser: Boolean?,
                val joinedAt: String?,
                val location: String?,
                @SerializedName("login_with_google")
                val loginWithGoogle: Boolean?,
                val mobile: String?,
                val password: String?,
                @SerializedName("req_user")
                val reqUser: Boolean?,
                val verification: Verification?,
                val website: String?
            ) {
                data class Verification(
                    val endsAt: String?,
                    val planType: String?,
                    val startedAt: String?,
                    val status: Boolean?
                )
            }
        }

        data class Like(
            val community: Community?,
            val id: Int?,
            val twit: String?,
            val user: User?
        ) {
            data class Community(
                val backgroundImage: String?,
                val comName: String?,
                val comTwits: List<String?>?,
                val createdAt: String?,
                val description: String?,
                val id: Int?,
                val privateMode: Boolean?,
                val user: User?
            ) {
                data class User(
                    val backgroundImage: String?,
                    val bio: String?,
                    val birthDate: String?,
                    val education: String?,
                    val email: String?,
                    val fullName: String?,
                    val id: Int?,
                    val image: String?,
                    @SerializedName("is_req_user")
                    val isReqUser: Boolean?,
                    val joinedAt: String?,
                    val location: String?,
                    @SerializedName("login_with_google")
                    val loginWithGoogle: Boolean?,
                    val mobile: String?,
                    val password: String?,
                    @SerializedName("req_user")
                    val reqUser: Boolean?,
                    val verification: Verification?,
                    val website: String?
                ) {
                    data class Verification(
                        val endsAt: String?,
                        val planType: String?,
                        val startedAt: String?,
                        val status: Boolean?
                    )
                }
            }

            data class User(
                val backgroundImage: String?,
                val bio: String?,
                val birthDate: String?,
                val education: String?,
                val email: String?,
                val fullName: String?,
                val id: Int?,
                val image: String?,
                @SerializedName("is_req_user")
                val isReqUser: Boolean?,
                val joinedAt: String?,
                val location: String?,
                @SerializedName("login_with_google")
                val loginWithGoogle: Boolean?,
                val mobile: String?,
                val password: String?,
                @SerializedName("req_user")
                val reqUser: Boolean?,
                val verification: Verification?,
                val website: String?
            ) {
                data class Verification(
                    val endsAt: String?,
                    val planType: String?,
                    val startedAt: String?,
                    val status: Boolean?
                )
            }
        }

        data class RetwitUser(
            val backgroundImage: String?,
            val bio: String?,
            val birthDate: String?,
            val education: String?,
            val email: String?,
            val fullName: String?,
            val id: Int?,
            val image: String?,
            @SerializedName("is_req_user")
            val isReqUser: Boolean?,
            val joinedAt: String?,
            val location: String?,
            @SerializedName("login_with_google")
            val loginWithGoogle: Boolean?,
            val mobile: String?,
            val password: String?,
            @SerializedName("req_user")
            val reqUser: Boolean?,
            val verification: Verification?,
            val website: String?
        ) {
            data class Verification(
                val endsAt: String?,
                val planType: String?,
                val startedAt: String?,
                val status: Boolean?
            )
        }

        data class User(
            val backgroundImage: String?,
            val bio: String?,
            val birthDate: String?,
            val education: String?,
            val email: String?,
            val fullName: String?,
            val id: Int?,
            val image: String?,
            @SerializedName("is_req_user")
            val isReqUser: Boolean?,
            val joinedAt: String?,
            val location: String?,
            @SerializedName("login_with_google")
            val loginWithGoogle: Boolean?,
            val mobile: String?,
            val password: String?,
            @SerializedName("req_user")
            val reqUser: Boolean?,
            val verification: Verification?,
            val website: String?
        ) {
            data class Verification(
                val endsAt: String?,
                val planType: String?,
                val startedAt: String?,
                val status: Boolean?
            )
        }
    }

    data class Followingsc(
        val backgroundImage: String?,
        val bio: String?,
        val birthDate: String?,
        val education: String?,
        val email: String?,
        val fullName: String?,
        val id: Int?,
        val image: String?,
        @SerializedName("is_req_user")
        val isReqUser: Boolean?,
        val joinedAt: String?,
        val location: String?,
        @SerializedName("login_with_google")
        val loginWithGoogle: Boolean?,
        val mobile: String?,
        val password: String?,
        @SerializedName("req_user")
        val reqUser: Boolean?,
        val verification: Verification?,
        val website: String?
    ) {
        data class Verification(
            val endsAt: String?,
            val planType: String?,
            val startedAt: String?,
            val status: Boolean?
        )
    }

    data class FollowingscReady(
        val backgroundImage: String?,
        val bio: String?,
        val birthDate: String?,
        val education: String?,
        val email: String?,
        val fullName: String?,
        val id: Int?,
        val image: String?,
        @SerializedName("is_req_user")
        val isReqUser: Boolean?,
        val joinedAt: String?,
        val location: String?,
        @SerializedName("login_with_google")
        val loginWithGoogle: Boolean?,
        val mobile: String?,
        val password: String?,
        @SerializedName("req_user")
        val reqUser: Boolean?,
        val verification: Verification?,
        val website: String?
    ) {
        data class Verification(
            val endsAt: String?,
            val planType: String?,
            val startedAt: String?,
            val status: Boolean?
        )
    }

    data class User(
        val backgroundImage: String?,
        val bio: String?,
        val birthDate: String?,
        val education: String?,
        val email: String?,
        val followed: Boolean?,
        val followers: List<String?>?,
        val followings: List<String?>?,
        val fullName: String?,
        val hasFollowedLists: Boolean?,
        val id: Int?,
        val image: String?,
        val joinedAt: String?,
        val location: String?,
        @SerializedName("login_with_google")
        val loginWithGoogle: Boolean?,
        val mobile: String?,
        @SerializedName("req_user")
        val reqUser: Boolean?,
        val verified: Boolean?,
        val website: String?
    )
}