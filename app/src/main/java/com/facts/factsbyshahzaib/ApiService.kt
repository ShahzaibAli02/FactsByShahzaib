package com.facts.factsbyshahzaib

import com.facts.factsbyshahzaib.model.FactsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("facts")
    suspend fun getFacts(): FactsResponse
}
