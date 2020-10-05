package com.senyk.volodymyr.bloknot.domain.usecase.lock

import com.senyk.volodymyr.bloknot.domain.repository.LockRepository
import com.senyk.volodymyr.bloknot.domain.util.CheckSumCalculator
import io.reactivex.Single
import javax.inject.Inject

class CheckPasswordCorrectnessUseCase @Inject constructor(
    private val lockRepository: LockRepository,
    private val checkSumCalculator: CheckSumCalculator
) {

    operator fun invoke(input: String): Single<Boolean> =
        lockRepository.get().map { checkSum -> checkSumCalculator(input) == checkSum }
}
