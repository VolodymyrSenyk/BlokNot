package com.senyk.volodymyr.bloknot.data.datasource.filesystem

import io.reactivex.Completable
import io.reactivex.Single
import java.io.*
import javax.inject.Inject

class FileReaderWriter @Inject constructor() {

    fun write(file: File, data: String): Completable =
        write(outputStream = FileOutputStream(file), data = data)

    fun write(outputStream: OutputStream, data: String): Completable =
        Completable.fromCallable {
            outputStream.apply {
                write(data.toByteArray())
                close()
            }
        }

    fun read(file: File): Single<String> = read(FileInputStream(file))

    fun read(inputStream: InputStream): Single<String> =
        Single.fromCallable {
            val data = StringBuilder()
            var byte: Int
            while (inputStream.read().also { byte = it } != -1) {
                data.append(byte.toChar())
            }
            inputStream.close()
            String(data)
        }
}
