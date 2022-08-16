package com.pro.testtask.di

import com.pro.testtask.remote.TestApi
import com.pro.testtask.repository.TaskRepository
import com.pro.testtask.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideVSearchRepository(
        api: TestApi
    ) = TaskRepository(api)

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideVSearchApi(
        httpClient: OkHttpClient
    ): TestApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()
            .create(TestApi::class.java)
    }
}