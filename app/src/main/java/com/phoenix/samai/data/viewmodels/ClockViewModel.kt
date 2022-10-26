package com.phoenix.samai.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.samai.data.local.TimesZonesTable
import com.phoenix.samai.data.local.TimesZonesTableDao
import com.phoenix.samai.utils.getTimeZones
import kotlinx.coroutines.launch

class ClockViewModel(private val db: TimesZonesTableDao) : ViewModel() {


    fun timeZoneList() = db.getAllTimeZones()

    fun selectedTimeZoneList() = db.getSelectedTimeZones()

    suspend fun prePopulateDataBase() {
        if (db.count() == 0) {
            val list = getTimeZones()
            db.insert(list)
        }
    }

    fun updateTimeZone(timesZonesTable: TimesZonesTable) =
        viewModelScope.launch {
            db.update(timesZonesTable.apply {
                selected = !selected
            })
        }

}