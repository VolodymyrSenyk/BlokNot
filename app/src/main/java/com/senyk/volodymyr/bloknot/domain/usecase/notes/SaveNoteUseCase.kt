package com.senyk.volodymyr.bloknot.domain.usecase.notes

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import io.reactivex.Single
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) {

    operator fun invoke(note: NoteDto): Single<NoteDto> =
        if (note.isNew()) {
            handleNewNote(note)
        } else {
            handleExistingNote(note)
        }

    private fun handleNewNote(note: NoteDto): Single<NoteDto> =
        if (note.isEmpty()) {
            Single.fromCallable { NoteDto() }
        } else {
            createNoteUseCase(note)
        }

    private fun handleExistingNote(note: NoteDto): Single<NoteDto> =
        if (note.isEmpty()) {
            deleteNoteUseCase(note.id).toSingle { NoteDto() }
        } else {
            updateNoteUseCase(note)
        }
}
