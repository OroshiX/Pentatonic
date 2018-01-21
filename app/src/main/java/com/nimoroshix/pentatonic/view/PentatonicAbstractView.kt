package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_HINT_SMALL_NUMBER_CELL
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_NUMBER_CELL
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_SMALL_NUMBER_CELL
import com.nimoroshix.pentatonic.util.ViewUtils.Companion.getTextHeightForSize
import com.nimoroshix.pentatonic.util.ViewUtils.Companion.getTextSizeForWidth
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

    private val minOffsetLeft = 10f
    private val minOffsetTop = 10f
    var offsetLeft: Float = minOffsetLeft
    var offsetTop: Float = minOffsetTop
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


    private fun resetSizeAndOffsets() {
        offsetLeft = minOffsetLeft
        offsetTop = minOffsetTop

        // Calculate cellSize
        val maxCellHeight: Float = (height - 2 * offsetLeft) / grid.nbLines
        val maxCellWidth: Float = (width - 2 * offsetTop) / grid.nbColumns
        cellSize = Math.min(maxCellHeight, maxCellWidth)

        offsetTop = (height - cellSize * grid.nbLines) / 2
        offsetLeft = (width - cellSize * grid.nbColumns) / 2

        desiredWidthUnique = cellSize * PROPORTION_NUMBER_CELL
        desiredTextSizeUnique = getTextSizeForWidth(desiredWidthUnique, "5", paint)

        desiredHintWidthMultiple = cellSize * PROPORTION_HINT_SMALL_NUMBER_CELL
        desiredHintTextSizeMultiple = getTextSizeForWidth(desiredHintWidthMultiple, "5", paint)

        desiredWidthMultiple = cellSize * PROPORTION_SMALL_NUMBER_CELL
        desiredTextSizeMultiple = getTextSizeForWidth(desiredWidthMultiple, "5", paint)

        textHeightUnique = getTextHeightForSize(desiredTextSizeUnique, "5", paint)
        textHintHeightMultiple = getTextHeightForSize(desiredHintTextSizeMultiple, "5", paint)
        textHeightMultiple = getTextHeightForSize(desiredTextSizeMultiple, "5", paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // Calculate offsets
        if (changed)
            resetSizeAndOffsets()
    }

}