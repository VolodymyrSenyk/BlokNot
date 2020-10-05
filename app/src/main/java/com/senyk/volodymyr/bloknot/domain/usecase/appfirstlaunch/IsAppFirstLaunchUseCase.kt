package com.senyk.volodymyr.bloknot.domain.usecase.appfirstlaunch

import com.senyk.volodymyr.bloknot.domain.repository.AppFirstLaunchRepository
import io.reactivex.Single
import javax.inject.Inject

class IsAppFirstLaunchUseCase @Inject constructor(
    private val appFirstLaunchRepository: AppFirstLaunchRepository
) {

    operator fun invoke(): Single<Boolean> = appFirstLaunchRepository.isFirstLaunch()
}
