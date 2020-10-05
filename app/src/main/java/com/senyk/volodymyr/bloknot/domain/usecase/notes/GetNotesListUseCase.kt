package com.senyk.volodymyr.bloknot.domain.usecase.notes

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetNotesListUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    operator fun invoke(secret: Boolean = false): Observable<List<NoteDto>> =
        notesRepository.getAll(secret = secret)
            .map { notes -> notes.map { note -> note.copy(content = "") } }
}
