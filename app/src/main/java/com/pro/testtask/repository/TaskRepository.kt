package com.pro.testtask.repository

import com.pro.testtask.remote.TestApi
import com.pro.testtask.remote.data.ItemIndexesRemote
import com.pro.testtask.remote.data.ItemRemote
import com.pro.testtask.util.Resource
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val api: TestApi
) {

    suspend fun getIndexes(): Resource<ItemIndexesRemote> {
        val response = try {
            api.getIndexes()
        } catch (e: Exception) {
            val textErr = if(e.message.isNullOrBlank()) {"An unknown error occurred."} else e.message!!
            return Resource.Error(textErr)
        }
        return Resource.Success(response)
    }

    suspend fun getItem(itemId: Int): Resource<ItemRemote> {
        val response = try {
            api.getItem(itemId)
        } catch (e: Exception){

            val textErr = if(e.message.isNullOrBlank()) {"An unknown error occurred."} else e.message!!
            return Resource.Error(textErr)
        }
        return Resource.Success(response)
    }
}