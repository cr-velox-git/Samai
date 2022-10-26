package com.phoenix.samai.di

import android.content.Context
import androidx.room.Room
import com.phoenix.samai.R
import com.phoenix.samai.data.local.AlarmTableDao
import com.phoenix.samai.data.local.AppDatabase
import com.phoenix.samai.data.local.TimesZonesTableDao


fun getDb(context: Context): AppDatabase {
    return synchronized(context) {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-${context.getString(R.string.app_name).lowercase()}"
        ).build()
    }
}

fun getTimeZoneDao(appDatabase: AppDatabase): TimesZonesTableDao {
    return appDatabase.timeZoneDao()
}

fun getAlarmTableDao(appDatabase: AppDatabase): AlarmTableDao {
    return appDatabase.alarmTableDao()
}