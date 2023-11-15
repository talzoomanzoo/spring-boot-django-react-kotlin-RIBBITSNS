package com.hippoddung.ribbit.network.bodys

import com.google.gson.annotations.SerializedName

data class RibbitPost(
    val com: Boolean? = false,
    val community: RibbitCommuItem? = null,
    var content: String = "",
    val createdAt: String = "",
    var edited: Boolean = false,
    var editedAt: String? = null,
    val ethiclabel: Int? = null,
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

    val ethicrateMAX: Int? = null,
    @SerializedName("is_liked")
    val isLiked: Boolean? = null,
    @SerializedName("is_notification")
    val isNotification: Boolean? = null,
    @SerializedName("is_retwit")
    val isRetwit: Boolean? = null,
    val location: String? = null,
    val notifications: List<Notification?>? = null,
    val retwitAt: String? = null,
    val thumbnail: String? = null,
    val twit: Boolean? = null
)