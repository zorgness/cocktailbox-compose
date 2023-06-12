package com.example.mycomposeskeleton

import LoginScreen
import NotFoundScreen
import com.example.mycomposeskeleton.ui.register.RegisterScreen
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycomposeskeleton.myanimation.CategoryTransition
import com.example.mycomposeskeleton.myanimation.DetailsTransition
import com.example.mycomposeskeleton.ui.classic.ClassicScreen
import com.example.mycomposeskeleton.ui.classic.ClassicViewModel
import com.example.mycomposeskeleton.ui.cocktail.CocktailScreen
import com.example.mycomposeskeleton.ui.cocktail.CocktailViewModel
import com.example.mycomposeskeleton.ui.details.DetailsScreen
import com.example.mycomposeskeleton.ui.details.DetailsViewModel
import com.example.mycomposeskeleton.ui.favorite.FavoriteViewModel
import com.example.mycomposeskeleton.ui.login.LoginViewModel
import com.example.mycomposeskeleton.ui.main.MainScreen
import com.example.mycomposeskeleton.ui.main.MainViewModel
import com.example.mycomposeskeleton.ui.non_alcoolic.NonAlcoolicScreen
import com.example.mycomposeskeleton.ui.non_alcoolic.NonAlcoolicViewModel
import com.example.mycomposeskeleton.ui.register.RegisterViewModel
import com.example.mycomposeskeleton.ui.splash.SplashScreen
import com.example.mycomposeskeleton.ui.splash.SplashViewModel
import com.example.mycomposeskeleton.ui.theme.MyComposeSkeletonTheme
import com.example.mycomposeskeleton.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeSkeletonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),

                    //color = MaterialTheme.colors.background
                    color = Color.Black
                ) {

                    AppNavigation()
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
fun AppNavigation() {
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
            MainScreen(navController, mainViewModel, sharedViewModel)

        }
        composable(Screen.Cocktail.route) {
            val cocktailViewModel: CocktailViewModel = hiltViewModel()
            CategoryTransition {
                CocktailScreen(navController, cocktailViewModel)
            }
        }
        composable(Screen.Classic.route) {
            val classicViewModel: ClassicViewModel = hiltViewModel()
            CategoryTransition {
                ClassicScreen(navController, classicViewModel)
            }
        }
        composable(Screen.NonAlcoolic.route) {
            val nonAlcoolicViewModel: NonAlcoolicViewModel = hiltViewModel()
            CategoryTransition {
                NonAlcoolicScreen(navController, nonAlcoolicViewModel)
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
            DetailsTransition{
                DetailsScreen( navController, detailsViewModel, cocktailId)
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
    MyComposeSkeletonTheme {

    }
}