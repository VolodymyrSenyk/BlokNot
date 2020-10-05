package com.senyk.volodymyr.bloknot.presentation.view.activity.base

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.DayNightModeViewModel

abstract class BaseDayNightActivity : BaseActivity() {

    protected val dayNightModeViewModel by viewModels<DayNightModeViewModel>(
        factoryProducer = { viewModelFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayNightModeViewModel.currentMode.observe(this, { appTheme ->
            AppCompatDelegate.setDefaultNightMode(appTheme)
        })
    }
}
