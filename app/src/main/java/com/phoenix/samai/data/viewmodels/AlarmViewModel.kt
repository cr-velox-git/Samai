package com.phoenix.samai.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.samai.data.local.AlarmTable
import com.phoenix.samai.data.local.AlarmTableDao

import kotlinx.coroutines.launch

class AlarmViewModel(private val db: AlarmTableDao) : ViewModel() {
    fun saveNewAlarm(time: Long, s: String, requestCode: Int) {
        viewModelScope.launch {
            db.insert(AlarmTable(time, s, requestCode))
        }
    }

    val list = db.getAllAlarms()
}