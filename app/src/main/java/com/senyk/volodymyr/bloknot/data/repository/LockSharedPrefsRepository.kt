package com.senyk.volodymyr.bloknot.data.repository

import com.senyk.volodymyr.bloknot.data.datasource.filesystem.SharedPrefsReaderWriter
import com.senyk.volodymyr.bloknot.domain.repository.LockRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LockSharedPrefsRepository @Inject constructor(
    private val readerWriter: SharedPrefsReaderWriter
) : LockRepository {

    override fun isAlreadySet(): Single<Boolean> =
        readerWriter.readBoolean(
            bundleKey = BUNDLE_KEY_LOCK_ALREADY_SET,
            defaultValue = LOCK_ALREADY_SET_DEFAULT_VALUE
        )

    override fun create(data: String): Completable =
        readerWriter.writeString(
            bundleKey = BUNDLE_KEY_LOCK_PASSWORD,
            data = data
        ).andThen(
            readerWriter.writeBoolean(
                bundleKey = BUNDLE_KEY_LOCK_ALREADY_SET,
                data = true
            )
        )

    override fun get(): Single<String> =
        readerWriter.readString(
            bundleKey = BUNDLE_KEY_LOCK_PASSWORD,
            defaultValue = PASSWORD_DEFAULT_VALUE
        )

    override fun clear(): Completable =
        readerWriter.writeString(
            bundleKey = BUNDLE_KEY_LOCK_PASSWORD,
            data = PASSWORD_DEFAULT_VALUE
        ).andThen(
            readerWriter.writeBoolean(
                bundleKey = BUNDLE_KEY_LOCK_ALREADY_SET,
                data = LOCK_ALREADY_SET_DEFAULT_VALUE
            )
        )

    companion object {
        private const val BUNDLE_KEY_LOCK_ALREADY_SET = "BUNDLE_KEY_LOCK_ALREADY_SET"
        private const val LOCK_ALREADY_SET_DEFAULT_VALUE = false
        private const val BUNDLE_KEY_LOCK_PASSWORD = "BUNDLE_KEY_LOCK_PASSWORD"
        private const val PASSWORD_DEFAULT_VALUE = ""
    }
}
