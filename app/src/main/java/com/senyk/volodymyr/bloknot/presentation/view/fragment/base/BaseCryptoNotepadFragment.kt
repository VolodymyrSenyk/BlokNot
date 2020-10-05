package com.senyk.volodymyr.bloknot.presentation.view.fragment.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.CryptoNotepadViewModel

abstract class BaseCryptoNotepadFragment : BaseDayNightFragment() {

    protected val cryptoNotepadViewModel by activityViewModels<CryptoNotepadViewModel>(
        factoryProducer = { viewModelFactory }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBaseObservers(cryptoNotepadViewModel)
    }

    override fun onResume() {
        super.onResume()
        cryptoNotepadViewModel.onSecretModeOff()
    }
}
