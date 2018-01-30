package com.nimoroshix.pentatonic.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import com.nimoroshix.pentatonic.BuildConfig
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 30/01/2018.
 */
const val TAG = "ShareUtil"

fun takeScreenshot(view: View): Bitmap {
    view.isDrawingCacheEnabled = true
    return view.drawingCache
}

fun saveBitmap(context: Context, bitmap: Bitmap): File {
    val filename = "screenshot.png"
    val fos: FileOutputStream
    try {
        fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    } catch (e: FileNotFoundException) {
        Log.e(TAG, e.message, e)
    } catch (e: IOException) {
        Log.e(TAG, e.message, e)
    }
    return context.getFileStreamPath(filename)
}

fun shareIt(context: Context, file: File, body: String, subject: String) {
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    //Uri.fromFile(file)
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "image/*"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, body)
    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
}