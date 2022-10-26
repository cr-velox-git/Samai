package com.phoenix.samai.ui.screen.home

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


@Composable
fun HomeScreen(parentNavController: NavHostController) {
    val navController = rememberNavController()
    Scaffold(
        drawerContent = {
            SideDrawer()
        },
        bottomBar = {
            BottomBar(parentNavController, navController)
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
//                TimerScreen { navController.navigateUp() }
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

