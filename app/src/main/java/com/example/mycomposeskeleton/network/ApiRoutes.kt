package com.example.mycomposeskeleton.network

object ApiRoutes {

    // COCKTAILS DB API
    const val BASE_URL_ITEM = "https://www.thecocktaildb.com/api/json/v1/1/"
    const val GET_ALL_DRINKS = "filter.php"
    const val GET_COCKTAIL_BY_ID = "lookup.php"

    // AUTH
    //const val BASE_URL = "http://10.0.2.2:8000"
    const val BASE_URL = "https://project-dev-id-backend.herokuapp.com"
    const val REGISTER = "/api/users"
    const val UPDATE = "/api/users/{userId}"
    const val LOGIN = "/api/login"

    // FAVORITE
    const val FAVORITE = "/api/favorites"
    const val FAVORITE_BY_ID = "/api/favorites/{favoriteId}"
}