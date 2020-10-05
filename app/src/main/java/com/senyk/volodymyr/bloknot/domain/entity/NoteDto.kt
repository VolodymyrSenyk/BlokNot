package com.senyk.volodymyr.bloknot.domain.entity

import java.text.DateFormat
import java.util.*

data class NoteDto(
    val id: String = "",
    val name: String = DateFormat.getDateTimeInstance().format(Date()),
    val content: String = "",
    val isSecret: Boolean = false,
    val creationTimeInMillis: Long = Calendar.getInstance().timeInMillis,
    val lastUpdateTimeInMillis: Long = Calendar.getInstance().timeInMillis
) {

    fun isNew(): Boolean = id.trim().isEmpty()
    fun isEmpty(): Boolean = content.trim().isEmpty()
}
