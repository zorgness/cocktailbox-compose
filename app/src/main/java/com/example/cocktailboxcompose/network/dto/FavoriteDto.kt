package com.example.cocktailboxcompose.network.dto


import com.squareup.moshi.Json

data class FavoriteDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "idDrink")
    val idDrink: Int,
    @Json(name = "userId")
    val userId: String
)


data class GetFavoritesDto(
    @Json(name = "hydra:member")
    val favorites: List<FavoriteDto> = listOf()
)