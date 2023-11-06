package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    @GET("api/users/profile")
    suspend fun getMyProfile(
    ): User

    @GET("api/users/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: Int
    ): User

    @PUT("api/users/update")
    suspend fun putEditingProfile(
        @Body user: User
    ): User

    @PUT("api/users/{userId}/follow")
    suspend fun putUserIdFollow(
        @Path("userId") userId: Int
    ): User
}