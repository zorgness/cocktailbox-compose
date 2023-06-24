package com.example.cocktailboxcompose.ui.main

import CustomLazyRow
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cocktailboxcompose.myanimation.addBrushEffect
import com.example.cocktailboxcompose.network.dto.Drink
import com.example.cocktailboxcompose.ui.darkmode.DarkViewModel
import com.example.cocktailboxcompose.ui.favorite.FavoriteViewModel
import com.example.cocktailboxcompose.ui.theme.Purple500
import com.example.cocktailboxcompose.utils.Screen
import com.example.mycomposeskeleton.R


@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    sharedViewModel: FavoriteViewModel,
    darkViewModel: DarkViewModel,
) {

    val cocktailList by viewModel.cocktailSummaryListStateFlow.collectAsState()
    val ordinaryList by viewModel.ordinarySummaryListStateFlow.collectAsState()
    val nonAlcoolicList by viewModel.nonAlcoolicSummaryListStateFlow.collectAsState()
    val isDarkMode by darkViewModel.isDarkMode.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.messageSharedFlow.collect { state ->
            when (state) {
                MainViewModel.MainState.ERROR_SERVER -> R.string.error_server
                MainViewModel.MainState.ERROR_CONNECTION -> R.string.error_connection
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(true) {
        sharedViewModel.messageSharedFlow.collect { state ->
            when (state) {
                FavoriteViewModel.FavoriteState.ERROR_SERVER -> R.string.error_server
                FavoriteViewModel.FavoriteState.ERROR_CONNECTION -> R.string.error_connection
                FavoriteViewModel.FavoriteState.TOKEN_EXPIRED -> R.string.token_expired
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(true) {
        with(viewModel) {
            fetchCocktailsAll()
            fetchOrdinariesAll()
            fetchNonAlcoolicsAll()
        }
    }

    LaunchedEffect(true) {
        viewModel.goToLoginSharedFlow.collect {
            navController.navigate(it.route) {
                popUpTo(Screen.Main.route) {
                    inclusive = true
                }
            }
        }
    }


    MainContent(
        context = context,
        cocktailList= cocktailList,
        ordinaryList= ordinaryList,
        nonAlcoolicList = nonAlcoolicList,
        isDarkMode = isDarkMode,
        checkIsFavorite = { sharedViewModel.checkIsFavorite(it) },
        handleDarkMode = { darkViewModel.setDarkMode() },
        handleLogout = { viewModel.logout() },
        handleItemClicked = { drinkId ->
            navController.navigate(Screen.Details.route + "/$drinkId" )
        },
        handleFavoriteClicked = {
            sharedViewModel.setFavorite(it) {setFavorite ->
                if(setFavorite)
                    with(viewModel) {
                        fetchCocktailsAll()
                        fetchOrdinariesAll()
                        fetchNonAlcoolicsAll()
                    }
            }


        },
        goToScreen = {
            navController.navigate(it)
        }
    )
}

@Composable
fun MainContent(
    context: Context,
    cocktailList: List<Drink>,
    ordinaryList: List<Drink>,
    isDarkMode: Boolean,
    nonAlcoolicList: List<Drink>,
    handleDarkMode: () -> Unit,
    checkIsFavorite: (Long) -> Boolean,
    handleLogout: () -> Unit,
    handleItemClicked: (Long) -> Unit,
    handleFavoriteClicked: (Long) -> Unit,
    goToScreen: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.Black)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            MainHeader(
                context = context,
                isDarkMode = isDarkMode,
                onLogoutIconClicked = { handleLogout() },
                onSwitchDarkMode = { handleDarkMode() }
            )

            Row(
                modifier = Modifier.height(300.dp)
            ) {
                CustomLazyRow(
                    title = context.getString(R.string.cocktails),
                    list = cocktailList,
                    checkIsFavorite = { checkIsFavorite(it) },
                    handleTitleClicked = { goToScreen(Screen.Cocktail.route) },
                    handleItemClicked = { handleItemClicked(it) },
                    handleFavoriteClicked = { handleFavoriteClicked(it)}
                )
            }

            Row(
                modifier = Modifier.height(300.dp)
            ) {
                CustomLazyRow(
                    title = context.getString(R.string.classics),
                    list = ordinaryList,
                    checkIsFavorite = { checkIsFavorite(it) },
                    handleTitleClicked = { goToScreen(Screen.Classic.route) },
                    handleItemClicked = { handleItemClicked(it) },
                    handleFavoriteClicked = { handleFavoriteClicked(it) }

                )
            }

            if(!isDarkMode) {
                Row(
                    modifier = Modifier.height(300.dp)
                ) {
                    CustomLazyRow(
                        title = context.getString(R.string.non_alcoolics),
                        list = nonAlcoolicList,
                        checkIsFavorite = { checkIsFavorite(it) },
                        handleTitleClicked = { goToScreen(Screen.NonAlcoolic.route) },
                        handleItemClicked = { handleItemClicked(it) },
                        handleFavoriteClicked = { handleFavoriteClicked(it) }
                    )
                }

            }


        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun MainHeader(
    context: Context,
    isDarkMode: Boolean,
    onLogoutIconClicked: () -> Unit,
    onSwitchDarkMode: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 20.dp,
                top = 40.dp,
                bottom = 60.dp
            ),
        contentAlignment = Alignment.Center

    ) {
        if(!isDarkMode) {
            Text(
                text = context.getString(R.string.cocktail_box),
                modifier = Modifier
                    .align(Alignment.CenterStart),
                //color = Color.White,
                fontSize = 36.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(brush = addBrushEffect(10000))
            )

        } else {
            Text(
                text = context.getString(R.string.cocktail_box),
                modifier = Modifier
                    .align(Alignment.CenterStart),
                //color = Color.White,
                fontSize = 36.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold,

                )
        }


        Switch(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end= 80.dp),
            checked = isDarkMode,
            onCheckedChange = {
                onSwitchDarkMode()
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                uncheckedThumbColor = Color.DarkGray
            )
        )
        Icon(
            Icons.Outlined.ExitToApp,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterEnd)
                .clickable { onLogoutIconClicked() },
            tint = Purple500
        )
    }
}


