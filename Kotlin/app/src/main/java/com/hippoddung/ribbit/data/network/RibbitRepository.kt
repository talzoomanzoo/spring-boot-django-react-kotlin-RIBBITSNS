package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import javax.inject.Inject

class RibbitRepository @Inject constructor(
    private val ribbitApiService: RibbitApiService
) {
    suspend fun getPosts() = ribbitApiService.getPosts()
    suspend fun postCreatePost(twitCreateRequest: TwitCreateRequest) {
        ribbitApiService.postCreatePost(twitCreateRequest)
    }
    suspend fun deletePost(postId: Int) = ribbitApiService.deletePost(postId)
    suspend fun getPostIdPost(postId: Int) = ribbitApiService.getPostIdPost(postId)

    suspend fun postReply(replyRequest: ReplyRequest) {
        ribbitApiService.postReply(replyRequest)
    }
}