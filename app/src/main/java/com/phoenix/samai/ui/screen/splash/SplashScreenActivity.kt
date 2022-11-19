@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.phoenix.samai.ui.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.phoenix.samai.MainActivity
import com.phoenix.samai.R
import com.phoenix.samai.ui.components.RootView
import com.phoenix.samai.utils.RootUtils
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RootView {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    val progress = remember { mutableStateOf(0f) }
                    val i = remember {
                        mutableStateOf(0f)
                    }
                    val clockTicking = remember { mutableStateOf(false) }
                    val clock = object : CountDownTimer(25000, 10) {
                        override fun onTick(p0: Long) {
                            progress.value = (0.08f * i.value) / 100
                            i.value += 10
                            if (progress.value >= 1f) {
                                cancel()
                                if (RootUtils.isDeviceRooted) {
                                    Toast.makeText(
                                        this@SplashScreenActivity,
                                        "Device is rooted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@SplashScreenActivity,
                                        "Device is not rooted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    callIntent()
                                }
                            }
                        }

                        override fun onFinish() {

                        }
                    }
                    if (!clockTicking.value) {
                        clockTicking.value = true
                        clock.start()
                    }




                    Spacer(modifier = Modifier.weight(1f))
                    Row{
                        Spacer(modifier = Modifier.weight(1f))
                        SplashScreen(progress = progress)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    private fun callIntent() {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}


@OptIn(ExperimentalMotionApi::class)
@Composable
fun SplashScreen(progress: MutableState<Float>) {
    val context = LocalContext.current
    val dpSize = 350.dp
    val motionScene = remember {
        context.resources.openRawResource(R.raw.splash_motion_scene)
            .readBytes()
            .decodeToString()
    }
    MotionLayout(
        modifier = Modifier
            .width(dpSize)
            .height(dpSize),
        progress = progress.value,
        motionScene = MotionScene(content = motionScene)
    ) {


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
        Box(
            Modifier
                .width(dpSize)
                .height(dpSize)
                .layoutId("circle_1")
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width
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

        Box(
            Modifier
                .width(dpSize)
                .height(dpSize)
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width
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
                .width(dpSize)
                .height(dpSize)
                .layoutId("circle_2")
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width

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
                .width(dpSize)
                .height(dpSize)
                .layoutId("circle_3")
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width

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
                .width(dpSize)
                .height(dpSize)
                .layoutId("circle_4")
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width

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
        Box(
            Modifier
                .width(dpSize)
                .height(dpSize)
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width

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
                .width(dpSize)
                .height(dpSize)
                .layoutId("circle_5")
        ) {
            Canvas(
                modifier = Modifier
                    .width(dpSize)
                    .height(dpSize)
            ) {
                val width = size.width

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


}


