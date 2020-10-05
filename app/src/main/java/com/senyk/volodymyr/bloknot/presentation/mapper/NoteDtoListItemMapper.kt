package com.senyk.volodymyr.bloknot.presentation.mapper

import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.presentation.view.recyclerview.SimpleDataListItem
import javax.inject.Inject

class NoteDtoListItemMapper @Inject constructor() {

    operator fun invoke(dto: NoteDto): SimpleDataListItem =
        SimpleDataListItem(
            id = dto.id,
            title = dto.name,
            data = emptyList()
        )

    operator fun invoke(listItem: SimpleDataListItem): NoteDto =
        NoteDto(
            id = listItem.id,
            name = listItem.title,
            content = listItem.data.toString()
        )
}
