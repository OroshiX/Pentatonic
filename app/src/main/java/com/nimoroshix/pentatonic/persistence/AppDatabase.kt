package com.nimoroshix.pentatonic.persistence

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
@Database(entities = arrayOf(Pentatonic::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pentatonicDao(): PentatonicDao

    companion object {
        private var NAME_DB = "pentatonic.db"
        private var INSTANCE: AppDatabase? = null
//        private val MIGRATION_1_2: Migration? = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                        "CREATE TABLE `toto` (`id INTEGER, `name` TEXT, PRIMARY KEY(`id`))")
//            }
//

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {
                        val rdc = object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // TODO
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                            }
                        }
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                AppDatabase::class.java, NAME_DB)
                                .addCallback(rdc)
//                                .addMigrations( )
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}