package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.TwitCreateRequest
import com.hippoddung.ribbit.network.bodys.TwitCreateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val BASE_URL =
    "http://3.35.24.214:8080/"

interface RibbitApiService {
    @GET("api/twits/")
    suspend fun getPosts(): List<RibbitPost>


    @POST("api/twits/create")
    suspend fun twitCreate(
        @Body twitCreateRequest: TwitCreateRequest
    ): TwitCreateResponse
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