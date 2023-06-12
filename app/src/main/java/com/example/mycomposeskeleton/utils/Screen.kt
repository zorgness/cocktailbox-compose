package com.example.mycomposeskeleton.utils

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Cocktail : Screen("cocktail")
    object Classic : Screen("classic")
    object NonAlcoolic : Screen("nonAlcoolic")
    object Details : Screen("details")
    object Login : Screen("login")
    object Register : Screen("register")

    object NotFound : Screen("notFound")
}