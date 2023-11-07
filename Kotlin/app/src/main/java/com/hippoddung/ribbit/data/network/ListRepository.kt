package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.ListApiService
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val listApiService: ListApiService
) {
    suspend fun getLists() = listApiService.getLists()
}