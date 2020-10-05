package com.senyk.volodymyr.bloknot.domain.usecase.notes

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    operator fun invoke(note: NoteDto): Single<NoteDto> =
        notesRepository.update(note.copy(lastUpdateTimeInMillis = Calendar.getInstance().timeInMillis))
}
