package com.senyk.volodymyr.bloknot.presentation.view.fragment.base

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.CryptoNotepadViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigationEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.StartActivityForResultEvent
import kotlinx.android.synthetic.main.fragment_curtains.*

abstract class BaseCryptoNotepadCryptoFragment : BaseDayNightFragment() {

    protected val cryptoNotepadViewModel by activityViewModels<CryptoNotepadViewModel>(
        factoryProducer = { viewModelFactory }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBaseObservers(cryptoNotepadViewModel)
    }

    override fun onResume() {
        super.onResume()
        curtainsView?.isGone = true
        cryptoNotepadViewModel.onSecretModeOn()
    }

    override fun onPause() {
        curtainsView?.isVisible = true
        super.onPause()
    }

    override fun handleNavigationEvent(event: NavigationEvent?) {
        if (event is StartActivityForResultEvent) {
            cryptoNotepadViewModel.onSecretModeOff()
        }
        super.handleNavigationEvent(event)
    }
}
