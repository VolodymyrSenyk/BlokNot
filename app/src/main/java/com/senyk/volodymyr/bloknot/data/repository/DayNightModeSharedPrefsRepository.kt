package com.senyk.volodymyr.bloknot.data.repository

import android.content.SharedPreferences
import com.senyk.volodymyr.bloknot.domain.repository.DayNightModeRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DayNightModeSharedPrefsRepository @Inject constructor(
    private val preferences: SharedPreferences
) : DayNightModeRepository {

    override fun setDayMode(): Completable = Completable.fromCallable { writeToPrefs(data = false) }

    override fun setNightMode(): Completable =
        Completable.fromCallable { writeToPrefs(data = true) }

    override fun isNightMode(): Single<Boolean> =
        Single.fromCallable {
            preferences.getBoolean(BUNDLE_KEY_APP_THEME, false)
        }

    private fun writeToPrefs(data: Boolean) =
        preferences.edit().apply {
            putBoolean(BUNDLE_KEY_APP_THEME, data)
            apply()
        }

    companion object {
        private const val BUNDLE_KEY_APP_THEME = "BUNDLE_KEY_APP_THEME"
    }
}
