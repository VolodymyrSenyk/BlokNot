package com.senyk.volodymyr.bloknot.domain.interactor

import com.senyk.volodymyr.bloknot.domain.usecase.backup.ApplyBackupUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.backup.CreateBackupUseCase
import io.reactivex.Completable
import java.io.*
import javax.inject.Inject

class BackupInteractor @Inject constructor(
    private val applyBackupUseCase: ApplyBackupUseCase,
    private val createBackupUseCase: CreateBackupUseCase
) {

    fun createBackup(file: File, password: String = "", secret: Boolean = false): Completable =
        createBackup(file = FileOutputStream(file), password = password, secret = secret)

    fun createBackup(
        file: OutputStream,
        password: String = "",
        secret: Boolean = false
    ): Completable =
        createBackupUseCase(fileForBackup = file, password = password, secret = secret)

    fun applyBackup(file: File, password: String = ""): Completable =
        applyBackup(file = FileInputStream(file), password = password)

    fun applyBackup(file: InputStream, password: String = ""): Completable =
        applyBackupUseCase(backupFile = file, password = password)
}
