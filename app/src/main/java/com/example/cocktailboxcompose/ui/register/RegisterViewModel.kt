package com.example.cocktailboxcompose.ui.register

import ERROR_400
import ERROR_503
import HTTP_200
import HTTP_303
import HTTP_304
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailboxcompose.network.ApiService
import com.example.cocktailboxcompose.network.dto.RegisterDto
import com.example.cocktailboxcompose.network.dto.UserDto
import com.example.cocktailboxcompose.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context,
): ViewModel() {

    enum class RegisterState(val httpStatus: Int?) {
        ERROR_SERVICE(ERROR_503),
        ERROR_CONNECTION(null),
        ERROR_SERVER(null),
        EMPTY_FIELDS(null),
        ERROR_CONFIRMATION(null),
        ERROR_PARAM(ERROR_400),
        LOGIN_USED(HTTP_303),
        SUCCESS(HTTP_200),
        FAILURE(HTTP_304);
    }

    private fun getCurrentState(httpStatus: Int): RegisterState? {
        RegisterState.values().forEach { state ->
            if (state.httpStatus == httpStatus) {
                return state
            }
        }
        return null
    }

    private val _emailStateFlow = MutableStateFlow("")
    val emailStateFlow = _emailStateFlow.asStateFlow()

    private val _usernameStateFlow = MutableStateFlow("")
    val usernameStateFlow = _usernameStateFlow.asStateFlow()

    private val _passwordStateFlow = MutableStateFlow("")
    val passwordStateFlow = _passwordStateFlow.asStateFlow()

    private val _confirmStateFlow = MutableStateFlow("")
    val confirmStateFlow = _confirmStateFlow.asStateFlow()

    private val _registerStateSharedFlow = MutableSharedFlow<RegisterState>()
    val registerStateSharedFlow = _registerStateSharedFlow.asSharedFlow()

    private val _goToLoginSharedFlow = MutableSharedFlow<Screen>()
    val goToLoginSharedFlow = _goToLoginSharedFlow.asSharedFlow()


    private var currentState: RegisterState? = null

    fun updateEmail(email: String) {
        _emailStateFlow.value = email
    }
    fun updateUsername(username: String) {
        _usernameStateFlow.value = username
    }
    fun updatePassword(password: String) {
        _passwordStateFlow.value = password
    }
    fun updateConfirm(confirm: String) {
        _confirmStateFlow.value = confirm
    }



    fun register() {
        viewModelScope.launch {
            if (
                emailStateFlow.value.isNotBlank()
                &&
                passwordStateFlow.value.isNotBlank()
            ) {
                if (passwordStateFlow.value == confirmStateFlow.value) {

                    try {
                        withContext(Dispatchers.IO) {
                            val responseRegister: Response<UserDto>? = apiService.register(
                                RegisterDto(
                                    email = emailStateFlow.value,
                                    username = usernameStateFlow.value,
                                    password = passwordStateFlow.value
                                )
                            )

                            val body = responseRegister?.body()

                            when {
                                responseRegister == null ->
                                    currentState = RegisterState.ERROR_CONNECTION

                                responseRegister.isSuccessful && (body != null) -> {
                                        currentState = RegisterState.SUCCESS

                                        _goToLoginSharedFlow.emit(Screen.Login)
                                }

                                else -> {
                                    getCurrentState(responseRegister.code()).let { state ->
                                        currentState = state
                                    }
                                }
                            }
                        }

                    } catch (e: Exception) {
                        currentState = RegisterState.ERROR_CONNECTION
                    }
                } else
                    currentState = RegisterState.ERROR_CONFIRMATION
            } else {
                currentState = RegisterState.EMPTY_FIELDS
            }

            currentState?.let { state ->
                _registerStateSharedFlow.emit(state)
            }
        }
    }



}
