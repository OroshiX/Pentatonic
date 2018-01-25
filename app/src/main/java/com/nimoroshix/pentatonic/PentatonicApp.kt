package com.nimoroshix.pentatonic

import android.app.Application
import android.content.Context
import android.util.Log
import com.facebook.stetho.Stetho
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.persistence.AppDatabase
import com.nimoroshix.pentatonic.persistence.PentatonicDao
import com.nimoroshix.pentatonic.serializer.Serializer
import com.nimoroshix.pentatonic.util.Constants.Companion.MAX_DIFFICULTY
import com.nimoroshix.pentatonic.util.Constants.Companion.PATH_PENTA
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
class PentatonicApp : Application() {

    override fun onCreate() {
        super.onCreate()
        checkFirstRun()

        // Go to chrome://inspect/#devices -> inspect com.nimoroshix.pentatonic -> resources tab -> web SQL
        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build())

    }

    /**
     * https://stackoverflow.com/a/30274315/2589983
     */
    private fun checkFirstRun() {
        // get Current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // get saved version code
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)

        Log.d(TAG, "current version : $currentVersionCode, saved version : $savedVersionCode")

        // check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode ->
                // Just a normal run
                return
            savedVersionCode == DOESNT_EXIST ->
                // this is a new install (or user cleared the sharedPrefs)
                Observable.just(AppDatabase.getInstance(this).pentatonicDao())
                        .subscribeOn(Schedulers.io())
                        .subscribe { dao -> doOnFirstInstall(dao) }
            currentVersionCode > savedVersionCode ->
                // this is an upgrade (handle different migrations)
                Observable.just(AppDatabase.getInstance(this).pentatonicDao())
                        .subscribeOn(Schedulers.io())
                        .subscribe { dao -> doOnUpgrade(savedVersionCode, currentVersionCode, dao) }
        }

        // update the shared prefs with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }

    /**
     * Here, get all pentatonics available, and put them in the database
     */
    private fun doOnFirstInstall(dao: PentatonicDao) {
        val versions: Array<out String> = resources.assets.list(PATH_PENTA)
        versions.forEach { version ->
            addAllPentatonicsFromVersionFolder(dao, version)
        }
    }

    private fun addAllPentatonicsFromVersionFolder(dao: PentatonicDao, version: String) {
        val assetsPath = PATH_PENTA + File.separator + version + File.separator
        (1..MAX_DIFFICULTY).forEach { diff ->
            addAllPentatonicsFromFolder(dao, assetsPath + diff, diff)
        }
    }

    private fun addAllPentatonicsFromFolder(dao: PentatonicDao, path: String, difficulty: Int) {
        val pentatonicFiles = resources.assets.list(path)
        pentatonicFiles.forEach { pentatonicFile ->
            val grid: Grid = Serializer.deserialize(resources.assets.open(path + File.separator + pentatonicFile), pentatonicFile)
            grid.difficulty = difficulty
            grid.filename = pentatonicFile
            val penta = Serializer.fromGridToDb(grid)
            dao.insertPentatonic(penta)
        }
    }

    /**
     * Here, see which upgrade to apply, and put the remainings pentatonics in the database
     */
    private fun doOnUpgrade(oldVersion: Int, newVersion: Int, dao: PentatonicDao) {
        var currentVersion = oldVersion
        assert(oldVersion < newVersion)
        while (currentVersion < newVersion) {
            upgradeOneVersion(currentVersion, dao)
            currentVersion++
        }
    }

    private fun upgradeOneVersion(oldVersion: Int, dao: PentatonicDao) {
        when (oldVersion) {
            1 -> from1to2(dao)
            2 -> from2to3(dao)
        }
    }

    private fun from1to2(dao: PentatonicDao) {
        addAllPentatonicsFromVersionFolder(dao, "v2")
    }

    private fun from2to3(dao: PentatonicDao) {
        addAllPentatonicsFromVersionFolder(dao, "v3")
    }

    companion object {
        const val TAG = "PentatonicApp"
        const val PREFS_NAME = "pentatonicPrefFile"
        const val PREF_VERSION_CODE_KEY = "version_code"
        const val DOESNT_EXIST = -1

    }
}