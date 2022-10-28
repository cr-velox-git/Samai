package com.phoenix.samai.ui.screen.timer

import android.content.Context
import android.media.MediaPlayer
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoenix.samai.R
import com.phoenix.samai.data.viewmodels.TimerViewModel
import org.koin.java.KoinJavaComponent


@Composable
fun TimerScreen(context: Context, navigateUp: () -> Unit) {
    timeScreen(context)
}

@Composable
fun timeScreen(context: Context) {
    val viewModel by KoinJavaComponent.inject<TimerViewModel>(TimerViewModel::class.java)
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        val mWakeLock =
            (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, javaClass.name)
        val hour = remember { mutableStateOf("0") }
        val pMin = remember { mutableStateOf("0") }
        val sMin = remember { mutableStateOf("0") }
        val pProgress = remember { mutableStateOf(100f) }
        val pTimer = remember { mutableStateOf("0:0:0") }
        val sProgress = remember { mutableStateOf(100f) }
        val sTimer = remember { mutableStateOf("0:0:0") }
        val count = remember { mutableStateOf(1) }
        val btn = remember { mutableStateOf("START") }

        val setDuration = remember { mutableStateOf(true) }
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Timer", fontSize = 25.sp)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.ic_baseline_more_vert_24),
                contentDescription = stringResource(
                    id = R.string.image_desc
                ),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                alignment = CenterEnd
            )

        }


        Box(
            modifier = Modifier
                .width(500.dp)
                .height(500.dp)
        ) {


            Progress(
                modifier = Modifier
                    .padding(16.dp),
                pProgress.value,
                sProgress.value
            )
            Column(modifier = Modifier.align(Center)) {
                Text(
                    text = pTimer.value,
                    fontSize = 40.sp,
                )
                Text(
                    text = sTimer.value,
                    fontSize = 20.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
                Text(
                    text = count.value.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
            }
        }
        if (setDuration.value) {
            SetDuration(hour, pMin, sMin)
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = btn.value,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary, RoundedCornerShape(8.dp))
                .padding(16.dp)
                .clickable {

                    if (pMin.value != "" && sMin.value != "" && btn.value == "START") {
                        btn.value = "STOP"

// screen stays on in this section
// screen stays on in this section

                        setDuration.value = false
                        val _hour = hour.value.toFloat() * 60 * 60
                        val pmin = pMin.value.toFloat() * 60
                        val smin = sMin.value.toFloat() * 60

                        val n = _hour / (pmin + smin)
                        startProgress(
                            context,
                            mWakeLock,
                            setDuration,
                            pProgress,
                            sProgress,
                            viewModel,
                            pTimer,
                            sTimer,
                            count,
                            n,
                            pmin,
                            smin
                        )


                    }
                }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

fun startProgress(
    context: Context,
    mWakeLock: PowerManager.WakeLock,
    setDuration: MutableState<Boolean>,
    pProgress: MutableState<Float>,
    sProgress: MutableState<Float>,
    viewModel: TimerViewModel,
    pTimer: MutableState<String>,
    sTimer: MutableState<String>,
    count: MutableState<Int>,
    n: Float,
    pmin: Float,
    smin: Float
) {
    if (count.value < n) {
        mWakeLock.acquire()
        pProgress.value = 100f
        sProgress.value = 100f
        viewModel.runTimer(pmin, pProgress, pTimer) {
            playSound(context)
            viewModel.runTimer(smin, sProgress, sTimer) {
                pProgress.value = 100f
                sProgress.value = 100f
                playSound(context)
                count.value += 1
                startProgress(
                    context,
                    mWakeLock,
                    setDuration,
                    pProgress,
                    sProgress,
                    viewModel,
                    pTimer,
                    sTimer,
                    count,
                    n,
                    pmin,
                    smin
                )
            }.start()
        }.start()
    } else {
        setDuration.value = true
        mWakeLock.release()
    }
}

fun playSound(context: Context) {
    val player = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI)
    player.start()
}

@Composable
fun Progress(modifier: Modifier, pProgress: Float, sProgress: Float) {
//    val size = 300
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
                brush = Brush.linearGradient(listOf(Color.White, Color.White)),
                useCenter = true,
                size = Size(width - thickness, width - thickness),
                startAngle = 0f,
                sweepAngle = 360f
            )
        })
    }
}

@Composable
fun SetDuration(
    hour: MutableState<String>,
    pMin: MutableState<String>,
    sMin: MutableState<String>
) {
    Column(modifier = Modifier) {
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.secondary,
                    unfocusedBorderColor = MaterialTheme.colors.secondary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = pMin.value,
                singleLine = true,
                onValueChange = {
                    pMin.value = it
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.onPrimary)
                    .weight(1f),
            )
            Text(text = " / ", Modifier.align(CenterVertically), fontSize = 40.sp)
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.primary
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = sMin.value,
                singleLine = true,
                onValueChange = {
                    sMin.value = it
                },
                modifier = Modifier
                    .background(MaterialTheme.colors.onPrimary)
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = " min ", Modifier.align(CenterVertically), fontSize = 10.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = " min ", Modifier.align(CenterVertically), fontSize = 10.sp)
            Spacer(modifier = Modifier.weight(1f))
        }

        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.primary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = hour.value,
            singleLine = true,
            onValueChange = {
                hour.value = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(100.dp)
                .background(MaterialTheme.colors.onPrimary),
        )
        Text(text = " hour ", Modifier.align(Alignment.CenterHorizontally), fontSize = 10.sp)
    }
}