package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {
    @GET("api/users/profile")
    suspend fun getMyProfile(): User

    @GET("api/users/{userId}")
    suspend fun getProfile(
        @Path("userId") userId : Int
    ): User
}