package com.senyk.volodymyr.bloknot.di.module

import com.senyk.volodymyr.bloknot.di.annotation.qualifier.execution.Background
import com.senyk.volodymyr.bloknot.di.annotation.qualifier.execution.Foreground
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SchedulerModule {

    @Background
    @Provides
    fun provideBackgroundScheduler(): Scheduler = Schedulers.computation()

    @Foreground
    @Provides
    fun provideForegroundScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
