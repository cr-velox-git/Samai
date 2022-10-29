package com.phoenix.samai.ui.screen.timer

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
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
import com.phoenix.samai.ui.components.Progress
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
        /**
         * creating notification object
         * */
        val mWakeLock =
            (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, javaClass.name)

        val hour = remember { mutableStateOf("") }
        val pMin = remember { mutableStateOf("") }
        val sMin = remember { mutableStateOf("") }
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
                        btn.value = "RUNNING..."


                        setDuration.value = false
                        val pmin = pMin.value.toFloat() * 60
                        val smin = sMin.value.toFloat() * 60
                        val _hour = if (hour.value != "" && hour.value.toFloat() != 0f) {
                            hour.value.toFloat() * 60 * 60
                        } else {
                            (pmin + smin)
                        }

                        count.value = (_hour / (pmin + smin)).toInt()
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
                            pmin,
                            smin,
                            btn
                        )


                    } else {
                        Toast
                            .makeText(context, "focus on your work", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        )

        Spacer(modifier = Modifier.weight(1f))
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
    pmin: Float,
    smin: Float,
    btn: MutableState<String>,
) {
    if (count.value > 0) {

        /**
         * Disable touch input
         * */
        (context as Activity).window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        mWakeLock.acquire()
        pProgress.value = 100f
        sProgress.value = 100f
        viewModel.runTimer(pmin, pProgress, pTimer) {
            playSound(context)
            viewModel.runTimer(smin, sProgress, sTimer) {
                pProgress.value = 100f
                sProgress.value = 100f
                playSound(context)
                count.value -= 1
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
                    pmin,
                    smin,
                    btn
                )
            }.start()
        }.start()
    } else {
        setDuration.value = true
        mWakeLock.release()
        btn.value = "START"
        /**
         * Enable touch input
         * */
        (context as Activity).window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

fun playSound(context: Context) {
    val player = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI)
    player.start()
}

