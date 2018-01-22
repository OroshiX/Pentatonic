package com.nimoroshix.pentatonic

import android.app.Application
import android.content.Context
import android.util.Log
import com.facebook.stetho.Stetho

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
class PentatonicApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build())

        checkFirstRun()
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
        if (currentVersionCode == savedVersionCode) {
            // Just a normal run
            return
        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO this is a new install (or user cleared the sharedPrefs)
        } else if (currentVersionCode > savedVersionCode) {
            // TODO this is an upgrade (handle different migrations)
        }

        // update the shared prefs with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }

    companion object {
        const val TAG = "PentatonicApp"
        const val PREFS_NAME = "pentatonicPrefFile"
        const val PREF_VERSION_CODE_KEY = "version_code"
        const val DOESNT_EXIST = -1

    }
}