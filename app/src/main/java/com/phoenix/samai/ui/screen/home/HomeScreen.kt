package com.phoenix.samai.ui.screen.home

import android.content.Context
import android.os.CountDownTimer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phoenix.samai.R
import com.phoenix.samai.ui.components.BottomBar
import com.phoenix.samai.ui.screen.Destinations
import com.phoenix.samai.ui.screen.alarm.AlarmScreen
import com.phoenix.samai.ui.screen.clock.ClockScreen
import com.phoenix.samai.ui.screen.stopwatch.StopWatchScreen
import com.phoenix.samai.ui.screen.timer.TimerScreen


@Composable
fun HomeScreen(
    context: Context,
    parentNavController: NavHostController,
    once: MutableState<Boolean>
) {
    val navController = rememberNavController()
    Scaffold(
        drawerContent = {
            SideDrawer()
        },
        bottomBar = {
            val progress = remember { mutableStateOf(1f) }
            val i = remember {
                mutableStateOf(0f)
            }
            val clockTicking = remember { mutableStateOf(false) }
            val clock = object : CountDownTimer(2000, 10) {
                override fun onTick(p0: Long) {
                    progress.value = (0.05f * i.value) / 100
                    i.value += 10
                    if (progress.value >= 1f) {
                        cancel()
                    }
                }

                override fun onFinish() {
                }
            }
            if (!clockTicking.value && once.value) {
                clockTicking.value = true
                clock.start()
                once.value = false
            }
            Row(
                horizontalArrangement = Arrangement.End,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier) {
                    startCircle(modifier = Modifier.align(Alignment.BottomEnd), progress = progress)
                    BottomBar(parentNavController, navController)
                }
            }
        },
//
        ) {
        NavHost(
            navController = navController,
            startDestination = Destinations.CLOCK_ROUTE,
            modifier = Modifier.padding(it)
        ) {
            composable(Destinations.CLOCK_ROUTE) {
                ClockScreen(parentNavController)
            }
            composable(Destinations.ALARM_ROUTE) {
                AlarmScreen(parentNavController)
            }
            composable(Destinations.TIMER_ROUTE) {
                TimerScreen(context) { navController.navigateUp() }
            }
            composable(Destinations.STOP_WATCH_ROUTE) {
                StopWatchScreen { navController.navigateUp() }
            }
        }
    }
}

@Composable
@Preview
fun pprivew() {
    Row(
        horizontalArrangement = Arrangement.End,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier) {
            startCircle(modifier = Modifier.align(Alignment.BottomEnd))
//            BottomBar()
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun startCircle(
    modifier: Modifier? = Modifier,
    progress: MutableState<Float>? = mutableStateOf(1f)
) {
    val context = LocalContext.current
    val dpSize = 2.dp
    val motionScene = remember {
        context.resources.openRawResource(R.raw.home_motion_scene)
            .readBytes()
            .decodeToString()
    }
    MotionLayout(
        modifier = modifier!!
            .width(dpSize)
            .height(dpSize),
        progress = progress!!.value,
        motionScene = MotionScene(content = motionScene)
    ) {


        val red = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.secondary,
                MaterialTheme.colors.secondary
            )
        )
        val white = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.background,
                MaterialTheme.colors.background
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
                val height = size.height

                drawArc(
                    topLeft = Offset(0f, 0f),
                    brush = red,
                    size = Size(width, width),
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
                val height = size.height

                drawArc(
                    topLeft = Offset(0f, 0f),
                    brush = white,
                    size = Size(width, width),
                    useCenter = true,
                    startAngle = 0f,
                    sweepAngle = 360f
                )
            }
        }

    }
}

@Composable
fun SideDrawer() {
    Text(text = "Test Text Side Drawer")
}

