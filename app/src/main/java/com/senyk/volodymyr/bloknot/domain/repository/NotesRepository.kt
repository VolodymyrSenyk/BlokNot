package com.senyk.volodymyr.bloknot.domain.repository

import androidx.annotation.CheckResult
import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface NotesRepository {

    @CheckResult
    fun add(data: NoteDto): Single<NoteDto>

    @CheckResult
    fun addAll(data: List<NoteDto>): Completable

    @CheckResult
    fun getById(id: String): Single<NoteDto>

    @CheckResult
    fun getAllOnce(): Single<List<NoteDto>>

    @CheckResult
    fun getAllOnce(secret: Boolean): Single<List<NoteDto>>

    @CheckResult
    fun getAll(secret: Boolean = false): Observable<List<NoteDto>>

    @CheckResult
    fun update(data: NoteDto): Single<NoteDto>

    @CheckResult
    fun updateAll(data: List<NoteDto>): Completable

    @CheckResult
    fun deleteById(id: String): Completable
}
