package com.senyk.volodymyr.bloknot.domain.usecase.notes

import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import io.reactivex.Completable
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    operator fun invoke(noteId: String): Completable = notesRepository.deleteById(id = noteId)
}
