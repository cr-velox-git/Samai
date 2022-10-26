package com.phoenix.samai.ui.screen.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.phoenix.samai.data.viewmodels.AlarmViewModel
import org.koin.java.KoinJavaComponent.inject
import java.util.*

@Preview
@Composable
fun CreateAlarmScreen(
//    navController: NavController
) {
    val viewModel by inject<AlarmViewModel>(AlarmViewModel::class.java)
    val context = LocalContext.current
    val c: Calendar = Calendar.getInstance()
    val hour: Int = c.get(Calendar.HOUR_OF_DAY)
    val minute: Int = c.get(Calendar.MINUTE)
    val alarmManager = context.getSystemService(ALARM_SERVICE) as? AlarmManager
    val time = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        time.value = "$hour:$minute"

        TextButton(onClick = {
            time.value = "$hour:$minute"
            TimePickerDialog(
                context,
                // TODO:  theme needs to be created
                //  R.style.CalenderViewCustom,
                { _, hourOfDay, min ->
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    c.set(Calendar.MINUTE, min)
                    time.value = "$hourOfDay:$min"
                },
                hour,
                minute,
                false
            ).show()
        }) {
            Text(
                text = time.value,
                style = MaterialTheme.typography.h3,
            )

        }

        TextButton(onClick = {

            Toast.makeText(context, "text button clicked", Toast.LENGTH_SHORT).show()
            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver

            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver
            val intent = Intent(context, AlarmReceiver::class.java)

            // we call broadcast using pendingIntent

            // we call broadcast using pendingIntent
            val requestCode = 991
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, FLAG_MUTABLE)

            var time = c.timeInMillis - c.timeInMillis % 60000
            Log.e(
                "compare",
                "${System.currentTimeMillis()}  $time     difference = ${System.currentTimeMillis()}"
            )
            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                time += if (Calendar.AM_PM === 0) 1000 * 60 * 60 * 12 else 1000 * 60 * 60 * 24
            }
            viewModel.saveNewAlarm(c.time.time, "desc", requestCode)
            // Alarm rings continuously until toggle button is turned off
            // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent)
            alarmManager?.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)

            // alarmManager.cancel(pendingIntent);
            Log.e("alarmManager", "${alarmManager?.nextAlarmClock}")
            setAlarm()
        }) {
            Text(
                text = "Set Alarm",
                style = MaterialTheme.typography.h3,
            )

        }
    }
}

fun setAlarm(){}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
//        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show()
        var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        // setting default ringtone
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)

        // play ringtone
        ringtone.play()

    }
}



