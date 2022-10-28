package com.phoenix.samai.ui.screen.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.phoenix.samai.ui.components.RootView
import java.util.*

@OptIn(ExperimentalMotionApi::class)
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RootView {
                SplashScreen()
            }
        }
    }
}

@Composable
@Preview
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        val dpSize = 350.dp
        val red = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.secondary,
                MaterialTheme.colors.secondary
            )
        )
        val black = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.primary
            )
        )

        val white = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.background,
                MaterialTheme.colors.background
//                Color.Green,
//                Color.Green
            )
        )

        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .width(dpSize)
                .height(dpSize)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .layoutId("circle_1")) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(0f, 0f),
                        brush = red,
                        size = Size(width, width),
                        useCenter = true,
                        startAngle = 0f,
                        sweepAngle = 160f
                    )
                }
            }

            Box(Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(20f, 20f),
                        brush = white,
                        size = Size(width - 40, width - 40),
                        useCenter = true,
                        startAngle = 0f,
                        sweepAngle = 360f
                    )
                }
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .layoutId("circle_2")) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(50f, 50f),
                        brush = black,
                        size = Size(width - 100, width - 100),
                        useCenter = true,
                        startAngle = 120f,
                        sweepAngle = 200f
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .layoutId("circle_3")) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(0f, 0f),
                        brush = red,
                        size = Size(width, width),
                        useCenter = true,
                        startAngle = 0f,
                        sweepAngle = 60f
                    )
                }
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .layoutId("circle_4")) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(120f, 120f),
                        brush = white,
                        size = Size(width - 240, width - 240),
                        useCenter = true,
                        startAngle = 200f,
                        sweepAngle = 60f
                    )
                }
            }
            Box(Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(140f, 140f),
                        brush = white,
                        size = Size(width - 280, width - 280),
                        useCenter = true,
                        startAngle = 0f,
                        sweepAngle = 360f
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .layoutId("circle_5")) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    drawArc(
                        topLeft = Offset(170f, 170f),
                        brush = red,
                        size = Size(width - 340, width - 340),
                        useCenter = true,
                        startAngle = 0f,
                        sweepAngle = 360f
                    )
                }
            }

        }
        Spacer(Modifier.weight(1f))

    }

}

