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

/**
 * Takes a screenshot of a view, and returns a Bitmap
 * @param view the view to take the screenshot of
 * @return the bitmap of the view
 */
fun takeScreenshot(view: View): Bitmap {
    view.isDrawingCacheEnabled = true
    return view.drawingCache
}

/**
 * Saves a bitmap to an image file
 * @param context
 * @param bitmap the bitmap to save
 * @return the file in which the bitmap was saved
 */
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

/**
 * Starts a share intent with a file, a subject and a body
 * @param context
 * @param file the file to share
 * @param body the message body
 * @param subject the message subject
 */
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