package com.example.mycomposeskeleton.ui.details

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HourglassFull
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mycomposeskeleton.R
import com.example.mycomposeskeleton.network.dto.DrinkDetail
import com.example.mycomposeskeleton.ui.theme.MyComposeSkeletonTheme
import com.example.mycomposeskeleton.utils.Screen


@Composable
fun DetailsScreen(
    navController: NavHostController,
    viewModel: DetailsViewModel,
    drinkId: Long
) {

    val drink by viewModel.drinkStateFlow.collectAsState()
    val context = LocalContext.current

    val ingredientsAndMeasuresList = viewModel.getIngredientsAndMeasuresList(drink)

    LaunchedEffect(drinkId) {
        viewModel.fetchDrinkById(drinkId)
    }


    LaunchedEffect(true ) {
        viewModel.goToNotFoundScreen.collect {
            navController.navigate(it.route)
        }
    }

    LaunchedEffect(true) {
        viewModel.messageSharedFlow.collect { state ->
            when (state) {
                DetailsViewModel.DetailsState.ERROR_SERVER-> R.string.error_server
                DetailsViewModel.DetailsState.ERROR_CONNECTION-> R.string.error_connection
                DetailsViewModel.DetailsState.DRINK_NOT_FOUND -> R.string.drink_not_found
            }.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    DetailsContent(
        drink = drink,
        list = ingredientsAndMeasuresList
    )
}


@Composable
fun DetailsContent(
    drink: DrinkDetail?,
    list: List<MutableList<String>>
) {

    if (drink != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.Black)
        ) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(drink.strDrinkThumb)
                    .crossfade(true)
                    .build(),
                error= painterResource(id = R.drawable.cocktail),
                contentDescription = null,
                contentScale= ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()

            )

            Column {
                Text(
                    text = drink.strDrink,
                    modifier = Modifier.padding(20.dp) ,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )


               /* val scroll = rememberScrollState(0)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(scroll),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {

                    Text(
                        text= if((drink.strCategory ?: "")
                                .contains("other", true)) "Other"
                              else drink.strCategory ?: "" ,
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                Color.DarkGray,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp),
                        color = Color.White
                    )

                    Text(
                        text = drink.strAlcoholic ?: "",
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                Color.DarkGray,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp),
                        color = Color.White
                    )

                  *//*  Text(
                        text =  drink.strGlass ?: "",
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                Color.DarkGray,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp),
                        color = Color.White
                    )*//*
                }
*/
                Text(text = "Ingredients",
                    color = Color.White,
                    modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),

                ) {
                        list[0].forEachIndexed { idx, value ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = value,
                                    color = Color.White
                                )
                                if(list[0].count() == list[1].count()) {
                                    Text(
                                        text = list[1][idx],
                                        color = Color.White
                                    )
                                }

                            }
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                Color.Gray,
                                1.dp
                            )
                        }
               }

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Instructions",
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )

                    Text(
                        text = drink.strInstructions,
                        color = Color.White
                    )
                }
            }
        }
    }
}

