package com.example.brewkery.data.api

import com.example.brewkery.data.model.MenuResponse
import com.example.brewkery.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApi {

    @GET("data.json")
    suspend fun getMenu(): MenuResponse

    @GET("api/items/{id}.json")
    suspend fun getItem(
        @Path("id") id: Int
    ): Product
}