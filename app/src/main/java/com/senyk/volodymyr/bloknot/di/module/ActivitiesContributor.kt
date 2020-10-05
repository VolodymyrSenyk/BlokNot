package com.senyk.volodymyr.bloknot.di.module

import com.senyk.volodymyr.bloknot.di.annotation.scope.ActivityScope
import com.senyk.volodymyr.bloknot.di.module.activity.main.MainActivityFragmentsContributor
import com.senyk.volodymyr.bloknot.di.module.activity.main.MainActivityModule
import com.senyk.volodymyr.bloknot.presentation.view.activity.NotesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
interface ActivitiesContributor {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            MainActivityFragmentsContributor::class
        ]
    )
    fun contributeMainActivity(): NotesActivity
}
