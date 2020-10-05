package com.senyk.volodymyr.bloknot.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senyk.volodymyr.bloknot.data.datasource.database.dao.NotesDao
import com.senyk.volodymyr.bloknot.data.datasource.database.entity.NoteDbo

@Database(
    entities = [NoteDbo::class],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NotesDao
}
