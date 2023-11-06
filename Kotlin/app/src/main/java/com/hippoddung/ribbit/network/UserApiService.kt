package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("api/users/withdraw")
    suspend fun postWithdrawal(): User

    @GET("api/users/search1")
    suspend fun getUsersSearch(
        @Query("query") searchQuery : String    // @Query 안의 String값이 서버의 parameter 명과 같아야 함.
    ): List<User>

    @PUT("api/users/{userId}/follow")
    suspend fun putUserIdFollow(
        @Path("userId") userId: Int
    ): User
}