package com.senyk.volodymyr.bloknot.domain.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Single

interface LockRepository {

    @CheckResult
    fun isAlreadySet(): Single<Boolean>

    @CheckResult
    fun create(data: String): Completable

    @CheckResult
    fun get(): Single<String>

    @CheckResult
    fun clear(): Completable
}
