package com.example.cocktailboxcompose.ui.non_alcoolic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailboxcompose.network.ApiServiceCocktailDb
import com.example.cocktailboxcompose.network.dto.Drink
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
class NonAlcoolicViewModel @Inject constructor(
    private val apiServiceCocktailDb: ApiServiceCocktailDb,

    ): ViewModel() {


    enum class NonAlcoolicState {
        ERROR_CONNECTION,
        ERROR_SERVER
    }

    private val _messageSharedFlow = MutableSharedFlow<NonAlcoolicState>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _isPbVisibleStateFlow = MutableStateFlow(true)
    val isPbVisibleStateFlow = _isPbVisibleStateFlow.asStateFlow()

    private val _searchStrStateFlow = MutableStateFlow("")
    val searchStrStateFlow= _searchStrStateFlow.asStateFlow()

    private val _nonAlcoolicSummaryListStateFlow = MutableStateFlow(emptyList<Drink>())
    val nonAlcoolicSummaryListStateFlow = _nonAlcoolicSummaryListStateFlow.asStateFlow()

    private val _isListReadySharedFlow = MutableSharedFlow<Boolean>()
    val isListReadySharedFlow = _isListReadySharedFlow.asSharedFlow()

    private var currentState: NonAlcoolicState? = null

    private var nonAlcoolicFullList = mutableListOf<Drink>()

    init {
        fetchCocktailsAll()
    }

    fun updateSearchText(searchText: String) {
        _searchStrStateFlow.value = searchText
        fetchDrinksToShow()
    }

    fun fetchDrinksToShow() {
        _nonAlcoolicSummaryListStateFlow.value = nonAlcoolicFullList.filter { drink ->
            drink.strDrink.contains(searchStrStateFlow.value, true)
        }
    }

    private fun fetchCocktailsAll() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiServiceCocktailDb.getAllNonAlcoolicDrinks()

                    val body = response?.body()

                    when {
                        response == null ->
                            currentState = NonAlcoolicState.ERROR_SERVER

                        response.isSuccessful && (body != null)-> {
                            nonAlcoolicFullList.addAll(body.drinks)
                            _isPbVisibleStateFlow.value = false
                            _isListReadySharedFlow.emit(true)
                        }
                    }
                }
            } catch (e: IOException) {
                currentState = NonAlcoolicState.ERROR_CONNECTION
            }
            currentState?.let {state->
                _messageSharedFlow.emit(state)
            }
        }
    }
}