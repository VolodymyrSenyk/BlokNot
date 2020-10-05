package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.senyk.volodymyr.bloknot.domain.interactor.DayNightModeSwitchInteractor
import com.senyk.volodymyr.bloknot.presentation.mapper.DayNightModeUiDtoMapper
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import javax.inject.Inject


class DayNightModeViewModel @Inject constructor(
    private val dayNightModeSwitchInteractor: DayNightModeSwitchInteractor,
    private val dayNightModeUiDtoMapper: DayNightModeUiDtoMapper
) : BaseRxViewModel() {

    private val _nightModeEnabled = MutableLiveData<Boolean>()
    val nightModeEnabled: LiveData<Boolean>
        get() = _nightModeEnabled

    private val _currentMode = MutableLiveData<Int>()
    val currentMode: LiveData<Int>
        get() = _currentMode

    init {
        getCurrentMode()
    }

    fun onSwitchModeClick() {
        subscribe(
            upstream = if (_nightModeEnabled.value != true) {
                dayNightModeSwitchInteractor.setNightMode()
            } else {
                dayNightModeSwitchInteractor.setDayMode()
            },
            onComplete = { getCurrentMode() }
        )
    }

    private fun getCurrentMode() {
        subscribe(
            upstream = dayNightModeSwitchInteractor.isNightMode(),
            onSuccess = { nightMode ->
                _nightModeEnabled.value = nightMode
                _currentMode.value = dayNightModeUiDtoMapper(nightMode)
            }
        )
    }
}
