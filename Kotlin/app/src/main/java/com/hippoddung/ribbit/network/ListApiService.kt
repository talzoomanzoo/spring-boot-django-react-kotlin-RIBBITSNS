package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.network.bodys.responsebody.DeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ListApiService {
    @GET("api/lists/")
    suspend fun getLists(
    ): List<RibbitListItem>

    @POST("api/lists/create")
    suspend fun postCreateList(
        @Body ribbitListItem: RibbitListItem
    ): RibbitListItem

    @GET("api/lists/{listId}")
    suspend fun getListIdPost(
        @Path("listId") listId : Int
    ): RibbitListItem

    @DELETE("api/lists/{listId}")
    suspend fun deleteListIdList(
        @Path("listId") listId : Int
    ): DeleteResponse

    @PUT("api/lists/update")
    suspend fun putEditList(
        @Body listItem: RibbitListItem
    ): RibbitListItem

    @POST("api/lists/{listId}/add1/{userId}")
    suspend fun postAddUser(
        @Path("listId") listId : Int,
        @Path("userId") userId : Int
    ): RibbitListItem
}