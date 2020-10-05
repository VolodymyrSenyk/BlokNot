package com.senyk.volodymyr.bloknot.domain.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface BackupRepository {

    @CheckResult
    fun create(file: File, data: String): Completable

    @CheckResult
    fun create(outputStream: OutputStream, data: String): Completable

    @CheckResult
    fun get(file: File): Single<String>

    @CheckResult
    fun get(inputStream: InputStream): Single<String>
}
