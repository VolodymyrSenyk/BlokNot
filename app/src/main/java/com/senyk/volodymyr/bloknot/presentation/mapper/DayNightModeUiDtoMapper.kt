package com.senyk.volodymyr.bloknot.presentation.mapper

import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

class DayNightModeUiDtoMapper @Inject constructor() {

    operator fun invoke(nightMode: Boolean): Int = if (nightMode) NIGHT_MODE else DAY_MODE

    companion object {
        private const val DAY_MODE = AppCompatDelegate.MODE_NIGHT_NO
        private const val NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_YES
    }
}
