package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,

    @SerializedName("category_id")
    val categoryId: String,

    val name: String,
    val tagline: String,
    val description: String,

    @SerializedName("base_price")
    val basePrice: Double,

    val rating: Double,

    @SerializedName("review_count")
    val reviewCount: Int,

    @SerializedName("prep_time")
    val prepTime: String,

    val calories: Int,

    @SerializedName("image_url")
    val imageUrl: String,

    val badge: String?,

    val ingredients: List<String>,

    val customizations: Customizations
)
