package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import com.hippoddung.ribbit.network.bodys.responsebody.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_IP =
    "3.36.249.200:8080"
//    "54.180.96.205:8080"
const val BASE_URL =
    "http://$BASE_IP/"
interface RibbitApiService {
    @GET("api/twits/")
    suspend fun getRibbitPosts(): List<RibbitPost>
    @GET("api/followtwit/")  // 서버에서 버그 해결을 위해 컨트롤러 주소를 바꾸었으나 나머지는 모두 문제가 없는 상태여서 같은 곳에서 service 를 실행
    suspend fun getFollowingPosts(): List<RibbitPost>

    @GET("api/twits/topviews")
    suspend fun getTopViewsRibbitPosts(): List<RibbitPost>
    @GET("api/twits/toplikes")
    suspend fun getTopLikesRibbitPosts(): List<RibbitPost>

    @POST("api/twits/create")
    suspend fun postCreatePost(
        @Body ribbitPost: RibbitPost
    ): RibbitPost
    @POST("api/ethic/reqsentence")
    suspend fun postCreatePostEthic(
        @Body ribbitPost: RibbitPost
    ): RibbitPost

    @POST("api/twits/edit")
    suspend fun postEditPost(
        @Body post: RibbitPost
    ): RibbitPost

    @DELETE("api/twits/{twitId}")
    suspend fun deletePost(
        @Path("twitId") postId : Int
    ): DeleteResponse

    @GET("api/twits/{twitId}")
    suspend fun getPostIdPost(
        @Path("twitId") postId : Int
    ): RibbitPost

    @GET("api/twits/user/{userId}")
    suspend fun getUserIdPosts(
        @Path("userId") userId : Int
    ): List<RibbitPost>
    @GET("api/twits/user/{userId}/replies")
    suspend fun getUserIdReplies(
        @Path("userId") userId : Int
    ): List<RibbitPost>
    @GET("api/twits/user/{userId}/likes")
    suspend fun getUserIdLikes(
        @Path("userId") userId : Int
    ): List<RibbitPost>

    @POST("api/twits/reply")
    suspend fun postReply(
        @Body replyRequest: ReplyRequest
    )

    @POST("api/{postId}/like")
    suspend fun postPostIdLike(
        @Path("postId") postId : Int
    )

//    @DELETE("api/{postId}/unlike")
//    suspend fun deletePostIdLike(
//        @Path("postId") postId : Int
//    ): RibbitPost

    @PUT("api/twits/{postId}/retwit")
    suspend fun putPostIdRepost(
        @Path("postId") postId : Int
    )

    @POST("api/twits/{postId}/count")
    suspend fun postPostIdCount(
        @Path("postId") postId : Int
    )

    @GET("api/twits/search2")
    suspend fun getPostsSearch(
        @Query("query") searchQuery : String    // @Query 안의 String 값이 서버의 parameter 명과 같아야 함.
    ): List<RibbitPost>

    @GET("/api/twits/{listId}/listTwit")
    suspend fun getListIdPosts(
        @Path("listId") listId : Int
    ): List<RibbitPost>

    @GET("api/twits/allComs")
    suspend fun getAllCommuPosts(): List<RibbitPost>
    @GET("/api/twits/{comId}/comTwit")
    suspend fun getCommuIdPosts(
        @Path("comId") commuId : Int
    ): List<RibbitPost>
    @POST("api/twits/{comId}/create")
    suspend fun postCreateCommuPost(
        @Path("comId") commuId : Int,
        @Body ribbitPost: RibbitPost
    ): RibbitPost
}

//    @GET("auth/refresh")
//    suspend fun refreshToken(
//        @Header("Authorization") token: String,
//    ): retrofit2.Response<LoginResponse>

//interface RibbitApiService {
//    @GET("api/twits/")
//    suspend fun getPosts(): List<RibbitPost>
//}
//
//object RibbitApi {
//    val retrofitService: RibbitApiService by lazy {
//        retrofit.create(RibbitApiService::class.java)
//    }
//}