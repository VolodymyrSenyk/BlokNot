package com.senyk.volodymyr.bloknot.presentation.mapper

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.presentation.entity.NoteUi
import javax.inject.Inject

class NoteUiDtoMapper @Inject constructor() {

    operator fun invoke(dto: NoteDto): NoteUi =
        NoteUi(
            id = dto.id,
            name = dto.name,
            content = dto.content
        )

    operator fun invoke(ui: NoteUi): NoteDto =
        NoteDto(
            id = ui.id,
            name = ui.name,
            content = ui.content
        )
}
