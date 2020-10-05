package com.senyk.volodymyr.bloknot.data.repository

import com.senyk.volodymyr.bloknot.data.datasource.database.dao.NotesDao
import com.senyk.volodymyr.bloknot.data.datasource.database.mapper.NoteDboDtoMapper
import com.senyk.volodymyr.bloknot.domain.entity.NoteDto
import com.senyk.volodymyr.bloknot.domain.repository.NotesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class NotesRoomRepository @Inject constructor(
    private val notesDao: NotesDao,
    private val noteMapper: NoteDboDtoMapper
) : NotesRepository {

    override fun add(data: NoteDto): Single<NoteDto> =
        notesDao.add(noteMapper(data)).andThen(getById(data.id))

    override fun addAll(data: List<NoteDto>): Completable =
        notesDao.addAll(data.map { note -> noteMapper(note) })

    override fun getById(id: String): Single<NoteDto> =
        notesDao.getById(id).map { note -> noteMapper(note) }

    override fun getAllOnce(): Single<List<NoteDto>> =
        notesDao.getAllOnce().map { notes -> notes.map { note -> noteMapper(note) } }

    override fun getAllOnce(secret: Boolean): Single<List<NoteDto>> =
        notesDao.getAllOnce(secret).map { notes -> notes.map { note -> noteMapper(note) } }

    override fun getAll(secret: Boolean): Observable<List<NoteDto>> =
        notesDao.getAll(secret).map { notes -> notes.map { note -> noteMapper(note) } }

    override fun update(data: NoteDto): Single<NoteDto> =
        notesDao.update(noteMapper(data)).andThen(getById(data.id))

    override fun updateAll(data: List<NoteDto>): Completable =
        notesDao.updateAll(data.map { note -> noteMapper(note) })

    override fun deleteById(id: String): Completable = notesDao.removeById(id)
}
