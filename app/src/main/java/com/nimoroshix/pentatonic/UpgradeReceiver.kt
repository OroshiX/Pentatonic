package com.nimoroshix.pentatonic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Project Pentatonic
 * <p>
 * Created by OroshiX on 22/01/2018.
 */

class UpgradeReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "UpgradeReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Application was upgraded
        Log.d(TAG, "onReceive: ${intent.action}")
        val versionCode = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME
        Log.d(TAG, "version code: $versionCode, versionName: $versionName")
    }
}
