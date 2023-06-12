package com.example.mycomposeskeleton.ui.favorite

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycomposeskeleton.network.ApiService
import com.example.mycomposeskeleton.network.dto.FavoriteDto
import com.example.mycomposeskeleton.network.dto.NewFavoriteDto
import com.example.mycomposeskeleton.service.MySharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import toHydraUserId
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPref: MySharedPref
): ViewModel() {

    enum class FavoriteState {
        ERROR_SERVER,
        ERROR_CONNECTION
    }

    private val _favoriteUserList = MutableStateFlow(emptyList<FavoriteDto>())
    val favoriteUserList = _favoriteUserList.asStateFlow()

    private val headers = HashMap<String, String>()
    private var currentState: FavoriteState? = null

    init {
        fetchFavoritesUserList()
    }

    fun checkIsFavorite(idDrink: Long): Boolean {

        val idList = _favoriteUserList.value.map { it.idDrink.toLong()}


        return idList.contains(idDrink)
    }

    private fun getFavoriteId(idDrink: Long): FavoriteDto {
        val favorite = _favoriteUserList.value.filter {
            it.idDrink.toLong() == idDrink
        }
        return favorite[0]
    }

    fun setFavorite(idDrink: Long, onCallback: (Boolean) -> Unit ){
        headers["Authorization"] = "bearer ${sharedPref.token}"

        if(checkIsFavorite(idDrink)) {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        val response = apiService.deleteFavorite(
                            favoriteId = getFavoriteId(idDrink).id,
                            headers = headers
                        )

                        when {
                            response == null ->
                                currentState = FavoriteState.ERROR_SERVER

                            response.isSuccessful -> {
                                fetchFavoritesUserList()
                                onCallback(true)
                            }
                        }
                    }

                } catch (e: Exception) {
                    currentState = FavoriteState.ERROR_CONNECTION
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        val response = apiService.newFavorite(
                            NewFavoriteDto(
                                idDrink = idDrink,
                                userId = sharedPref.userId?.toHydraUserId() ?: ""
                            ),
                            headers = headers
                        )

                        when {
                            response == null ->
                                currentState = FavoriteState.ERROR_SERVER

                            response.isSuccessful -> {
                                fetchFavoritesUserList()
                                onCallback(true)
                            }
                        }
                    }
                } catch(e: Exception) {
                    currentState = FavoriteState.ERROR_CONNECTION
                }
            }
        }
    }

    private fun fetchFavoritesUserList() {
        headers["Authorization"] = "bearer ${sharedPref.token}"
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiService.getFavoritesUserList(
                        userId = sharedPref.userId ?: 0L,
                        headers = headers
                    )

                    val body = response?.body()

                    when {
                        response == null ->
                           currentState = FavoriteState.ERROR_SERVER

                        response.isSuccessful && (body != null) -> {
                            _favoriteUserList.value = body.favorites
                        }
                    }
                }
            } catch (e: Exception) {
                currentState = FavoriteState.ERROR_CONNECTION
            }
        }
    }
}