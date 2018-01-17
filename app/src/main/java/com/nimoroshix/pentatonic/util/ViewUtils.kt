package com.nimoroshix.pentatonic.util

import android.graphics.Paint
import android.graphics.Rect
import kotlin.math.min

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 17/01/2018.
 */
class ViewUtils {
    companion object {

        /**
         * https://stackoverflow.com/questions/12166476/android-canvas-drawtext-set-font-size-from-width
         * Sets the text size for a Paint object so a given string of text will be given width.
         *
         * @param desiredWidth the desired width
         * @param text         the text that should be that width
         */
        fun getTextSizeForWidth(desiredWidth: Float, text: String, paint: Paint): Float {
            val testTextSize = 48f
            // Get the bounds of the text, using our testTextSize
            paint.textSize = testTextSize
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)

            // calculate the desired size as a proportion of our testTextSize
            return testTextSize * desiredWidth / bounds.height()
        }

        fun getTextHeightForSize(textSize: Float, text: String, paint: Paint): Float {
            paint.textSize = textSize
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            return bounds.height().toFloat()
        }

        fun getFitTextSize(desiredWidth: Float, desiredHeight: Float, text: String, paint: Paint): Float {
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val nowHeight = bounds.height()
            val nowWidth = bounds.width()

            return min(desiredWidth * paint.textSize / nowWidth, desiredHeight * paint.textSize / nowHeight)
        }
    }
}