package com.facts.factsbyshahzaib.activities.main

import com.facts.factsbyshahzaib.ApiService
import com.facts.factsbyshahzaib.model.FactsResponse
import com.facts.factsbyshahzaib.model.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService)
{
    suspend fun loadFacts(): Resource<FactsResponse> {
        return try
        {
            Resource.Success(apiService.getFacts())
        }
        catch (e:java.lang.Exception)
        {
            if (e is IOException) {
                return Resource.Error("No internet connection")
            }
            else
            if (e is HttpException) {
                val statusCode = e.code()
                if (statusCode in 400..499) {
                    return Resource.Error("Client-side error")
                } else if (statusCode in 500..599) {
                    return Resource.Error("Server-side error")
                }
            }
            throw  return Resource.Error("Unknown error")
        }
    }
}