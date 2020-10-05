package com.senyk.volodymyr.bloknot.domain.usecase.lock

import com.senyk.volodymyr.bloknot.domain.repository.LockRepository
import com.senyk.volodymyr.bloknot.domain.util.CheckSumCalculator
import io.reactivex.Completable
import javax.inject.Inject

class SetPasswordUseCase @Inject constructor(
    private val lockRepository: LockRepository,
    private val checkSumCalculator: CheckSumCalculator
) {

    operator fun invoke(password: String): Completable =
        lockRepository.create(checkSumCalculator(password))
}
