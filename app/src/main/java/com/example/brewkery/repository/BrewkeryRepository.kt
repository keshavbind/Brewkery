package com.example.brewkery.repository

import com.example.brewkery.data.api.RetrofitInstance
import com.example.brewkery.data.model.MenuResponse
import com.example.brewkery.data.model.Product

class BrewkeryRepository {

    private val api = RetrofitInstance.api

    suspend fun getMenu(): MenuResponse {
        return api.getMenu()
    }

    suspend fun getItem(id: Int): Product {
        return api.getItem(id)
    }
}