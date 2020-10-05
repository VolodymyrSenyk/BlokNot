package com.senyk.volodymyr.bloknot.di.module

import androidx.lifecycle.ViewModel
import com.senyk.volodymyr.bloknot.di.annotation.mapkey.ViewModelKey
import com.senyk.volodymyr.bloknot.presentation.viewmodel.model.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [SchedulerModule::class, RepositoryModule::class])
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DayNightModeViewModel::class)
    fun bindDayNightModeViewModel(viewModel: DayNightModeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotesListViewModel::class)
    fun bindNotesListViewModel(viewModel: NotesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteDetailsViewModel::class)
    fun bindNoteDetailsViewModel(viewModel: NoteDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditNoteViewModel::class)
    fun bindEditNoteViewModel(viewModel: EditNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvisibleLockViewModel::class)
    fun bindInvisibleLockViewModel(viewModel: InvisibleLockViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CryptoNotepadViewModel::class)
    fun bindCryptoNotepadViewModel(viewModel: CryptoNotepadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CryptoNotesListViewModel::class)
    fun bindCryptoNotesListViewModel(viewModel: CryptoNotesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CryptoNoteDetailsViewModel::class)
    fun bindCryptoNoteDetailsViewModel(viewModel: CryptoNoteDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditCryptoNoteViewModel::class)
    fun bindEditCryptoNoteViewModel(viewModel: EditCryptoNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PermissionsViewModel::class)
    fun bindPermissionsViewModel(viewModel: PermissionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BackupViewModel::class)
    fun bindBackupViewModel(viewModel: BackupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CryptoBackupViewModel::class)
    fun bindCryptoBackupViewModel(viewModel: CryptoBackupViewModel): ViewModel
}
