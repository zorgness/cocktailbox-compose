package com.example.mycomposeskeleton.ui.login

import ERROR_400
import ERROR_401
import ERROR_503
import HTTP_200
import HTTP_304
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeskeleton.network.ApiService
import com.example.mycomposeskeleton.network.dto.LoginDto
import com.example.mycomposeskeleton.service.MySharedPref
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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPref: MySharedPref
): ViewModel() {

    enum class LoginState(val httpStatus: Int?) {
        SUCCESS(HTTP_200),
        ERROR_PARAM(ERROR_400),
        ERROR_CONNECTION(null),
        ERROR_SERVER(null),
        WRONG_CREDENTIAL(ERROR_401),
        EMPTY_FIELDS(null),
        ERROR_SERVICE(ERROR_503);

    }

    private fun getCurrentState(httpStatus: Int): LoginState? {
        LoginState.values().forEach { state ->
            if (state.httpStatus == httpStatus) {
                return state
            }
        }
        return null
    }


    /*
    *  KEEP TRACK OF EACH FIELDS
    */
    private val _emailStateFlow = MutableStateFlow("")
    val emailStateFlow = _emailStateFlow.asStateFlow()

    private val _passwordStateFlow = MutableStateFlow("")
    val passwordStateFlow = _passwordStateFlow.asStateFlow()

    private val _loginStateSharedFlow = MutableSharedFlow<LoginState>()
    val loginStateSharedFlow = _loginStateSharedFlow.asSharedFlow()

    /*
    * REDIRECTION
    */
    private val _goToMainSharedFlow = MutableSharedFlow<Screen>()
    val goToMainSharedFlow = _goToMainSharedFlow.asSharedFlow()

    private var currentState: LoginState? = null

    fun updateEmail(email: String) {
        _emailStateFlow.value = email
    }

    fun updatePassword(password: String) {
        _passwordStateFlow.value = password
    }


    fun login() {

        viewModelScope.launch {
            if (
                emailStateFlow.value.isNotBlank()
                &&
                passwordStateFlow.value.isNotBlank()
            ) {
                try {
                    withContext(Dispatchers.IO) {
                        val responseLogin =
                            apiService.login(
                                LoginDto(
                                    email = emailStateFlow.value,
                                    password = passwordStateFlow.value
                                )
                            )
                        val body = responseLogin?.body()

                        when {
                            responseLogin == null ->
                                currentState = LoginState.ERROR_SERVER

                            responseLogin.isSuccessful && (body != null) -> {

                                currentState = LoginState.SUCCESS
                                sharedPref.token = body.token
                                sharedPref.userId = body.id
                                _goToMainSharedFlow.emit(Screen.Main)
                            }

                            else -> {
                                getCurrentState(responseLogin.code())?.let { state ->
                                    currentState = state
                                }
                            }
                        }
                    }

                } catch (e: Exception) {
                    currentState = LoginState.ERROR_CONNECTION
                }
            } else {
                currentState = LoginState.EMPTY_FIELDS
            }
            currentState?.let {state->
                _loginStateSharedFlow.emit(state)
            }
        }
    }
}