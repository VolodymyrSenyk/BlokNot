package com.senyk.volodymyr.bloknot.di.module.activity.main

import com.senyk.volodymyr.bloknot.di.annotation.scope.ActivityScope
import com.senyk.volodymyr.bloknot.presentation.view.activity.NotesActivity
import com.senyk.volodymyr.bloknot.presentation.view.activity.base.BaseActivity
import dagger.Binds
import dagger.Module

@Module
interface MainActivityModule {

    @ActivityScope
    @Binds
    fun bindActivity(activity: NotesActivity): BaseActivity
}
