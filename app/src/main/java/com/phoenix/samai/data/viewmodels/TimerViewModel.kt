package com.phoenix.samai.data.viewmodels

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel


class TimerViewModel : ViewModel() {


    fun runTimer(
        sec: Float,
        progress: MutableState<Float>,
        timeRemains: MutableState<String>,
        finalFunction: () -> Unit
    )  = object : CountDownTimer((sec * 1000).toLong(), 1000) {
            var t = sec
            override fun onTick(millisUntilFinished: Long) {
                t -= 1
                progress.value = (100f / sec) * t

                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60


                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli
                timeRemains.value = "$elapsedHours:$elapsedMinutes:$elapsedSeconds"

            }

            override fun onFinish() {
                finalFunction.invoke()
            }
        }

    }

