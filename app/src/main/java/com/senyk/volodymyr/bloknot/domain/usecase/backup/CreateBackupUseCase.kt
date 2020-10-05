package com.senyk.volodymyr.bloknot.domain.usecase.backup

import com.google.gson.Gson
import com.senyk.volodymyr.bloknot.domain.repository.BackupRepository
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import com.senyk.volodymyr.bloknot.domain.util.Aes256EncoderDecoder
import io.reactivex.Completable
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class CreateBackupUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val backupRepository: BackupRepository,
    private val encoderDecoder: Aes256EncoderDecoder
) {

    operator fun invoke(
        fileForBackup: File,
        password: String = "",
        secret: Boolean = false
    ): Completable =
        invoke(
            fileForBackup = FileOutputStream(fileForBackup),
            password = password,
            secret = secret
        )

    operator fun invoke(
        fileForBackup: OutputStream,
        password: String = "",
        secret: Boolean = false
    ): Completable =
        notesRepository.getAllOnce(secret = secret)
            .flatMapCompletable { notes ->
                val dataToWrite =
                    encoderDecoder.encode(data = Gson().toJson(notes), password = password)
                backupRepository.create(outputStream = fileForBackup, data = dataToWrite)
            }
}
