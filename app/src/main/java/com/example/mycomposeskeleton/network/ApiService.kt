package com.example.mycomposeskeleton.network

import com.example.mycomposeskeleton.network.ApiRoutes
import com.example.mycomposeskeleton.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST(ApiRoutes.REGISTER)
    suspend fun register(@Body registerInfo: RegisterDto): Response<UserDto>?

    @Headers("Content-Type: application/json")
    @POST(ApiRoutes.LOGIN)
    suspend fun login(@Body loginInfo: LoginDto): Response<SessionDto>?

    @Headers("Content-Type: application/merge-patch+json")
    @PATCH(ApiRoutes.UPDATE)
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body updateInfo: UpdateDto
    ): Response<UserDto>?

    @Headers("Content-Type: application/json")
    @GET(ApiRoutes.FAVORITE)
    suspend fun getFavoritesUserList(
        @Query("userId") userId: Long,
        @HeaderMap headers: Map<String, String>,
    ): Response<GetFavoritesDto>?

    @Headers("Content-Type: application/json")
    @POST(ApiRoutes.FAVORITE)
    suspend fun newFavorite(
        @Body newFavorite: NewFavoriteDto,
        @HeaderMap headers: Map<String, String>,
    ): Response<GetFavoritesDto>?

    @Headers("Content-Type: application/json")
    @DELETE(ApiRoutes.FAVORITE_BY_ID)
    suspend fun deleteFavorite(
        @Path("favoriteId") favoriteId: Int,
        @HeaderMap headers: Map<String, String>,
    ): Response<GetFavoritesDto>?
}
