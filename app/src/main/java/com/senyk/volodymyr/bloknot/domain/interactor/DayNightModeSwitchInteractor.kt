package com.senyk.volodymyr.bloknot.domain.interactor

import com.senyk.volodymyr.bloknot.domain.usecase.daynightmode.IsNightModeUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.daynightmode.SetDayModeUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.daynightmode.SetNightModeUseCase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DayNightModeSwitchInteractor @Inject constructor(
    private val setDayModeUseCase: SetDayModeUseCase,
    private val setNightModeUseCase: SetNightModeUseCase,
    private val isNightModeUseCase: IsNightModeUseCase
) {

    fun setDayMode(): Completable = setDayModeUseCase()

    fun setNightMode(): Completable = setNightModeUseCase()

    fun isNightMode(): Single<Boolean> = isNightModeUseCase()
}
