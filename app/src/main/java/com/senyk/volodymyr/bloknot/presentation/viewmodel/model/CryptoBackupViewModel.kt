package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.exception.WrongPasswordException
import com.senyk.volodymyr.bloknot.domain.usecase.backup.ApplyBackupUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.backup.CreateBackupUseCase
import com.senyk.volodymyr.bloknot.presentation.view.dialog.EnterPasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.view.dialog.NoBackupPasswordDialogFragment
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.DateFormatterUtil
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

open class CryptoBackupViewModel @Inject constructor(
    createBackupUseCase: CreateBackupUseCase,
    applyBackupUseCase: ApplyBackupUseCase,
    resourcesProvider: ResourcesProvider,
) : BackupViewModel(
    createBackupUseCase,
    applyBackupUseCase,
    resourcesProvider
) {

    private var backupFile: InputStream? = null
    private var fileForBackup: OutputStream? = null
    protected var passwordRequestInProgress = false

    override fun onFileForBackupSelected(outputStream: OutputStream) {
        fileForBackup = outputStream
        if (backupCreatingInProgress || backupApplyingInProgress) {
            requestPassword()
        }
    }

    override fun onBackupFileSelected(inputStream: InputStream) {
        backupFile = inputStream
        if (backupCreatingInProgress || backupApplyingInProgress) {
            requestPassword()
        }
    }

    private fun requestPassword() {
        passwordRequestInProgress = true
        _dialogFragment.setValue(EnterPasswordDialogFragment.newInstance())
    }

    fun onPasswordReturned(password: String) {
        passwordRequestInProgress = false
        if (backupCreatingInProgress) {
            if (password.isEmpty()) {
                _dialogFragment.setValue(NoBackupPasswordDialogFragment.newInstance())
                return
            }
            fileForBackup?.let { file ->
                createBackup(fileForBackup = file, password = password)
            }
        } else if (backupApplyingInProgress) {
            backupFile?.let { file ->
                applyBackup(backupFile = file, password = password)
            }
        }
    }

    fun onCancelEmptyBackupPasswordClick() {
        passwordRequestInProgress = true
        _dialogFragment.setValue(EnterPasswordDialogFragment.newInstance())
    }

    fun onConfirmEmptyBackupPasswordClick() {
        fileForBackup?.let { file ->
            createBackup(fileForBackup = file)
        }
    }

    private fun createBackup(fileForBackup: OutputStream, password: String) {
        backupCreatingInProgress = false
        subscribe(
            upstream = createBackupUseCase(
                fileForBackup = fileForBackup,
                password = password,
                secret = true
            ),
            onComplete = {
                resourcesProvider.getString(
                    R.string.backup_successfully_created_message,
                ).apply { _toastMessage.setValue(this) }
            }
        )
    }

    private fun applyBackup(backupFile: InputStream, password: String) {
        backupApplyingInProgress = false
        subscribe(
            upstream = applyBackupUseCase(
                backupFile = backupFile,
                password = password
            )
                .doOnError { error ->
                    if (error is WrongPasswordException) {
                        _toastMessage.postValue(resourcesProvider.getString(R.string.wrong_backup_password_message))
                    } else if (error is IllegalArgumentException) {
                        _toastMessage.postValue(resourcesProvider.getString(R.string.file_not_available_message))
                    }
                },
            onComplete = {
                resourcesProvider.getString(
                    R.string.backup_successfully_applied_message
                ).apply { _toastMessage.setValue(this) }
            }
        )
    }

    override fun getNameForBackupFile(): String =
        "backup-c-${DateFormatterUtil.getShortInternationalDate(Date()).replace(" ", "-")}"
}
