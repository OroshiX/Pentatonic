package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.RelativePosition.*
import com.nimoroshix.pentatonic.util.Constants.Companion.PROPORTION_MARGIN_SMALL_NUMBER_CELL
import com.nimoroshix.pentatonic.util.ViewUtils.Companion.getTextHeightForSize
import com.nimoroshix.pentatonic.util.ViewUtils.Companion.getTextSizeForWidth
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class PentatonicView : PentatonicAbstractView {
    override fun update(o: Observable?, arg: Any?) {
        if (arg == Grid.STRUCTURE || arg == Grid.ZOOM) {
            invalidate()
        }
    }


    private var colorGrid: Int

    private var pathEffectDotted: PathEffect
    private var strokeWidthLarge: Float = 6f
    private var strokeWidthThin: Float = 3f
    private var strokeWidthNumber: Float = 2.5f

    private var xArray: Array<Float> = Array(5) { _ -> 0f }
    private var yArray: Array<Float> = Array(5) { _ -> 0f }

//    private val backgroundDrawable: Drawable?

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PentatonicView)
        colorGrid = array.getColor(R.styleable.PentatonicView_gridColor, Color.BLACK)

        val on = array.getDimension(R.styleable.PentatonicView_dashesOn, 2f)
        val off = array.getDimension(R.styleable.PentatonicView_dashesOff, 10f)
        pathEffectDotted = DashPathEffect(floatArrayOf(on, off), 0f)
        array.recycle()

        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidthLarge
//        backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.beige_paper)
    }

    companion object {
        @JvmField
        val TAG = "PentatonicView"
    }

//    private val imageBounds: Rect = Rect()

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")

        canvas.concat(grid.viewMatrix)

        // Draw a rectangle (m * cellSize) * (n * cellSize)
        paint.style = Paint.Style.STROKE
        paint.color = colorGrid

        canvas.drawRect(offsetLeft, offsetTop, offsetLeft + grid.nbColumns * cellSize,
                offsetTop + grid.nbLines * cellSize, paint)

        // Draw a dotted grid
        paint.pathEffect = pathEffectDotted
        paint.strokeWidth = strokeWidthThin

        for (i in 0 until grid.nbLines) {
            canvas.drawLine(offsetLeft, offsetTop + i * cellSize,
                    offsetLeft + grid.nbColumns * cellSize, offsetTop + i * cellSize, paint)
        }

        for (j in 0 until grid.nbColumns) {
            canvas.drawLine(offsetLeft + j * cellSize, offsetTop, offsetLeft + j * cellSize,
                    offsetTop + grid.nbLines * cellSize, paint)
        }

        // Draw the different areas (bold)
        paint.strokeWidth = strokeWidthLarge
        paint.pathEffect = null

        // compare two horizontal cells area ids
        for (i in 0 until grid.nbLines) {
            (1 until grid.nbColumns).filter {
                // Compare the value of the area of the 2 adjacent cells
                grid.cells[i][it].area.id != grid.cells[i][it - 1].area.id
            }.forEach {
                        // Different areas, so draw a line
                        canvas.drawLine(offsetLeft + it * cellSize, offsetTop + i * cellSize,
                                offsetLeft + it * cellSize, offsetTop + (i + 1) * cellSize, paint)
                    }
        }

        // compare two vertical cells area ids
        for (j in 0 until grid.nbColumns) {
            (1 until grid.nbLines)
                    .filter { grid.cells[it][j].area.id != grid.cells[it - 1][j].area.id }
                    .forEach {
                        canvas.drawLine(offsetLeft + j * cellSize, offsetTop + it * cellSize,
                                offsetLeft + (j + 1) * cellSize, offsetTop + it * cellSize, paint)
                    }
        }

        // Draw the initial numbers
        paint.strokeWidth = strokeWidthNumber
        paint.style = Paint.Style.FILL_AND_STROKE

        grid.cellSequence().filter { c -> c.enonce || c.sister != null }.forEach {
            // We want to draw the unique value from the initial numbers
            if (it.enonce) {
                // Draw the unique value in the center of the cell
                paint.textSize = desiredTextSizeUnique
                canvas.drawText(it.values[0].toString(),
                        offsetLeft + it.position.nColumn * cellSize + (cellSize - desiredWidthUnique) / 2,
                        offsetTop + it.position.nLine * cellSize + (cellSize + textHeightUnique) / 2,
                        paint)
            }
            if (it.sister != null) {
                paint.textSize = desiredHintTextSizeMultiple
                // Draw the sister symbol on the bottom-right corner
                desiredHintTextSizeMultiple = getTextSizeForWidth(desiredHintWidthMultiple,
                        it.sister.toString(), paint)
                textHintHeightMultiple = getTextHeightForSize(desiredHintTextSizeMultiple,
                        it.sister.toString(), paint)
                canvas.drawText(it.sister.toString(),
                        offsetLeft + it.position.nColumn * cellSize + (1f - PROPORTION_MARGIN_SMALL_NUMBER_CELL) * cellSize - desiredHintWidthMultiple,
                        offsetTop + it.position.nLine * cellSize + (1f - PROPORTION_MARGIN_SMALL_NUMBER_CELL) * cellSize,
                        paint)

            }
        }
        drawDiffOnes(paint, canvas)
    }

    private fun drawDiffOnes(paint: Paint, canvas: Canvas) {
        // Draw all the diff Ones
        grid.diffOnes.forEach { diffOne ->
            for (k in 0 until xArray.size) {
                xArray[k] = offsetLeft + diffOne.position1.nColumn * cellSize + (cellSize * k) / 4
                yArray[k] = offsetTop + diffOne.position1.nLine * cellSize + (cellSize * k) / 4
            }
            val xStart: Float
            val xEnd: Float
            val yStart: Float
            val yEnd: Float
            when (diffOne.position1.getPositionRelativeToMe(diffOne.position2)) {
                TOP          -> {
                    xStart = xArray[2]
                    xEnd = xArray[2]
                    yStart = 2 * yArray[0] - yArray[1]
                    yEnd = yArray[1]
                }
                RIGHT        -> {
                    xStart = 2 * xArray[4] - xArray[3]
                    xEnd = xArray[3]
                    yStart = yArray[2]
                    yEnd = yArray[2]
                }
                BOTTOM       -> {
                    xStart = xArray[2]
                    xEnd = xArray[2]
                    yStart = 2 * yArray[4] - yArray[3]
                    yEnd = yArray[3]
                }
                LEFT         -> {
                    xStart = 2 * xArray[0] - xArray[1]
                    xEnd = xArray[1]
                    yStart = yArray[2]
                    yEnd = yArray[2]
                }
                TOP_RIGHT    -> {
                    xStart = 2 * xArray[4] - xArray[3]
                    xEnd = xArray[3]
                    yStart = 2 * yArray[0] - yArray[1]
                    yEnd = yArray[1]
                }
                BOTTOM_RIGHT -> {
                    xStart = 2 * xArray[4] - xArray[3]
                    xEnd = xArray[3]
                    yStart = 2 * yArray[4] - yArray[3]
                    yEnd = yArray[3]
                }
                BOTTOM_LEFT  -> {
                    xStart = 2 * xArray[0] - xArray[1]
                    xEnd = xArray[1]
                    yStart = 2 * yArray[4] - yArray[3]
                    yEnd = yArray[3]
                }
                TOP_LEFT     -> {
                    xStart = 2 * xArray[0] - xArray[1]
                    xEnd = xArray[1]
                    yStart = 2 * yArray[0] - yArray[1]
                    yEnd = yArray[1]
                }
                ILLEGAL      -> {
                    Log.e(TAG, "illegal position")
                    return
                }
            }
            canvas.drawLine(xStart, yStart, xEnd, yEnd, paint)
        }
    }

}