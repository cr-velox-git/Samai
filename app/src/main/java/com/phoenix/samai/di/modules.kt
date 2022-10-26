package com.phoenix.samai.di

import com.phoenix.samai.data.viewmodels.AlarmViewModel
import com.phoenix.samai.data.viewmodels.ClockViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var databaseModule = module {
    single { getDb(androidApplication()) }
    single { getTimeZoneDao(get()) }
    single { getAlarmTableDao(get()) }

}
var viewModelModule = module {
    viewModel { ClockViewModel(get()) }
    viewModel { AlarmViewModel(get()) }
}