import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.ui.login.LoginViewModel
import com.example.mycomposeskeleton.utils.Screen
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.myanimation.addBrushEffect

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    val email by viewModel.emailStateFlow.collectAsState("")
    val password by viewModel.passwordStateFlow.collectAsState("")

    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.goToMainSharedFlow.collect {
            navController.navigate(it.route) {
                popUpTo(Screen.Login.route) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(true ) {
        viewModel.loginStateSharedFlow.collect {state->
            when(state) {
                LoginViewModel.LoginState.ERROR_SERVER -> R.string.error_server
                LoginViewModel.LoginState.ERROR_CONNECTION -> R.string.error_connection
                LoginViewModel.LoginState.WRONG_CREDENTIAL -> R.string.wrong_credentials
                LoginViewModel.LoginState.EMPTY_FIELDS -> R.string.empty_fields
                LoginViewModel.LoginState.ERROR_PARAM -> R.string.error_param
                LoginViewModel.LoginState.ERROR_SERVICE -> R.string.error_service
                LoginViewModel.LoginState.SUCCESS -> R.string.login_success
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoginContent(
        context = context,
        email = email,
        password = password,
        handleEmail = { viewModel.updateEmail(it) },
        handlePassword = { viewModel.updatePassword(it) },
        handleClick = { viewModel.login() },
        goToRegister = {
            navController.navigate(Screen.Register.route)
        }
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun LoginContent(
    context: Context,
    email: String,
    password: String,
    handleEmail: (String) -> Unit,
    handlePassword: (String) -> Unit,
    handleClick: () -> Unit,
    goToRegister: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        Text(
            text = context.getString(R.string.connect),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp),
            style = TextStyle(brush = addBrushEffect(10000))
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 30.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                placeholder = context.getString(R.string.email),
                value = email,
                handleValue = { handleEmail(it) }
            )
            Spacer(modifier = Modifier.height(40.dp))
            CustomTextField(
                placeholder = context.getString(R.string.password),
                value = password,
                isPassword = true,
                handleValue = { handlePassword(it) }
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                onClick = { handleClick() }
            ) {
                Text(
                    text = context.getString(R.string.connect),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = context.getString(R.string.no_account_yet),
                color = Color.White,
                modifier = Modifier.clickable { goToRegister() },
                maxLines = 2

            )
        }
    }
}