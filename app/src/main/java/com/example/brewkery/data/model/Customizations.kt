package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class Customizations(
    val sizes: List<SizeOption>,

    @SerializedName("sugar_levels")
    val sugarLevels: List<String>,

    @SerializedName("milk_options")
    val milkOptions: List<MilkOption>
)
data class SizeOption(
    val id: String,
    val label: String,

    @SerializedName("extra_price")
    val extraPrice: Double
)

data class MilkOption(
    val id: String,
    val name: String,

    @SerializedName("extra_price")
    val extraPrice: Double
)
