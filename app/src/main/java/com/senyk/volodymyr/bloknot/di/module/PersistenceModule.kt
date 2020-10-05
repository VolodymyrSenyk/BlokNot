package com.senyk.volodymyr.bloknot.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.senyk.volodymyr.bloknot.data.datasource.database.AppRoomDatabase
import com.senyk.volodymyr.bloknot.data.datasource.database.dao.NotesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideAppRoomDatabase(context: Context): AppRoomDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppRoomDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    fun provideNotesDao(database: AppRoomDatabase): NotesDao =
        database.getNotesDao()

    companion object {
        private const val SHARED_PREFS_NAME = "AppSharedPrefs"
        private const val DATABASE_NAME = "AppDatabase.db"
    }
}
