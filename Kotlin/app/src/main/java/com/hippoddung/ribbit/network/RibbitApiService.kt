package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.ReplyRequest
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.responsebody.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL =
    "http://54.180.96.205:8080/"

interface RibbitApiService {
    @GET("api/twits/")
    suspend fun getPosts(): List<RibbitPost>

    @POST("api/twits/create")
    suspend fun postCreatePost(
        @Body twitCreateRequest: TwitCreateRequest
    ): RibbitPost

    @DELETE("api/twits/{twitId}")
    suspend fun deletePost(
        @Path("twitId") postId : Int
    ): DeleteResponse

    @GET("api/twits/{twitId}")
    suspend fun getPostIdPost(
        @Path("twitId") postId : Int
    ): RibbitPost

    @POST("api/twits/reply")
    suspend fun postReply(
        @Body replyRequest: ReplyRequest
    )
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