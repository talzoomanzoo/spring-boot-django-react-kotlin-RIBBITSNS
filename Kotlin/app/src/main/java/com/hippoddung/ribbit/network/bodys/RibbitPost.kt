package com.hippoddung.ribbit.network.bodys

import com.google.gson.annotations.SerializedName

data class RibbitPost(
    var content: String = "",
    val createdAt: String = "",
    var edited: Boolean = false,
    var editedAt: String? = null,
    val ethicrate: String? = null,
    val id: Int = 0,
    var image: String? = null,
    val label: Int? = null,
    val liked: Boolean = false,
    val reply: Boolean = false,
    val replyTwits: List<RibbitPost?>? = null,   // listOf(): 비어있는 리스트를 리턴
    val retwit: Boolean = false,
    val retwitUsersId: List<Int?> = listOf(),
    val sentence: String? = null,
    val totalLikes: Int = 0,
    val totalReplies: Int = 0,
    val totalRetweets: Int = 0,
    val user: User? = null,
    var video: String? = null,
    val viewCount: Int = 0,

    val com: Boolean?,
    val community: RibbitCommuItem2.ComTwit.Community?,
    val ethiclabel: Int?,
    val ethicrateMAX: Int?,
    @SerializedName("is_liked")
    val isLiked: Boolean?,
    @SerializedName("is_notification")
    val isNotification: Boolean?,
    @SerializedName("is_retwit")
    val isRetwit: Boolean?,
    val likes: List<RibbitCommuItem2.ComTwit.Like?>?,
    val location: String?,
    val replyFor: String?,
    val retwitAt: String?,
    val retwitUser: List<RibbitCommuItem2.ComTwit.RetwitUser?>?,
    val thumbnail: String?,
    val twit: Boolean?,
)