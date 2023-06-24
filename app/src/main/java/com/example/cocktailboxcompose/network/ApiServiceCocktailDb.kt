package com.example.cocktailboxcompose.network

import com.example.cocktailboxcompose.network.dto.GetCocktailByIdDto
import com.example.cocktailboxcompose.network.dto.ResponseGetCocktailListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceCocktailDb {

    @GET(ApiRoutes.GET_ALL_DRINKS)
    suspend fun getAllCocktails(
        @Query("c") cat: String = "Cocktail"
    ): Response<ResponseGetCocktailListDto>?

    @GET(ApiRoutes.GET_ALL_DRINKS)
    suspend fun getAllOrdinaryDrinks(
        @Query("c") cat: String = "Ordinary_Drink"
    ): Response<ResponseGetCocktailListDto>?

    @GET(ApiRoutes.GET_ALL_DRINKS)
    suspend fun getAllNonAlcoolicDrinks(
        @Query("a") cat: String = "Non_Alcoholic"
    ): Response<ResponseGetCocktailListDto>?

    @GET(ApiRoutes.GET_ALL_DRINKS)
    suspend fun getDrinksByCategory(
        @Query("a") cat: String
    ): Response<ResponseGetCocktailListDto>?

    @GET(ApiRoutes.GET_COCKTAIL_BY_ID)
    suspend fun getDrinkById(
        @Query("i") id: Long
    ): Response<GetCocktailByIdDto>?

}