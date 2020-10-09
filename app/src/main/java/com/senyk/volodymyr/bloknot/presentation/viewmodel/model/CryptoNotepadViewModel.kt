package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.interactor.AppFirstLaunchInteractor
import com.senyk.volodymyr.bloknot.presentation.view.dialog.SecretModeEnteringHintDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.util.DoubleClickListener
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.HandledEventLiveData
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.HandledEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateBackEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigateBackToFragmentEvent
import javax.inject.Inject

class CryptoNotepadViewModel @Inject constructor(
    private val appFirstLaunchInteractor: AppFirstLaunchInteractor
) : BaseRxViewModel() {

    private val _firstLaunch = HandledEventLiveData<Boolean>()
    val isFirstLaunch: LiveData<HandledEvent<Boolean>>
        get() = _firstLaunch

    private val clickListener = object : DoubleClickListener() {
        override fun onSingleClick() {
            _navigationEvent.setHandledValue(NavigateBackEvent())
        }

        override fun onDoubleClick() {
            exitCryptoMode()
        }
    }

    private val _inSecretMode = MutableLiveData<Boolean>()
    val isInSecretMode: LiveData<Boolean>
        get() = _inSecretMode

    fun onStart() {
        subscribe(
            upstream = appFirstLaunchInteractor.isFirstLaunch(),
            onSuccess = { isFirstLaunch ->
                _firstLaunch.setHandledValue(isFirstLaunch)
                if (isFirstLaunch) {
                    _dialogFragment.setValue(SecretModeEnteringHintDialogFragment.newInstance())
                }
            }
        )
    }

    fun onSecretModeOn() {
        _inSecretMode.value = true
    }

    fun onSecretModeOff() {
        _inSecretMode.value = false
    }

    fun onApplicationHide() {
        exitCryptoMode()
    }

    fun onBackPressed() {
        clickListener.onClick()
    }

    private fun exitCryptoMode() {
        if (isInSecretMode.value == true) {
            val destination = R.id.notesListFragment
            _navigationEvent.setHandledValue(NavigateBackToFragmentEvent(destination = destination))
        }
    }
}
