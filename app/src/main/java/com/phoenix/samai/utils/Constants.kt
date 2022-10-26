package com.phoenix.samai.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.text.SimpleDateFormat



// Preference Constants
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val themePreferenceKey = intPreferencesKey("list_theme")
val clockPreferenceKey = intPreferencesKey("clock_type")

const val DATE_FORMAT_AA = "aa"
const val DATE_FORMAT_TIME = "hh:mm aa"
const val DATE_FORMAT_DAY_DATE = "EEE , dd/MM"
const val DATE_FORMAT_FULL = "hh:mm aa, EEE , dd/MM"


val dateFormatterAA = SimpleDateFormat(DATE_FORMAT_AA)
val dateFormatterSimple = SimpleDateFormat(DATE_FORMAT_TIME)
val dateFormatterDay = SimpleDateFormat(DATE_FORMAT_FULL)
