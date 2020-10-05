package com.senyk.volodymyr.bloknot.domain.usecase.lock

import com.senyk.volodymyr.bloknot.domain.repository.LockRepository
import io.reactivex.Single
import javax.inject.Inject

class CheckPasswordIsAlreadySetUseCase @Inject constructor(
    private val lockRepository: LockRepository
) {

    operator fun invoke(): Single<Boolean> = lockRepository.isAlreadySet()
}
