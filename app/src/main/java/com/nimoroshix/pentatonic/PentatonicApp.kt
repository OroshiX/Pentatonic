package com.nimoroshix.pentatonic

import android.app.Application
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
    }
}