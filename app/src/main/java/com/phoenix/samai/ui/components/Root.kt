package com.phoenix.samai.ui.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.phoenix.samai.ui.screen.Destinations
import com.phoenix.samai.ui.screen.alarm.CreateAlarmScreen
import com.phoenix.samai.ui.screen.clock.TimeZoneScreen
import com.phoenix.samai.ui.screen.home.HomeScreen
import com.phoenix.samai.ui.screen.setting.SettingsScreen
import com.phoenix.samai.ui.screen.timer.TimerScreen
import com.phoenix.samai.ui.theme.SamaiTheme

@Composable
fun RootView(content: @Composable () -> Unit){
    SamaiTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        val darkIcons = MaterialTheme.colors.isLight
        SideEffect { systemUiController.setSystemBarsColor(Color.Transparent, darkIcons) }
        Surface { content.invoke() }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun NavigationContainer(context: Context, once: MutableState<Boolean>) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            modifier = containerModifier,
            navController = navController,
            startDestination = Destinations.HOME_ROUTE
        ) {
            composable(Destinations.HOME_ROUTE) {
                HomeScreen(context, navController,once)
            }
            bottomSheet(Destinations.TIMEZONE_ROUTE) {
                TimeZoneScreen(bottomSheetNavigator) {
                    navController.navigateUp()
                }
            }
            composable(Destinations.SETTINGS_ROUTE) {
                SettingsScreen {
                    navController.navigateUp()
                }
            }
            composable(Destinations.TIMER_ROUTE) {
                TimerScreen (context){
//                    navController.navigateUp()
                }
            }
            bottomSheet(Destinations.CREATE_ALARM_ROUTE) {
                CreateAlarmScreen(
//                    navController
                )
            }
        }
    }
}

private val containerModifier = Modifier
    .fillMaxSize()
    .statusBarsPadding()
    .navigationBarsPadding()