package com.example.cocktailboxcompose

import LoginScreen
import NotFoundScreen
import com.example.cocktailboxcompose.ui.register.RegisterScreen
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cocktailboxcompose.myanimation.CategoryTransition
import com.example.cocktailboxcompose.myanimation.DetailsTransition
import com.example.cocktailboxcompose.ui.classic.ClassicScreen
import com.example.cocktailboxcompose.ui.classic.ClassicViewModel
import com.example.cocktailboxcompose.ui.cocktail.CocktailScreen
import com.example.cocktailboxcompose.ui.cocktail.CocktailViewModel
import com.example.cocktailboxcompose.ui.darkmode.DarkViewModel
import com.example.cocktailboxcompose.ui.details.DetailsScreen
import com.example.cocktailboxcompose.ui.details.DetailsViewModel
import com.example.cocktailboxcompose.ui.favorite.FavoriteViewModel
import com.example.cocktailboxcompose.ui.login.LoginViewModel
import com.example.cocktailboxcompose.ui.main.MainScreen
import com.example.cocktailboxcompose.ui.main.MainViewModel
import com.example.cocktailboxcompose.ui.non_alcoolic.NonAlcoolicScreen
import com.example.cocktailboxcompose.ui.non_alcoolic.NonAlcoolicViewModel
import com.example.cocktailboxcompose.ui.register.RegisterViewModel
import com.example.cocktailboxcompose.ui.splash.SplashScreen
import com.example.cocktailboxcompose.ui.splash.SplashViewModel
import com.example.cocktailboxcompose.ui.theme.CocktailBoxComposeTheme
import com.example.cocktailboxcompose.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var darkViewModel: DarkViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            darkViewModel= hiltViewModel()
            val isDarkMode by darkViewModel!!.isDarkMode.collectAsState()

            CocktailBoxComposeTheme(
                darkTheme = isDarkMode
            ) {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),

                    color = MaterialTheme.colors.background
                    //color = Color.Black
                ) {

                    AppNavigation(
                        isDarkMode = isDarkMode,
                        darkViewModel = darkViewModel!!
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onStart() {
        super.onStart()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }


}

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    darkViewModel: DarkViewModel
) {
    val navController = rememberNavController()
    val sharedViewModel: FavoriteViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(
                navController,
                mainViewModel,
                sharedViewModel,
                darkViewModel
            )

        }
        composable(Screen.Cocktail.route) {
            val cocktailViewModel: CocktailViewModel = hiltViewModel()
            if(!isDarkMode) {
                CategoryTransition {
                    CocktailScreen(
                        navController,
                        cocktailViewModel,
                        sharedViewModel
                    )
                }

            } else {

                    CocktailScreen(
                        navController,
                        cocktailViewModel,
                        sharedViewModel
                    )
            }

        }
        composable(Screen.Classic.route) {
            val classicViewModel: ClassicViewModel = hiltViewModel()

            CategoryTransition {
                ClassicScreen(
                    navController,
                    classicViewModel,
                    sharedViewModel
                )
            }
        }
        composable(Screen.NonAlcoolic.route) {
            val nonAlcoolicViewModel: NonAlcoolicViewModel = hiltViewModel()
            CategoryTransition {
                NonAlcoolicScreen(
                    navController,
                    nonAlcoolicViewModel,
                    sharedViewModel
                )
            }
        }
        composable(
            Screen.Details.route + "/{cocktailId}",
            arguments = listOf(
                navArgument("cocktailId"){
                    type = NavType.LongType
                }
            )
        ){
            val detailsViewModel: DetailsViewModel = hiltViewModel()
            val cocktailId = it.arguments?.getLong("cocktailId") ?: 0L
            if(!isDarkMode) {
                DetailsTransition{
                    DetailsScreen(
                        navController,
                        detailsViewModel,
                        cocktailId,
                        sharedViewModel
                    )
                }

            } else {
                DetailsScreen(
                    navController,
                    detailsViewModel,
                    cocktailId,
                    sharedViewModel
                )

            }

        }
        composable(Screen.NotFound.route) {
            NotFoundScreen()
        }


    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CocktailBoxComposeTheme {

    }
}