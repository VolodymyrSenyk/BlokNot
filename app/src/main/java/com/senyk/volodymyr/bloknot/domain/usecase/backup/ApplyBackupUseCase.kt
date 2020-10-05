package com.senyk.volodymyr.bloknot.domain.usecase.backup

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.senyk.volodymyr.bloknot.R
import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.repository.BackupRepository
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import com.senyk.volodymyr.bloknot.domain.util.Aes256EncoderDecoder
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.DateFormatterUtil
import com.senyk.volodymyr.bloknot.presentation.viewmodel.util.ResourcesProvider
import io.reactivex.Completable
import io.reactivex.functions.BiFunction
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import javax.inject.Inject

class ApplyBackupUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val backupRepository: BackupRepository,
    private val resourcesProvider: ResourcesProvider,
    private val encoderDecoder: Aes256EncoderDecoder
) {

    operator fun invoke(
        backupFile: File,
        password: String = ""
    ): Completable =
        invoke(backupFile = FileInputStream(backupFile), password = password)

    operator fun invoke(
        backupFile: InputStream,
        password: String = ""
    ): Completable =
        backupRepository.get(inputStream = backupFile)
            .zipWith(
                notesRepository.getAllOnce(),
                BiFunction<String, List<NoteDto>, List<NoteDto>> { backupData, existingNotes ->
                    val notesFromBackup = getNotesFromBackup(
                        backupData = backupData,
                        password = password
                    )
                    return@BiFunction concatNotes(
                        notesFromBackup = notesFromBackup,
                        existingNotes = existingNotes
                    )
                })
            .flatMapCompletable { notes ->
                notesRepository.addAll(notes)
            }

    private fun getNotesFromBackup(backupData: String, password: String): List<NoteDto> {
        val json = encoderDecoder.decode(data = backupData, password = password)
        return fromJson(json)
    }

    private inline fun <reified T> fromJson(json: String): T =
        Gson().fromJson(json, object : TypeToken<List<NoteDto>>() {}.type)

    private fun concatNotes(
        notesFromBackup: List<NoteDto>,
        existingNotes: List<NoteDto>
    ): List<NoteDto> {
        if (notesFromBackup.isNullOrEmpty()) {
            return emptyList()
        } else if (existingNotes.isNullOrEmpty()) {
            return notesFromBackup
        }
        val notes = mutableListOf<NoteDto>()
        notesFromBackup.forEach { backupNote ->
            val existingNote = existingNotes.find { it.id == backupNote.id }
            if (existingNote == null || existingNote.content == backupNote.content) {
                notes.add(backupNote)
            } else {
                notes.add(
                    backupNote.copy(
                        content = concatNotesContent(listOf(backupNote, existingNote)),
                        creationTimeInMillis = backupNote.creationTimeInMillis,
                        lastUpdateTimeInMillis = Calendar.getInstance().timeInMillis
                    )
                )
            }
        }
        return notes
    }

    private fun concatNotesContent(notes: List<NoteDto>): String {
        notes.sortedByDescending { note -> note.lastUpdateTimeInMillis }
        val res = StringBuilder()
        notes.forEach { note ->
            val dateString =
                DateFormatterUtil.getShortInternationalDate(note.lastUpdateTimeInMillis)
            res.apply {
                append(SEPARATOR)
                append(resourcesProvider.getString(R.string.note_last_update_output, dateString))
                append(SEPARATOR)
                append(System.lineSeparator())
                append(note.content)
                append(System.lineSeparator())
            }
        }
        return res.toString()
    }

    companion object {
        private const val SEPARATOR = "---"
    }
}
