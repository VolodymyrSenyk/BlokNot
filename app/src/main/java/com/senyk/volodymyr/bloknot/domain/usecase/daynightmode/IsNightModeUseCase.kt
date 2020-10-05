package com.senyk.volodymyr.bloknot.domain.usecase.daynightmode

import com.senyk.volodymyr.bloknot.domain.repository.DayNightModeRepository
import io.reactivex.Single
import javax.inject.Inject

class IsNightModeUseCase @Inject constructor(
    private val dayNightModeRepository: DayNightModeRepository
) {

    operator fun invoke(): Single<Boolean> = dayNightModeRepository.isNightMode()
}
