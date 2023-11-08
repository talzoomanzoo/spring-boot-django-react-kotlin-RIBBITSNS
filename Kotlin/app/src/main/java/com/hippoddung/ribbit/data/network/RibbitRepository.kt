package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.RibbitApiService
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import javax.inject.Inject

class RibbitRepository @Inject constructor(
    private val ribbitApiService: RibbitApiService
) {
    suspend fun getRibbitPosts() = ribbitApiService.getRibbitPosts()
    suspend fun getFollowingPosts() = ribbitApiService.getFollowingPosts()
    suspend fun getTopViewsRibbitPosts() = ribbitApiService.getTopViewsRibbitPosts()
    suspend fun getTopLikesRibbitPosts() = ribbitApiService.getTopLikesRibbitPosts()
    suspend fun postCreatePost(twitCreateRequest: TwitCreateRequest) = ribbitApiService.postCreatePost(twitCreateRequest)
    suspend fun postEditPost(post: RibbitPost) = ribbitApiService.postEditPost(post)
    suspend fun deletePost(postId: Int) = ribbitApiService.deletePost(postId)
    suspend fun getPostIdPost(postId: Int) = ribbitApiService.getPostIdPost(postId)
    suspend fun getUserIdPosts(userId: Int) = ribbitApiService.getUserIdPosts(userId)
    suspend fun getUserIdReplies(userId: Int) = ribbitApiService.getUserIdReplies(userId)
    suspend fun getUserIdLikes(userId: Int) = ribbitApiService.getUserIdLikes(userId)
    suspend fun postReply(replyRequest: ReplyRequest) = ribbitApiService.postReply(replyRequest)
    suspend fun postPostIdLike(postId: Int) = ribbitApiService.postPostIdLike(postId)
    suspend fun deletePostIdLike(postId: Int) = ribbitApiService.deletePostIdLike(postId)
    suspend fun putPostIdRepost(postId: Int) = ribbitApiService.putPostIdRepost(postId)
    suspend fun postPostIdCount(postId: Int) = ribbitApiService.postPostIdCount(postId)
    suspend fun getPostsSearch(searchQuery: String) = ribbitApiService.getPostsSearch(searchQuery)
}