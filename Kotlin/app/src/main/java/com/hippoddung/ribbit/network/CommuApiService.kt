package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitCommuItem
import com.hippoddung.ribbit.network.bodys.responsebody.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommuApiService {
    @GET("api/communities/")
    suspend fun getCommus(
    ): List<RibbitCommuItem>

    @POST("api/communities/create")
    suspend fun postCreateCommu(
        @Body ribbitCommuItem: RibbitCommuItem
    ): RibbitCommuItem

    @GET("api/communities/{comId}")
    suspend fun getCommuIdPost(
        @Path("comId") commuId : Int
    ): RibbitCommuItem

    @DELETE("api/communities/{comId}")
    suspend fun deleteCommuIdCommu(
        @Path("comId") commuId : Int
    ): DeleteResponse

    @POST("api/communities/update")
    suspend fun postEditCommu(
        @Body commuItem: RibbitCommuItem
    ): RibbitCommuItem

    @POST("api/communities/{comId}/add2/{userId}")
    suspend fun postAddUser(
        @Path("comId") commuId : Int,
        @Path("userId") userId : Int
    ): RibbitCommuItem

    @POST("api/communities/{comId}/signup")
    suspend fun signupCommu(
        @Path("comId") commuId : Int
    ): RibbitCommuItem

    @POST("api/communities/{comId}/signupok/{userId}")
    suspend fun postSignupOk(
        @Path("comId") commuId : Int,
        @Path("userId") userId : Int
    ): RibbitCommuItem

    @POST("api/communities/{comId}/signout")
    suspend fun postSignoutCommuId(
        @Path("comId") commuId : Int
    ): RibbitCommuItem
}