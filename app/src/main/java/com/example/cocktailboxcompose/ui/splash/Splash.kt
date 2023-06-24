package com.example.cocktailboxcompose.ui.splash

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.R
import com.example.cocktailboxcompose.myanimation.addBrushEffect
import com.example.cocktailboxcompose.utils.Screen


@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel
) {

    val context = LocalContext.current
    val isBig by viewModel.isBigStateflow.collectAsState()


    LaunchedEffect(true ) {
        viewModel.goToScreen.collect {
            navController.navigate(it.route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
    }

    viewModel.initSplash()
    SplashContent(
        context,
        isBig
    )

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun SplashContent(
    context: Context,
    isBig: Boolean

) {

    val animateText by animateIntAsState(
          targetValue =  if(isBig) 60  else 2,
          animationSpec = tween(1000, 200, easing = LinearEasing)
    )


    Column(
        modifier = Modifier
            .fillMaxSize(),
            //.background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = context.getString(R.string.cocktail_box),
            modifier = Modifier
                .padding(
                    start= 24.dp,
                    top= 24.dp,
                    bottom= 44.dp
                ),
            color = Color.White,
            fontSize = animateText.sp,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.ExtraBold,
            style = TextStyle(brush = addBrushEffect(2000))
        )
    }

}


