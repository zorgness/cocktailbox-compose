package com.example.cocktailboxcompose.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailboxcompose.service.MySharedPref
import com.example.cocktailboxcompose.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPref: MySharedPref
): ViewModel() {

    private val _isBigStateflow = MutableStateFlow(false)
    val isBigStateflow = _isBigStateflow.asStateFlow()

    private val _goToScreen = MutableSharedFlow<Screen>()
    val goToScreen = _goToScreen.asSharedFlow()

    fun initSplash() {
        _isBigStateflow.value = true
        viewModelScope.launch {
            delay(2400)
            sharedPref.token?.let {
                _goToScreen.emit(Screen.Main)
            }
                ?:  _goToScreen.emit(Screen.Login)

        }

    }
}