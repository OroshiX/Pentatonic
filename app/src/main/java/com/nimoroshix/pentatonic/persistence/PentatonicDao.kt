package com.nimoroshix.pentatonic.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
@Dao
interface PentatonicDao {
    @Query("select * from pentatonic")
    fun getAllPentatonic(): Flowable<List<Pentatonic>>

    @Query("select * from pentatonic where difficulty = :diff")
    fun findPentatonicByDifficulty(diff: Int): List<Pentatonic>

    @Query("select * from pentatonic where id = :id")
    fun getPentatonicById(id: Long): Pentatonic

    @Insert(onConflict = REPLACE)
    fun insertPentatonic(pentatonic: Pentatonic)

}