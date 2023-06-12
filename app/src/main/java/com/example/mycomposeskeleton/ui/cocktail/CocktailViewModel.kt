package com.example.mycomposeskeleton.ui.cocktail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeskeleton.network.ApiServiceCocktailDb
import com.example.mycomposeskeleton.network.dto.Drink
import com.example.mycomposeskeleton.service.MySharedPref
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
class CocktailViewModel @Inject constructor(
    private val apiServiceCocktailDb: ApiServiceCocktailDb,
    private val sharedPref: MySharedPref
): ViewModel() {


    enum class CocktailState {
        ERROR_CONNECTION,
        ERROR_SERVER
    }

    private val _messageSharedFlow = MutableSharedFlow<CocktailState>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _isPbVisibleStateFlow = MutableStateFlow(true)
    val isPbVisibleStateFlow = _isPbVisibleStateFlow.asStateFlow()

    private val _searchStrStateFlow = MutableStateFlow("")
    val searchStrStateFlow= _searchStrStateFlow.asStateFlow()

    private val _cocktailSummaryListStateFlow = MutableStateFlow(emptyList<Drink>())
    val cocktailSummaryListStateFlow = _cocktailSummaryListStateFlow.asStateFlow()

    private val _isListReadySharedFlow = MutableSharedFlow<Boolean>()
    val isListReadySharedFlow = _isListReadySharedFlow.asSharedFlow()

    private var currentState: CocktailState? = null

    private var cocktailFullList = mutableListOf<Drink>()

    init {
        fetchCocktailsAll()
    }

    fun updateSearchText(searchText: String) {
        _searchStrStateFlow.value = searchText
        fetchCocktailsToShow()
    }

    fun fetchCocktailsToShow() {
        _cocktailSummaryListStateFlow.value = cocktailFullList.filter { cocktail ->
            cocktail.strDrink.contains(searchStrStateFlow.value, true)
        }
    }

    private fun fetchCocktailsAll() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiServiceCocktailDb.getAllCocktails()

                    val body = response?.body()

                    when {
                        response == null ->
                            currentState = CocktailState.ERROR_SERVER

                        response.isSuccessful && (body != null)-> {
                             cocktailFullList.addAll(body.drinks)
                            _isPbVisibleStateFlow.value = false
                            _isListReadySharedFlow.emit(true)
                        }
                    }
                }
            } catch (e: IOException) {
                currentState = CocktailState.ERROR_CONNECTION
            }
            currentState?.let {state->
                _messageSharedFlow.emit(state)
            }
        }
    }
}