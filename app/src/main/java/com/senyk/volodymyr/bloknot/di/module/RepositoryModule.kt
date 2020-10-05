package com.senyk.volodymyr.bloknot.di.module

import com.senyk.volodymyr.bloknot.data.repository.*
import com.senyk.volodymyr.bloknot.domain.repository.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [PersistenceModule::class])
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindAppFirstLaunchRepository(repository: AppFirstLaunchSharedPrefsRepository): AppFirstLaunchRepository

    @Singleton
    @Binds
    fun bindAppThemeRepository(repository: DayNightModeSharedPrefsRepository): DayNightModeRepository

    @Singleton
    @Binds
    fun bindNotesRepository(repository: NotesRoomRepository): NotesRepository

    @Singleton
    @Binds
    fun bindLockRepository(repository: LockSharedPrefsRepository): LockRepository

    @Singleton
    @Binds
    fun bindBackupRepository(repository: BackupFileSystemRepository): BackupRepository
}
