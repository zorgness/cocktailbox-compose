package com.example.mycomposeskeleton.myanimation

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier



@Composable
fun CategoryTransition(content: @Composable () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        isVisible = !isVisible
    }
    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier,
        enter = slideInHorizontally(
            animationSpec = tween(500, 200, easing = LinearEasing),
            initialOffsetX = { -2000 }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(500, 200, easing = LinearOutSlowInEasing),
            targetOffsetX = { 2000 }
        ),

        ) {
        content()
    }
}

@Composable
fun DetailsTransition(content: @Composable () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        isVisible = !isVisible
    }
    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier,
        enter = slideInVertically(
            animationSpec = tween(500, 200, easing = LinearOutSlowInEasing),
            initialOffsetY = { 4000 }
        ),
        exit = slideOutVertically(
            animationSpec = tween(500, 200, easing = LinearOutSlowInEasing)
        ),

    ) {
        content()
    }
}

