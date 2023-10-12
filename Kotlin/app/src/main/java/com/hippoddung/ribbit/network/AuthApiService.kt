package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.Auth
import com.hippoddung.ribbit.network.bodys.LoginResponse
import com.hippoddung.ribbit.network.bodys.SignUpRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signin")
    suspend fun login(
        @Body auth: Auth
    ): Response<LoginResponse>

    @POST("auth/signup")
    suspend fun signUp(
        @Body auth: SignUpRequestBody
    ): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>
}