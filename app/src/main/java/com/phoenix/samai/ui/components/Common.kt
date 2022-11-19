package com.phoenix.samai.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@Composable
fun Progress(modifier: Modifier, pProgress: Float, sProgress: Float) {
    val sizeDp = 300.dp
    val thickness = 100f

    val pStartAngle = 150f
    val pEndAngle = 240f
    val sStartAngle = 30f
    val sEndAngle = 120f

    Box(
        modifier = modifier
    ) {
        val brush1 = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.secondary,
                MaterialTheme.colors.secondary
            )
        )
        val brush2 = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.primary
            )
        )
        val brush3 = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.background,
                MaterialTheme.colors.background
            )
        )
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val width = size.width
            val height = size.height


            drawArc(
                topLeft = Offset(0f, 0f),
                brush = brush2,
                size = Size(width, width),
                useCenter = true,
                startAngle = sStartAngle,
                sweepAngle = sEndAngle * (sProgress / 100)
            )
            drawArc(
                topLeft = Offset(0f, 0f),
                brush = brush1,
                useCenter = true,
                size = Size(width, width),
                startAngle = pStartAngle,
                sweepAngle = pEndAngle * (pProgress / 100f)
            )
            drawArc(
                topLeft = Offset(0f + thickness / 2, 0f + thickness / 2),
                brush = brush3,
                useCenter = true,
                size = Size(width - thickness, width - thickness),
                startAngle = 0f,
                sweepAngle = 360f
            )
        })
    }
}