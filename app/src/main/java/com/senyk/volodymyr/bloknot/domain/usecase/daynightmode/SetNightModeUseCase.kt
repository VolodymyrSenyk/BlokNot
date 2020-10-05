package com.senyk.volodymyr.bloknot.domain.usecase.daynightmode

import com.senyk.volodymyr.bloknot.domain.repository.DayNightModeRepository
import io.reactivex.Completable
import javax.inject.Inject

class SetNightModeUseCase @Inject constructor(
    private val dayNightModeRepository: DayNightModeRepository
) {

    operator fun invoke(): Completable = dayNightModeRepository.setNightMode()
}
