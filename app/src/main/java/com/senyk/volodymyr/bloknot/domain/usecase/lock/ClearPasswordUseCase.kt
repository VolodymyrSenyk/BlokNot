package com.senyk.volodymyr.bloknot.domain.usecase.lock

import com.senyk.volodymyr.bloknot.domain.repository.LockRepository
import io.reactivex.Completable
import javax.inject.Inject

class ClearPasswordUseCase @Inject constructor(
    private val lockRepository: LockRepository
) {

    operator fun invoke(): Completable = lockRepository.clear()
}
