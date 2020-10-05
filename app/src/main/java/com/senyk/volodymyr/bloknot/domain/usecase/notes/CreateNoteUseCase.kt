package com.senyk.volodymyr.bloknot.domain.usecase.notes

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    operator fun invoke(note: NoteDto): Single<NoteDto> =
        notesRepository.add(
            note.copy(
                id = Calendar.getInstance().timeInMillis.toString(),
                creationTimeInMillis = Calendar.getInstance().timeInMillis,
                lastUpdateTimeInMillis = Calendar.getInstance().timeInMillis
            )
        )
}
