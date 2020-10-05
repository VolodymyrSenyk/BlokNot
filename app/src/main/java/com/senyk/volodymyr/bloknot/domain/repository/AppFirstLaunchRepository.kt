package com.senyk.volodymyr.bloknot.domain.repository

import androidx.annotation.CheckResult
import io.reactivex.Single

interface AppFirstLaunchRepository {

    @CheckResult
    fun isFirstLaunch(): Single<Boolean>
}
