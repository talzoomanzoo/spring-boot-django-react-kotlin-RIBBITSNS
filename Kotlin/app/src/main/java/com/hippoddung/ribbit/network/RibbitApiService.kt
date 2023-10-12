package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.data.local.TokenManager
import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.LoginResponse
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.SignUpRequestBody
import com.hippoddung.ribbit.network.bodys.SignUpResponseBody
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject

const val BASE_URL =
    "http://15.165.203.239:8080/"

interface RibbitApiService {
    @POST("twits/")
    suspend fun getPosts(): List<RibbitPost>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): retrofit2.Response<LoginResponse>
}

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