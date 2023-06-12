package com.example.mycomposeskeleton.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeskeleton.network.ApiServiceCocktailDb
import com.example.mycomposeskeleton.network.dto.Drink
import com.example.mycomposeskeleton.service.MySharedPref
import com.example.mycomposeskeleton.ui.cocktail.CocktailViewModel
import com.example.mycomposeskeleton.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiServiceCocktailDb: ApiServiceCocktailDb,
    private val sharedPref: MySharedPref
) : ViewModel() {

    enum class MainState {
        ERROR_CONNECTION,
        ERROR_SERVER
    }

    private val _messageSharedFlow = MutableSharedFlow<MainState>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _cocktailSummaryListStateFlow = MutableStateFlow(emptyList<Drink>())
    val cocktailSummaryListStateFlow = _cocktailSummaryListStateFlow.asStateFlow()


    private val _ordinarySummaryListStateFlow = MutableStateFlow(emptyList<Drink>())
    val ordinarySummaryListStateFlow = _ordinarySummaryListStateFlow.asStateFlow()

    private val _nonAlcoolicSummaryListStateFlow = MutableStateFlow(emptyList<Drink>())
    val nonAlcoolicSummaryListStateFlow = _nonAlcoolicSummaryListStateFlow.asStateFlow()

    private val _goToLoginSharedFlow = MutableSharedFlow<Screen>()
    val goToLoginSharedFlow = _goToLoginSharedFlow.asSharedFlow()

    private var currentState: MainState? = null


    fun fetchCocktailsAll() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiServiceCocktailDb.getAllCocktails()

                    val body = response?.body()

                    when {
                        response == null ->
                            currentState = MainState.ERROR_SERVER

                        response.isSuccessful && (body != null)-> {
                            _cocktailSummaryListStateFlow.value = body.drinks
                        }
                    }
                }
            } catch (e: IOException) {
                currentState = MainState.ERROR_CONNECTION
            }
            currentState?.let {state->
                _messageSharedFlow.emit(state)
            }
        }
    }

    fun fetchOrdinariesAll() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiServiceCocktailDb.getAllOrdinaryDrinks()

                    val body = response?.body()

                    when {
                        response == null ->
                            currentState = MainState.ERROR_SERVER

                        response.isSuccessful && (body != null)-> {
                                _ordinarySummaryListStateFlow.value = body.drinks
                        }
                    }
                }
            } catch (e: IOException) {
                currentState = MainState.ERROR_CONNECTION
            }
            currentState?.let {state->
                _messageSharedFlow.emit(state)
            }
        }
    }

    fun fetchNonAlcoolicsAll() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiServiceCocktailDb.getAllNonAlcoolicDrinks()

                    val body = response?.body()

                    when {
                        response == null ->
                            currentState = MainState.ERROR_SERVER

                        response.isSuccessful && (body != null)-> {
                            _nonAlcoolicSummaryListStateFlow.value = body.drinks
                        }
                    }
                }
            } catch (e: IOException) {
                currentState = MainState.ERROR_CONNECTION
            }
            currentState?.let {state->
                _messageSharedFlow.emit(state)
            }
        }
    }

    fun logout() {
        sharedPref.clearSharedPref()
        viewModelScope.launch {
            _goToLoginSharedFlow.emit(Screen.Login)
        }
    }
}