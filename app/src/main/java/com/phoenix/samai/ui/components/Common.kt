package com.phoenix.samai.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.phoenix.samai.R
import com.phoenix.samai.ui.screen.Destinations

@Preview
@Composable
fun BottomBar(
    parentNavController: NavController? = null,
    navController: NavHostController? = null
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary, shape = RoundedCornerShape(30.dp,0.dp,0.dp,0.dp))
            .padding(4.dp)
    ) {
//        Spacer(modifier = Modifier.weight(1f, true))
        val size = 55.dp
        val padding = 16.dp
        Image(
            painter = painterResource(R.drawable.ic_clock),
            contentDescription = stringResource(id = R.string.image_desc),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .width(size)
                .height(size)
                .clickable {
                    navController?.navigate(Destinations.CLOCK_ROUTE) {
                        popUpTo(Destinations.CLOCK_ROUTE) { inclusive = true }
                    }
                }
                .padding(padding)
        )
        Image(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = stringResource(id = R.string.image_desc),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .width(size)
                .height(size)
                .clickable {
                    navController?.navigate(Destinations.ALARM_ROUTE) {
                        popUpTo(Destinations.CLOCK_ROUTE)
                    }
                }
                .padding(padding)
        )
        Image(
            painter = painterResource(R.drawable.chronometer),
            contentDescription = stringResource(id = R.string.image_desc),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .width(size)
                .height(size)
                .clickable {
                    navController?.navigate(Destinations.TIMER_ROUTE) {
                        popUpTo(Destinations.CLOCK_ROUTE)
                    }
                }
                .padding(padding)
        )

        Image(
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = stringResource(id = R.string.image_desc),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .width(size)
                .height(size)
                .clickable {
                    parentNavController?.navigate(Destinations.SETTINGS_ROUTE)
                }
                .padding(padding)
        )

    }
}