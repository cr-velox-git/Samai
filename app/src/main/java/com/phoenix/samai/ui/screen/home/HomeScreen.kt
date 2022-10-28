package com.phoenix.samai.ui.screen.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phoenix.samai.ui.components.BottomBar
import com.phoenix.samai.ui.screen.Destinations
import com.phoenix.samai.ui.screen.alarm.AlarmScreen
import com.phoenix.samai.ui.screen.clock.ClockScreen
import com.phoenix.samai.ui.screen.stopwatch.StopWatchScreen
import androidx.compose.ui.Modifier
import com.phoenix.samai.ui.screen.timer.TimerScreen


@Composable
fun HomeScreen(context: Context,parentNavController: NavHostController) {
    val navController = rememberNavController()
    Scaffold(
        drawerContent = {
            SideDrawer()
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.End,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                BottomBar(parentNavController, navController)
            }
        }) {
        NavHost(
            navController = navController,
            startDestination = Destinations.CLOCK_ROUTE
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
fun SideDrawer() {
    Text(text = "Test Text Side Drawer")
}

