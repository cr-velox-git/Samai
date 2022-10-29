package com.phoenix.samai.data.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel


class TimerViewModel : ViewModel() {


    fun runTimer(
        sec: Float ? = null,
        progress: MutableState<Float> ? = null,
        timeRemains: MutableState<String> ? = null,
        finalFunction: () -> Unit
    )  = object : CountDownTimer((sec?.times(1000))?.toLong()!!, 1000) {
            var t = sec
            override fun onTick(millisUntilFinished: Long) {
                Log.i("timer","running..")
                t = t?.minus(1)
                progress?.value = (100f / sec!!) * t!!

                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60


                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli
                timeRemains?.value = "$elapsedHours:$elapsedMinutes:$elapsedSeconds"

            }

            override fun onFinish() {
                finalFunction.invoke()
            }
        }

    }

