package com.phoenix.samai.ui.screen.clock

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.phoenix.samai.R
import com.phoenix.samai.data.local.TimesZonesTable
import com.phoenix.samai.data.viewmodels.ClockViewModel
import com.phoenix.samai.utils.dateFormatterAA
import com.phoenix.samai.utils.dateFormatterDay
import com.phoenix.samai.utils.dateFormatterSimple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.*


@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun TimeZoneScreen(bottomSheetNavigator: BottomSheetNavigator, navigateUp: () -> Unit) {
    val viewModel by KoinJavaComponent.inject<ClockViewModel>(ClockViewModel::class.java)
    // val selectedTimeZones = viewModel.selectedTimeZoneList().collectAsState(initial = emptyList())
    val allTimeZones = viewModel.timeZoneList().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = null, block = {
        coroutineScope.launch(Dispatchers.IO) {
            viewModel.prePopulateDataBase()
        }
        viewModel.timeZoneList().collect {
            Log.e("collect", it.filter { it.selected }.joinToString(", "))
        }
    })
    var isExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(bottomSheetNavigator.navigatorSheetState.targetValue) {
        isExpanded =
            bottomSheetNavigator.navigatorSheetState.targetValue == ModalBottomSheetValue.Expanded
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            TopAppBar(elevation = if (isExpanded) 4.dp else 0.dp) {
                Spacer(modifier = Modifier.width(10.dp))
                if (isExpanded) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                        modifier = Modifier
                            .clickable {
                                navigateUp.invoke()
                            }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = "Time Zone",
                    style = MaterialTheme.typography.h6
                )

            }
        }
    ) {
        LazyColumn(state = listState) {
            items(allTimeZones.value) { item ->
                TimeZoneListRowSmallView(item) {
                    viewModel.updateTimeZone(item)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeZoneListRowSmallView(
    timeZoneItem: TimesZonesTable,
    date: Date = Date(),
    onClick: () -> Unit
) {
    val timeZone = TimeZone.getTimeZone(timeZoneItem.time_id)
    val ampm = dateFormatterAA.apply { setTimeZone(timeZone) }.format(date)
    val time = dateFormatterSimple.apply { setTimeZone(timeZone) }.format(date)
    Surface(onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 0.dp)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f, fill = true),
                text = timeZoneItem.name,
                style = MaterialTheme.typography.h6
            )

            Row(modifier = Modifier.width(100.dp)) {
                Text(
                    text = time.subSequence(0, 5).toString(),
                    style = MaterialTheme.typography.h6
                )
                /*Text(
                    text = ampm.toUpperCase(Locale.current),
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(3.dp, 12.dp, 0.dp, 0.dp)
                )*/
            }
            Image(
                painter = painterResource(id = if (timeZoneItem.selected) R.drawable.ic_check_circle else R.drawable.ic_circle),
                contentDescription = "Selector",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary)
            )
        }
    }
}

@Composable
fun TimeZoneListRowView(timeZoneItem: TimesZonesTable, date: Date = Date()) {
    val timeZone = TimeZone.getTimeZone(timeZoneItem.time_id)
    val ampm = dateFormatterAA.apply { setTimeZone(timeZone) }.format(date)
    val time = dateFormatterSimple.apply { setTimeZone(timeZone) }.format(date)
    val day = dateFormatterDay.apply { setTimeZone(timeZone) }.format(date)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {

        Column(modifier = Modifier.weight(1f, fill = true)) {
            Text(
                text = timeZoneItem.name,
                style = MaterialTheme.typography.h5
            )

            Text(
                text = timeZone.id.split("/")[0],
                style = MaterialTheme.typography.caption
            )
        }
        Column(modifier = Modifier.width(100.dp)) {
            Row(modifier = Modifier.align(Alignment.End)) {
                Text(
                    text = time.subSequence(0, 5).toString(),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = ampm.toUpperCase(Locale.current),
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(3.dp, 12.dp, 0.dp, 0.dp)
                )
            }
            /* Text(
                 modifier = Modifier.align(Alignment.End),
                 text = day,
                 style = MaterialTheme.typography.caption
             )*/
        }
    }
}

