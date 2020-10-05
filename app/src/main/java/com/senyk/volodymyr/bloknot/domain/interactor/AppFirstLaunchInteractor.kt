package com.senyk.volodymyr.bloknot.domain.interactor

import com.senyk.volodymyr.bloknot.domain.usecase.appfirstlaunch.IsAppFirstLaunchUseCase
import io.reactivex.Single
import javax.inject.Inject

class AppFirstLaunchInteractor @Inject constructor(
    private val isAppFirstLaunchUseCase: IsAppFirstLaunchUseCase
) {

    fun isFirstLaunch(): Single<Boolean> = isAppFirstLaunchUseCase()
}
