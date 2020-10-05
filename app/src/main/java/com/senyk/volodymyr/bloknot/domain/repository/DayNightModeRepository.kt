package com.senyk.volodymyr.bloknot.domain.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Single

interface DayNightModeRepository {

    @CheckResult
    fun setDayMode(): Completable

    @CheckResult
    fun setNightMode(): Completable

    @CheckResult
    fun isNightMode(): Single<Boolean>
}
