package com.example.cocktailboxcompose.network.dto


import com.squareup.moshi.Json

data class NewFavoriteDto(
    @Json(name = "userId")
    val userId: String,
    @Json(name = "idDrink")
    val idDrink: Long
)