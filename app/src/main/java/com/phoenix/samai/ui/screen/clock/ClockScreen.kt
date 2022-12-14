package com.phoenix.samai.ui.screen.clock

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phoenix.samai.R
import com.phoenix.samai.data.viewmodels.ClockViewModel
import com.phoenix.samai.ui.screen.Destinations
import com.phoenix.samai.utils.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun ClockScreen(navController: NavController) {
    val viewModel by KoinJavaComponent.inject<ClockViewModel>(ClockViewModel::class.java)
    val list = viewModel.selectedTimeZoneList().collectAsState(initial = emptyList())

    Container("World Clock") {
        val timeZone = TimeZone.getDefault()
        val subHeading = "${timeZone.id} ${
            SimpleDateFormat(DATE_FORMAT_FULL).apply { setTimeZone(timeZone) }.format(Date())
        }"
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                content = {
                    item {
                        ClockView(timeZone)
                    }
                    items(list.value) { item ->
                        TimeZoneListRowView(item)
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth()
                        )
                    }

                }, modifier = Modifier
                    .fillMaxSize()
            )
            FloatingActionButton(
                onClick = {
                    navController.navigate(Destinations.TIMEZONE_ROUTE)
                },
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "generic image",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}

@Composable
fun Container(heading: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.h3,
        )
        Spacer(modifier = Modifier.height(12.dp))
        content.invoke(this)
    }
}


//clock
@Composable
@Preview
fun previewClock() {
    ClockView(timeZone = TimeZone.getDefault())
}

@Composable
fun ClockView(timeZone: TimeZone) {
    val infiniteTransition = rememberInfiniteTransition()

    val hand = infiniteTransition.animateValue(
        initialValue = 0f,
        targetValue = 1000f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            hands(hand.value, timeZone, currentClockType())
            staticUi()
        }
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        val sec = Calendar.getInstance(timeZone).get(Calendar.SECOND)
        val min = Calendar.getInstance(timeZone).get(Calendar.MINUTE)
        val hour = Calendar.getInstance(timeZone).get(Calendar.HOUR_OF_DAY)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "$hour : $min : $sec",
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            color = MaterialTheme.colors.primary,
            fontStyle = FontStyle.Normal
        )
        val day = Calendar.getInstance(timeZone).get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance(timeZone).get(Calendar.MONTH)
        val year = Calendar.getInstance(timeZone).get(Calendar.YEAR)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "$day/$month/$year",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = MaterialTheme.colors.primary,
            fontStyle = FontStyle.Normal
        )
        Spacer(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun hands(unused: Float, timeZone: TimeZone, clockType: ClockType) {

    val cal = Calendar.getInstance(timeZone)
    val color = MaterialTheme.colors.primary
    val colorSecondary: Color = MaterialTheme.colors.secondary
    Canvas(modifier = Modifier.fillMaxSize()) {
        val progression = ((cal.timeInMillis % 1000) / 1000.0)
        // Log.e("fl", " -> $fl   $progression")


        val centerX = (size.width / 2)
        val centerY = (size.height / 2)

        val sec = cal.get(Calendar.SECOND)
        val min = cal.get(Calendar.MINUTE)
        var hour = cal.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour

        val animatedSecond = sec + progression
        val animatedMinute = min + animatedSecond / 60
        val animatedHour = (hour + (animatedMinute / 60)) * 5f
        if (clockType == ClockType.CLOCK_ONE) {

            minuteHand(centerX, centerY, size.getRadius(0.6f), animatedMinute, color)
            hourHand(centerX, centerY, size.getRadius(0.45f), animatedHour, colorSecondary)
            secondHand(centerX, centerY, size.getRadius(0.7f), animatedSecond, color)
        } else {
            hourHand2(centerX, centerY, size.getRadius(0.45f), animatedHour, color)
            minuteHand2(centerX, centerY, size.getRadius(0.6f), animatedMinute, color)
        }
    }
}

fun DrawScope.secondHand(
    centerX: Float,
    centerY: Float,
    clockRadius: Float,
    animatedSecond: Double,
    color: Color
) {
    val degree = animatedSecond * oneMinuteRadians - pieByTwo
    val x = centerX + cos(degree) * clockRadius
    val y = centerY + sin(degree) * clockRadius
    //val minusx = centerX + cos(degree) * -30
    //val minusy = centerY + sin(degree) * -30
    drawLine(
        //start = Offset(minusx.toFloat(), minusy.toFloat()),
        start = Offset(centerX, centerY),
        end = Offset(x.toFloat(), y.toFloat()),
        color = color,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
}

fun DrawScope.minuteHand(
    centerX: Float,
    centerY: Float,
    clockRadius: Float,
    animatedMinute: Double,
    color: Color
) {
    val degree = animatedMinute * oneMinuteRadians - pieByTwo
    val x = centerX + cos(degree) * clockRadius
    val y = centerY + sin(degree) * clockRadius
    drawLine(
        start = Offset(centerX, centerY),
        end = Offset(x.toFloat(), y.toFloat()),
        color = color,
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )
}

fun DrawScope.hourHand(
    centerX: Float, centerY: Float, clockRadius: Float, animatedHour: Double,
    color: Color
) {
    val degree = animatedHour * oneMinuteRadians - pieByTwo
    val x = centerX + cos(degree) * clockRadius
    val y = centerY + sin(degree) * clockRadius
    drawLine(
        start = Offset(centerX, centerY),
        end = Offset(x.toFloat(), y.toFloat()),
        color = color,
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )
}

@Preview
@Composable
fun staticUi() {
    val color = MaterialTheme.colors.primary
    val color2 = MaterialTheme.colors.background
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = color,
            radius = 15f
        )
        drawCircle(
            color = color2,
            radius = 7f
        )
        val centerX = (size.width / 2)
        val centerY = (size.height / 2)
        val clockRadius = size.getRadius(0.8f)


        for (i in listOf(0.0, Math.PI / 2, Math.PI, (3 * Math.PI) / 2)) {
            val degree = i.toFloat()
            drawLine(
                color = color,
                start = Offset(
                    centerX + cos(degree) * (clockRadius - 20),
                    centerY + sin(degree) * (clockRadius - 20)
                ),
                end = Offset(
                    centerX + cos(degree) * (clockRadius - 2),
                    centerY + sin(degree) * (clockRadius - 2)
                ),
                strokeWidth = 10f,
                cap = StrokeCap.Round
            )
        }


        drawCircle(
            color = color,
            radius = size.getRadius(0.8f),
            style = Stroke(7f)
        )

    }
}

//@Composable
//fun drawHourLabel() {
//
//}

// second clock UI methods
fun DrawScope.minuteHand2(
    centerX: Float,
    centerY: Float,
    clockRadius: Float,
    animatedMinute: Double,
    color: Color
) {
    val degree = animatedMinute * oneMinuteRadians - pieByTwo
    val x = centerX + cos(degree) * clockRadius
    val y = centerY + sin(degree) * clockRadius
    val minusx = centerX + cos(degree) * -30
    val minusy = centerY + sin(degree) * -30
    drawLine(
        start = Offset(minusx.toFloat(), minusy.toFloat()),
        end = Offset(x.toFloat(), y.toFloat()),
        color = color,
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )
}

fun DrawScope.hourHand2(
    centerX: Float, centerY: Float, clockRadius: Float, animatedHour: Double,
    color: Color
) {
    val degree = animatedHour * oneMinuteRadians - pieByTwo
    val x = centerX + cos(degree) * clockRadius
    val y = centerY + sin(degree) * clockRadius
    val minusx = centerX + cos(degree) * -30
    val minusy = centerY + sin(degree) * -30
    drawLine(
        start = Offset(minusx.toFloat(), minusy.toFloat()),
        end = Offset(x.toFloat(), y.toFloat()),
        color = color,
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )
}


//clock type
enum class ClockType {
    CLOCK_ONE, CLOCK_TWO
}


fun Context.getClockType() = dataStore.data
    .map { preferences ->
        // No type safety.
        preferences[clockPreferenceKey] ?: 0
    }


@Composable
fun currentClockType(): ClockType {

    val context = LocalContext.current
    val preferences = runBlocking { context.dataStore.data.first() }
    val theme =
        context.getClockType().collectAsState(initial = preferences[clockPreferenceKey] ?: 0)
    return when (theme.value) {
        0 -> ClockType.CLOCK_ONE
        else -> ClockType.CLOCK_TWO
    }
}

fun TimeZone.offset(): String {
    val calendar: Calendar = Calendar.getInstance(this, Locale.getDefault())
    val timeZone: String = SimpleDateFormat("Z").format(calendar.time)
    return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5)
}