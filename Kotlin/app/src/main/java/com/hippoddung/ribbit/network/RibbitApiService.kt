package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val BASE_URL =
    "http://54.180.124.128:8080/"

interface RibbitApiService {
    @GET("api/twits/")
    suspend fun getPosts(): Response<List<RibbitPost>>

    @POST("api/twits/create")
    suspend fun twitCreate(
        @Body twitCreateRequest: TwitCreateRequest
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