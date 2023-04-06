package com.facts.factsbyshahzaib.di

import com.facts.factsbyshahzaib.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules
{

    private val BASE_URL = "https://cat-fact.herokuapp.com/"
    @Provides
    fun provideApiService(): ApiService
    {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

}