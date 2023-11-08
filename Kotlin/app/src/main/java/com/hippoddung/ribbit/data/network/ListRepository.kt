package com.hippoddung.ribbit.data.network

import com.hippoddung.ribbit.network.ListApiService
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val listApiService: ListApiService
) {
    suspend fun getLists() = listApiService.getLists()
    suspend fun postCreateList(ribbitListItem: RibbitListItem) = listApiService.postCreateList(ribbitListItem)
    suspend fun getListIdPost(listId: Int) = listApiService.getListIdPost(listId)
    suspend fun deleteListIdList(listId: Int) = listApiService.deleteListIdList(listId)
    suspend fun postEditList(listItem: RibbitListItem) = listApiService.postEditList(listItem)
}