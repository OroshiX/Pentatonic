package com.nimoroshix.pentatonic.persistence

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.util.Log

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
@Database(entities = [Pentatonic::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pentatonicDao(): PentatonicDao

    companion object {
        private const val TAG = "AppDatabase"
        private var NAME_DB = "pentatonic.db"
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val MIGRATION_1_2: Migration? = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                        "CREATE TABLE `toto` (`id INTEGER, `name` TEXT, PRIMARY KEY(`id`))")
            }
        }
        private val rdc = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d(TAG, "onCreate($db)")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(TAG, "onOpen($db)")
            }
        }

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, NAME_DB)
                .addCallback(rdc)
                .addMigrations(MIGRATION_1_2)
                .build()
    }

}