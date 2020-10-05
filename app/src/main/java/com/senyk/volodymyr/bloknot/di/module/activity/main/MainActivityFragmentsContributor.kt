package com.senyk.volodymyr.bloknot.di.module.activity.main

import com.senyk.volodymyr.bloknot.di.annotation.scope.FragmentScope
import com.senyk.volodymyr.bloknot.presentation.view.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityFragmentsContributor {

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeNotesListFragment(): NotesListFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeNoteDetailsFragment(): NoteDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeEditNoteFragment(): EditNoteFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeInvisibleLockFragment(): InvisibleLockFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeCryptoNotesListFragment(): CryptoNotesListFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeCryptoNoteDetailsFragment(): CryptoNoteDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeEditCryptoNoteFragment(): EditCryptoNoteFragment
}
