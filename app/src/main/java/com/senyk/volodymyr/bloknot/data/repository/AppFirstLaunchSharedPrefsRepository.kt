package com.senyk.volodymyr.bloknot.data.repository

import com.senyk.volodymyr.bloknot.data.datasource.filesystem.SharedPrefsReaderWriter
import com.senyk.volodymyr.bloknot.domain.repository.AppFirstLaunchRepository
import io.reactivex.Single
import javax.inject.Inject

class AppFirstLaunchSharedPrefsRepository @Inject constructor(
    private val readerWriter: SharedPrefsReaderWriter
) : AppFirstLaunchRepository {

    override fun isFirstLaunch(): Single<Boolean> =
        readerWriter.readBoolean(
            bundleKey = BUNDLE_KEY_APP_ALREADY_LAUNCHED,
            defaultValue = APP_ALREADY_LAUNCHED_DEFAULT_VALUE
        ).flatMap { isAlreadyLaunched ->
            readerWriter.writeBoolean(
                bundleKey = BUNDLE_KEY_APP_ALREADY_LAUNCHED,
                data = true
            ).andThen(Single.fromCallable { !isAlreadyLaunched })
        }

    companion object {
        private const val BUNDLE_KEY_APP_ALREADY_LAUNCHED = "BUNDLE_KEY_APP_ALREADY_LAUNCHED"
        private const val APP_ALREADY_LAUNCHED_DEFAULT_VALUE = false
    }
}
