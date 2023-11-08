package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.network.bodys.RibbitPost
import com.hippoddung.ribbit.network.bodys.requestbody.TwitCreateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ListApiService {
    @GET("api/lists/")
    suspend fun getLists(
    ): List<RibbitListItem>

    @POST("api/lists/create")
    suspend fun postCreateList(
        @Body ribbitListItem: RibbitListItem
    ): RibbitListItem

    @GET("api/twits/{listId}")
    suspend fun getListIdPost(
        @Path("listId") listId : Int
    ): RibbitListItem
}