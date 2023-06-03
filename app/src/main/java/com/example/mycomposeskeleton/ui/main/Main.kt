package com.example.mycomposeskeleton.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mycomposeskeleton.utils.Screen

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    LaunchedEffect(true) {
        viewModel.goToLoginSharedFlow.collect {
            navController.navigate(it.route) {
                popUpTo(Screen.Main.route) {
                    inclusive = true
                }
            }
        }
    }

    MainContent() {
        viewModel.logout()
    }
}

@Composable
fun MainContent(
    handleLogout: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MainHeader(
            onLogoutIconClicked = { handleLogout() }
        )
    }
}

@Composable
fun MainHeader(
    onLogoutIconClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Icon(
            Icons.Outlined.ExitToApp,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterEnd)
                .padding(8.dp)
                .clickable { onLogoutIconClicked() }
        )
    }
}