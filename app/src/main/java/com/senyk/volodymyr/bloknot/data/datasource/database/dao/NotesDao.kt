package com.senyk.volodymyr.bloknot.data.datasource.database.dao

import androidx.room.*
import com.senyk.volodymyr.bloknot.data.datasource.database.entity.NoteDbo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(data: NoteDbo): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(data: List<NoteDbo>): Completable

    @Query("SELECT * FROM ${NoteDbo.TABLE_NAME} WHERE ${NoteDbo.ID} = :id")
    fun getById(id: String): Single<NoteDbo>

    @Query("SELECT * FROM ${NoteDbo.TABLE_NAME} ORDER BY (${NoteDbo.NAME}) ASC")
    fun getAllOnce(): Single<List<NoteDbo>>

    @Query("SELECT * FROM ${NoteDbo.TABLE_NAME} WHERE ${NoteDbo.SECRET} = :secret ORDER BY (${NoteDbo.NAME}) ASC")
    fun getAllOnce(secret: Boolean): Single<List<NoteDbo>>

    @Query("SELECT * FROM ${NoteDbo.TABLE_NAME} WHERE ${NoteDbo.SECRET} = :secret ORDER BY (${NoteDbo.NAME}) ASC")
    fun getAll(secret: Boolean): Observable<List<NoteDbo>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(data: NoteDbo): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(data: List<NoteDbo>): Completable

    @Query("DELETE FROM ${NoteDbo.TABLE_NAME} WHERE ${NoteDbo.ID} = :id")
    fun removeById(id: String): Completable

    @Delete
    fun remove(data: NoteDbo): Completable

    @Delete
    fun removeAll(data: List<NoteDbo>): Completable

    @Query("DELETE FROM ${NoteDbo.TABLE_NAME}")
    fun clear(): Completable
}
