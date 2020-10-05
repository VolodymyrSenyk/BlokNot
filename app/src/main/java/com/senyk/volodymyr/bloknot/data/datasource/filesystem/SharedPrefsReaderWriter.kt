package com.senyk.volodymyr.bloknot.data.datasource.filesystem

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SharedPrefsReaderWriter @Inject constructor(private val preferences: SharedPreferences) {

    fun writeInt(bundleKey: String, data: Int): Completable =
        Completable.fromCallable {
            preferences.edit().apply {
                putInt(bundleKey, data)
                apply()
            }
        }

    fun writeLong(bundleKey: String, data: Long): Completable =
        Completable.fromCallable {
            preferences.edit().apply {
                putLong(bundleKey, data)
                apply()
            }
        }

    fun writeFloat(bundleKey: String, data: Float): Completable =
        Completable.fromCallable {
            preferences.edit().apply {
                putFloat(bundleKey, data)
                apply()
            }
        }

    fun writeBoolean(bundleKey: String, data: Boolean): Completable =
        Completable.fromCallable {
            preferences.edit().apply {
                putBoolean(bundleKey, data)
                apply()
            }
        }

    fun writeString(bundleKey: String, data: String): Completable =
        Completable.fromCallable {
            preferences.edit().apply {
                putString(bundleKey, data)
                apply()
            }
        }

    fun writeStringSet(bundleKey: String, data: Set<String>): Completable =
        Completable.fromCallable {
            preferences.edit().apply {
                putStringSet(bundleKey, data)
                apply()
            }
        }

    fun readInt(bundleKey: String, defaultValue: Int): Single<Int> =
        Single.fromCallable { preferences.getInt(bundleKey, defaultValue) }

    fun readLong(bundleKey: String, defaultValue: Long): Single<Long> =
        Single.fromCallable { preferences.getLong(bundleKey, defaultValue) }

    fun readFloat(bundleKey: String, defaultValue: Float): Single<Float> =
        Single.fromCallable { preferences.getFloat(bundleKey, defaultValue) }

    fun readBoolean(bundleKey: String, defaultValue: Boolean): Single<Boolean> =
        Single.fromCallable { preferences.getBoolean(bundleKey, defaultValue) }

    fun readString(bundleKey: String, defaultValue: String): Single<String> =
        Single.fromCallable { preferences.getString(bundleKey, defaultValue) }

    fun readStringSet(bundleKey: String, defaultValue: Set<String>): Single<Set<String>> =
        Single.fromCallable { preferences.getStringSet(bundleKey, defaultValue) }
}
