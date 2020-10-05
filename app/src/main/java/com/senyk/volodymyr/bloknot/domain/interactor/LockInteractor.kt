package com.senyk.volodymyr.bloknot.domain.interactor

import com.senyk.volodymyr.bloknot.domain.usecase.lock.CheckPasswordCorrectnessUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.lock.CheckPasswordIsAlreadySetUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.lock.ClearPasswordUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.lock.SetPasswordUseCase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LockInteractor @Inject constructor(
    private val checkPasswordIsAlreadySetUseCase: CheckPasswordIsAlreadySetUseCase,
    private val setPasswordUseCase: SetPasswordUseCase,
    private val checkPasswordCorrectnessUseCase: CheckPasswordCorrectnessUseCase,
    private val clearPasswordUseCase: ClearPasswordUseCase
) {

    fun isPasswordSet(): Single<Boolean> = checkPasswordIsAlreadySetUseCase()

    fun setPassword(password: String): Completable = setPasswordUseCase(password)

    fun isPasswordCorrect(password: String): Single<Boolean> =
        checkPasswordCorrectnessUseCase(password)

    fun clearPassword(): Completable = clearPasswordUseCase()
}
