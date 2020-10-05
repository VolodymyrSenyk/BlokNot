package com.senyk.volodymyr.bloknot.domain.interactor

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.usecase.notes.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class NotesInteractor @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val getNotesListUseCase: GetNotesListUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) {

    fun create(note: NoteDto): Single<NoteDto> = createNoteUseCase(note)

    fun get(id: String): Single<NoteDto> = getNoteUseCase(noteId = id)

    fun getAll(secret: Boolean = false): Observable<List<NoteDto>> =
        getNotesListUseCase(secret = secret)

    fun update(note: NoteDto): Single<NoteDto> = updateNoteUseCase(note)

    fun delete(id: String): Completable = deleteNoteUseCase(id)
}
