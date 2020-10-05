package com.senyk.volodymyr.bloknot.presentation.viewmodel.base

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.InfoWithAction
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.NavigationEventLiveData
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.SingleEventLiveData
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.HandledEvent
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.NavigationEvent

abstract class BaseViewModel : ViewModel() {

    protected val tag: String = this.javaClass.simpleName

    protected var _toastMessage = SingleEventLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    protected var _snackbarMessage = SingleEventLiveData<String>()
    val snackbarMessage: LiveData<String>
        get() = _snackbarMessage

    protected var _snackbarMessageWithAction = SingleEventLiveData<InfoWithAction>()
    val snackbarMessageWithAction: LiveData<InfoWithAction>
        get() = _snackbarMessageWithAction

    protected var _dialogFragment = SingleEventLiveData<DialogFragment>()
    val dialogFragment: LiveData<DialogFragment>
        get() = _dialogFragment

    protected var _showProgress = SingleEventLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    protected var _navigationEvent = NavigationEventLiveData()
    val navigationEvent: LiveData<HandledEvent<NavigationEvent>>
        get() = _navigationEvent

    protected open fun onError(throwable: Throwable) {
        hideProgress()
        Log.e(tag, "An error occurred: ${throwable.message}", throwable)
    }

    protected open fun showProgress() {
        _showProgress.setValue(true)
    }

    protected open fun hideProgress() {
        _showProgress.setValue(false)
    }
}
