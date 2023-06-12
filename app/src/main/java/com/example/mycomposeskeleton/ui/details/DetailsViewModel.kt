package com.example.mycomposeskeleton.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeskeleton.network.ApiServiceCocktailDb
import com.example.mycomposeskeleton.network.dto.Drink
import com.example.mycomposeskeleton.network.dto.DrinkDetail
import com.example.mycomposeskeleton.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val apiServiceCocktailDb: ApiServiceCocktailDb
): ViewModel() {

    enum class DetailsState {
        ERROR_SERVER,
        ERROR_CONNECTION,
        DRINK_NOT_FOUND
    }

    private val _messageSharedFlow = MutableSharedFlow<DetailsState>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _drinkStateFlow = MutableStateFlow<DrinkDetail?>(null)
    var drinkStateFlow = _drinkStateFlow.asStateFlow()

    private val _goToNotFoundScreen = MutableSharedFlow<Screen>()
    val goToNotFoundScreen = _goToNotFoundScreen.asSharedFlow()


    private var currentState: DetailsState? = null


    fun fetchDrinkById(drinkId: Long) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = apiServiceCocktailDb.getDrinkById(drinkId)
                    val body = response?.body()

                    when {
                        response == null ->
                            currentState = DetailsState.ERROR_SERVER

                        response.isSuccessful && (body != null) -> {
                            if( body.drinks.isEmpty()) {
                               _goToNotFoundScreen.emit(Screen.NotFound)
                            } else
                                _drinkStateFlow.value = body.drinks[0]
                        }
                    }
                }
            }  catch (e: Exception) {
                currentState = DetailsState.ERROR_CONNECTION

            }
        }
    }

    fun getIngredientsAndMeasuresList(drink: DrinkDetail?): List<MutableList<String>> {
        val ingredients = mutableListOf<String>()
        val measures = mutableListOf<String>()

        drink?.let {
            for(prop in drink.javaClass.declaredFields) {
                prop.isAccessible = true

                when{
                    prop.name.contains("strIngredient" ) && prop.get(it) != null ->
                        ingredients.add(prop.get(it) as String)

                    prop.name.contains("strMeasure" ) && prop.get(it) != null ->
                        measures.add(prop.get(it) as String)
                }
            }
        }

        return listOf(ingredients, measures)
    }

}

