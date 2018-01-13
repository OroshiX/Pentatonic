package com.nimoroshix.pentatonic

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.nimoroshix.pentatonic.persistence.AppDatabase
import com.nimoroshix.pentatonic.persistence.Pentatonic
import com.nimoroshix.pentatonic.persistence.PentatonicDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
@RunWith(AndroidJUnit4::class)
class PentatonicDaoTest {
    private lateinit var mDatabase: AppDatabase
    private val PENTATONIC = Pentatonic(1, 2)
    private lateinit var mPentatonicDao: PentatonicDao
    @Before
    fun initDb() {
        val context = InstrumentationRegistry.getContext()
        mDatabase = Room.inMemoryDatabaseBuilder(context,
                AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        mPentatonicDao = mDatabase.pentatonicDao()
        PENTATONIC.id = 1
        PENTATONIC.difficulty = 3

    }

    @Test
    fun insertAndGetPentatonicById() {
        mDatabase.pentatonicDao().insertPentatonic(PENTATONIC)
        mPentatonicDao.insertPentatonic(PENTATONIC)
        val byId = mPentatonicDao.getPentatonicById(1)
        assertThat(byId, equalTo(PENTATONIC))
    }

    @After
    fun closeDb() {
        mDatabase.close()
    }
}