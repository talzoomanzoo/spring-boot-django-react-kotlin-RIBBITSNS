package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import javax.inject.Inject

class RibbitRepository @Inject constructor(
    private val ribbitApiService: RibbitApiService
) {
    suspend fun getPosts() = ribbitApiService.getPosts()
    suspend fun twitCreate(twitCreateRequest: TwitCreateRequest) {
        ribbitApiService.twitCreate(twitCreateRequest)
    }
    suspend fun deletePost(postId: Int) = ribbitApiService.deletePost(postId)
    suspend fun getPostIdPosts(postId: Int) = ribbitApiService.getPostIdPosts(postId)
}