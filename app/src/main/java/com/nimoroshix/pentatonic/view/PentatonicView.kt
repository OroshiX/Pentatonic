package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.util.AttributeSet
import android.util.Log
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.util.PROPORTION_MARGIN_SMALL_NUMBER_CELL
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class PentatonicView : PentatonicAbstractView {
    override fun update(o: Observable?, arg: Any?) {
        if (arg == Grid.STRUCTURE)
            invalidate()
    }


    private var pathEffectDotted: PathEffect = DashPathEffect(floatArrayOf(10F, 50F), 0F)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.isAntiAlias = true
        paint.strokeWidth = 5f
    }

    companion object {
        @JvmField
        val TAG = "PentatonicView"
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        canvas.drawRGB(252, 247, 219)

        // Draw a rectangle (m * cellSize) * (n * cellSize)
        paint.style = Paint.Style.STROKE

        canvas.drawRect(offsetLeft, offsetTop, offsetLeft + grid.nbColumns * cellSize,
                offsetTop + grid.nbLines * cellSize, paint)

        // Draw a dotted grid
        paint.pathEffect = pathEffectDotted
        paint.strokeWidth = 1f

        for (i in 0 until grid.nbLines) {
            canvas.drawLine(offsetLeft, offsetTop + i * cellSize,
                    offsetLeft + grid.nbColumns * cellSize, offsetTop + i * cellSize, paint)
        }

        for (j in 0 until grid.nbColumns) {
            canvas.drawLine(offsetLeft + j * cellSize, offsetTop, offsetLeft + j * cellSize,
                    offsetTop + grid.nbLines * cellSize, paint)
        }

        // Draw the different areas (bold)
        paint.strokeWidth = 6f
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
        paint.strokeWidth = 2f
        paint.style = Paint.Style.FILL_AND_STROKE
        for (i in 0 until grid.nbLines) {
            for (j in 0 until grid.nbColumns) {
                val cell = grid.cells[i][j]
                val values = cell.values
                // We want to draw the unique value from the initial numbers
                if (cell.enonce) {
                    assert(values.size == 1)
                    // Draw the unique value in the center of the cell
                    paint.textSize = desiredTextSizeUnique
                    canvas.drawText(values[0].toString(),
                            offsetLeft + j * cellSize + (cellSize - desiredWidthUnique) / 2,
                            offsetTop + i * cellSize + (cellSize + textHeightUnique) / 2,
                            paint)
                }
                if (cell.sister != null) {
                    paint.textSize = desiredTextSizeMultiple
                    // Draw the sister symbol on the bottom-right corner
                    desiredTextSizeMultiple = getTextSizeForWidth(desiredWidthMultiple,
                            cell.sister.toString())
                    textHeightMultiple = getTextHeightForSize(desiredTextSizeMultiple,
                            cell.sister.toString())
                    canvas.drawText(cell.sister.toString(),
                            offsetLeft + j * cellSize + (1f - PROPORTION_MARGIN_SMALL_NUMBER_CELL) * cellSize - desiredWidthMultiple,
                            offsetTop + i * cellSize + (1f - PROPORTION_MARGIN_SMALL_NUMBER_CELL) * cellSize,
                            paint)
                }

                if (cell.differenceOne != null) {
                    // TODO find the other

                }
            }
        }
    }

}