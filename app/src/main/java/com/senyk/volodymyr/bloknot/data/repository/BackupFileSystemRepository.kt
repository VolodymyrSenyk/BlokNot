package com.senyk.volodymyr.bloknot.data.repository

import com.senyk.volodymyr.bloknot.data.datasource.filesystem.FileReaderWriter
import com.senyk.volodymyr.bloknot.domain.repository.BackupRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class BackupFileSystemRepository @Inject constructor(
    private val readerWriter: FileReaderWriter
) : BackupRepository {

    override fun create(file: File, data: String): Completable =
        readerWriter.write(file = file, data = data)

    override fun create(outputStream: OutputStream, data: String): Completable =
        readerWriter.write(outputStream = outputStream, data = data)

    override fun get(file: File): Single<String> = readerWriter.read(file = file)

    override fun get(inputStream: InputStream): Single<String> =
        readerWriter.read(inputStream = inputStream)
}
