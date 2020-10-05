package com.senyk.volodymyr.bloknot.data.datasource.database.mapper

import com.senyk.volodymyr.bloknot.data.datasource.database.entity.NoteDbo
import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import javax.inject.Inject

class NoteDboDtoMapper @Inject constructor() {
    operator fun invoke(dto: NoteDto): NoteDbo =
        NoteDbo(
            id = dto.id,
            name = dto.name,
            content = dto.content,
            secret = dto.isSecret,
            createdAt = dto.creationTimeInMillis,
            updatedAt = dto.lastUpdateTimeInMillis
        )

    operator fun invoke(dbo: NoteDbo): NoteDto =
        NoteDto(
            id = dbo.id,
            name = dbo.name,
            content = dbo.content,
            isSecret = dbo.secret,
            creationTimeInMillis = dbo.createdAt,
            lastUpdateTimeInMillis = dbo.updatedAt
        )
}
