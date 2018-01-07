package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_NUMBER_CELL
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_HINT_SMALL_NUMBER_CELL
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_SMALL_NUMBER_CELL
import java.util.*

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 05/01/2018.
 */
abstract class PentatonicAbstractView : View, Observer {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    protected var paint: Paint = Paint()
    var grid: Grid = Grid(1, 1)
        set(value) {
            field = value
            resetSizeAndOffsets()
        }

    var offsetLeft: Float = 10f
    var offsetTop: Float = 10f
    var cellSize: Float = 40f

    protected var desiredWidthUnique: Float = 0f
    protected var desiredHintWidthMultiple: Float = 0f
    protected var desiredWidthMultiple: Float = 0f
    protected var desiredTextSizeUnique: Float = 0f
    protected var desiredHintTextSizeMultiple: Float = 0f
    protected var desiredTextSizeMultiple: Float = 0f
    protected var textHeightUnique: Float = 0f
    protected var textHintHeightMultiple: Float = 0f
    protected var textHeightMultiple: Float = 0f
    /**
     * https://stackoverflow.com/questions/12166476/android-canvas-drawtext-set-font-size-from-width
     * Sets the text size for a Paint object so a given string of text will be given width.
     *
     * @param desiredWidth the desired width
     * @param text         the text that should be that width
     */
    protected fun getTextSizeForWidth(desiredWidth: Float, text: String): Float {
        val testTextSize = 48f
        // Get the bounds of the text, using our testTextSize
        paint.textSize = testTextSize
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        // calculate the desired size as a proportion of our testTextSize
        return testTextSize * desiredWidth / bounds.height()
    }

    protected fun getTextHeightForSize(textSize: Float, text: String): Float {
        paint.textSize = textSize
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        return bounds.height().toFloat()
    }

    private fun resetSizeAndOffsets() {
        // Calculate cellSize
        val maxCellHeight: Float = (height - 2 * offsetLeft) / grid.nbLines
        val maxCellWidth: Float = (width - 2 * offsetTop) / grid.nbColumns
        cellSize = Math.min(maxCellHeight, maxCellWidth)

        offsetTop = (height - cellSize * grid.nbLines) / 2
        offsetLeft = (width - cellSize * grid.nbColumns) / 2

        desiredWidthUnique = cellSize * PROPORTION_NUMBER_CELL
        desiredTextSizeUnique = getTextSizeForWidth(desiredWidthUnique, "5")

        desiredHintWidthMultiple = cellSize * PROPORTION_HINT_SMALL_NUMBER_CELL
        desiredHintTextSizeMultiple = getTextSizeForWidth(desiredHintWidthMultiple, "5")

        desiredWidthMultiple = cellSize * PROPORTION_SMALL_NUMBER_CELL
        desiredTextSizeMultiple = getTextSizeForWidth(desiredWidthMultiple, "5")

        textHeightUnique = getTextHeightForSize(desiredTextSizeUnique, "5")
        textHintHeightMultiple = getTextHeightForSize(desiredHintTextSizeMultiple, "5")
        textHeightMultiple = getTextHeightForSize(desiredTextSizeMultiple, "5")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // Calculate offsets
        Log.d(PentatonicView.TAG, "onLayout($changed, $left, $top, $right, $bottom)")
        resetSizeAndOffsets()
    }

}