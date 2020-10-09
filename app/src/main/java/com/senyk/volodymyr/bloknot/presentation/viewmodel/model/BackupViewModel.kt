package com.senyk.volodymyr.bloknot.presentation.viewmodel.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.exception.WrongPasswordException
import com.senyk.volodymyr.bloknot.domain.usecase.backup.ApplyBackupUseCase
import com.senyk.volodymyr.bloknot.domain.usecase.backup.CreateBackupUseCase
import com.senyk.volodymyr.bloknot.presentation.view.util.extensions.getInputStream
import com.senyk.volodymyr.bloknot.presentation.view.util.extensions.getOutputStream
import com.senyk.volodymyr.bloknot.presentation.viewmodel.base.BaseRxViewModel
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.DateFormatterUtil
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.livedata.event.navigation.StartActivityForResultEvent
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

open class BackupViewModel @Inject constructor(
    protected val createBackupUseCase: CreateBackupUseCase,
    protected val applyBackupUseCase: ApplyBackupUseCase,
    protected val resourcesProvider: ResourcesProvider
) : BaseRxViewModel() {

    protected var backupCreatingInProgress = false
    protected var backupApplyingInProgress = false

    open fun onCreateBackupClick() {
        backupCreatingInProgress = true
        requestFileForBackup()
    }

    open fun onApplyBackupClick() {
        backupApplyingInProgress = true
        requestBackupFile()
    }

    protected open fun requestFileForBackup() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(
            Intent.EXTRA_TITLE,
            getNameForBackupFile()
        )
        intent.type = "*/*"
        val chooserMessage =
            resourcesProvider.getString(R.string.dialog_select_file_for_backup_message)
        val chooserIntent = Intent.createChooser(intent, chooserMessage)
        val event = StartActivityForResultEvent(
            intent = chooserIntent,
            requestCode = RC_CHOOSE_FILE_FOR_BACKUP
        )
        _navigationEvent.setHandledValue(event)
    }

    protected open fun requestBackupFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        val chooserMessage =
            resourcesProvider.getString(R.string.dialog_select_backup_file_message)
        val chooserIntent = Intent.createChooser(intent, chooserMessage)
        val event = StartActivityForResultEvent(
            intent = chooserIntent,
            requestCode = RC_CHOOSE_BACKUP_FILE
        )
        _navigationEvent.setHandledValue(event)
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context) {
        when (requestCode) {
            RC_CHOOSE_FILE_FOR_BACKUP -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data
                    val file = uri?.getOutputStream(context)
                    if (file == null) {
                        onBackupInteractionStop()
                    } else {
                        onFileForBackupSelected(file)
                    }
                } else {
                    onBackupInteractionStop()
                }
            }

            RC_CHOOSE_BACKUP_FILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data
                    val file = uri?.getInputStream(context)
                    if (file == null) {
                        onBackupInteractionStop()
                    } else {
                        onBackupFileSelected(file)
                    }
                } else {
                    onBackupInteractionStop()
                }
            }
        }
    }

    protected open fun onBackupFileSelected(inputStream: InputStream) {
        if (backupApplyingInProgress) {
            applyBackup(backupFile = inputStream)
        }
    }

    protected open fun onFileForBackupSelected(outputStream: OutputStream) {
        if (backupCreatingInProgress) {
            createBackup(fileForBackup = outputStream)
        }
    }

    protected open fun createBackup(fileForBackup: OutputStream) {
        backupCreatingInProgress = false
        subscribe(
            upstream = createBackupUseCase(fileForBackup = fileForBackup),
            onComplete = {
                resourcesProvider.getString(
                    R.string.backup_successfully_created_message,
                ).apply { _toastMessage.setValue(this) }
            }
        )
    }

    protected open fun applyBackup(backupFile: InputStream) {
        backupApplyingInProgress = false
        subscribe(
            upstream = applyBackupUseCase(backupFile = backupFile)
                .doOnError { error ->
                    if (error is WrongPasswordException) {
                        _toastMessage.postValue(resourcesProvider.getString(R.string.backup_secured_message))
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

    protected open fun onBackupInteractionStop() {
        backupCreatingInProgress = false
        backupApplyingInProgress = false
    }

    protected open fun getNameForBackupFile(): String =
        "backup-${DateFormatterUtil.getShortInternationalDate(Date()).replace(" ", "-")}"

    companion object {
        private const val RC_CHOOSE_FILE_FOR_BACKUP = 11
        private const val RC_CHOOSE_BACKUP_FILE = 12
    }
}
