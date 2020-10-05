package com.senyk.volodymyr.bloknot.domain.usecase.notes

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import io.reactivex.Single
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    operator fun invoke(noteId: String): Single<NoteDto> =
        notesRepository.getById(id = noteId)
}
