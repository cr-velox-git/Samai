package com.phoenix.samai.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.phoenix.samai.R


class BroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val des = intent?.getStringExtra("DES")
        val title = intent?.getStringExtra("TITLE")
        val notificationId = intent?.getIntExtra("NOTIFICATION_ID", 123)
        Toast.makeText(context, "Notification Created for alarm $title", Toast.LENGTH_SHORT).show()

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context!!, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(des)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (notificationId != null) {
            notificationManagerCompat.notify(notificationId, builder.build())
            val vibrator = context
                .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(1000)
        }
    }
}