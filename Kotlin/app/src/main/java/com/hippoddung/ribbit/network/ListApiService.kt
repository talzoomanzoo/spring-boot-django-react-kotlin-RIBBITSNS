package com.hippoddung.ribbit.network

import com.hippoddung.ribbit.network.bodys.RibbitListItem
import retrofit2.http.GET

interface ListApiService {
    @GET("api/lists/")
    suspend fun getLists(
    ): List<RibbitListItem>
}