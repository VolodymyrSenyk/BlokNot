package com.senyk.volodymyr.bloknot.data.datasource.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity(tableName = NoteDbo.TABLE_NAME)
data class NoteDbo(
    @ColumnInfo(name = ID) @PrimaryKey val id: String = "",
    @ColumnInfo(name = NAME) val name: String = DateFormat.getDateTimeInstance().format(Date()),
    @ColumnInfo(name = CONTENT) val content: String = "",
    @ColumnInfo(name = SECRET) val secret: Boolean = false,
    @ColumnInfo(name = CREATED_AT) val createdAt: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = UPDATED_AT) val updatedAt: Long = Calendar.getInstance().timeInMillis
) {

    companion object {
        const val TABLE_NAME = "notes"
        const val ID = "note_id"
        const val NAME = "note_name"
        const val CONTENT = "note_content"
        const val SECRET = "note_is_secret"
        const val CREATED_AT = "note_creation_timestamp"
        const val UPDATED_AT = "note_last_updated_timestamp"
    }
}
