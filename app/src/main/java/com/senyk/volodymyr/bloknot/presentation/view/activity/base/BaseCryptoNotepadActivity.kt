package com.senyk.volodymyr.bloknot.presentation.view.activity.base

import android.os.Bundle
import androidx.activity.viewModels
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.CryptoNotepadViewModel

abstract class BaseCryptoNotepadActivity : BaseDayNightActivity() {

    protected val cryptoNotepadViewModel by viewModels<CryptoNotepadViewModel>(
        factoryProducer = { viewModelFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayNightModeViewModel.currentMode.observe(this, {
            cryptoNotepadViewModel.onSecretModeOff()
        })
        cryptoNotepadViewModel.onStart()
    }

    override fun onBackPressed() {
        cryptoNotepadViewModel.onBackPressed()
    }
}
