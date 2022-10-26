package com.phoenix.samai

import androidx.multidex.MultiDexApplication
import com.phoenix.samai.di.databaseModule
import com.phoenix.samai.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SamaiBaseActivity : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SamaiBaseActivity)
            modules(listOf(databaseModule, viewModelModule))
        }
    }
}