package com.pro.testtask.remote

import com.pro.testtask.remote.data.ItemIndexesRemote
import com.pro.testtask.remote.data.ItemRemote
import retrofit2.http.GET
import retrofit2.http.Path

interface TestApi {

    @GET("api/v1/hot")
    suspend fun getIndexes() : ItemIndexesRemote

    @GET("api/v1/post/{id}")
    suspend fun getItem(
        @Path("id") itemId: Int
    ): ItemRemote

}