package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.nimoroshix.pentatonic.model.CellState
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.util.Constants.Companion.COLOR_SELECTED
import com.nimoroshix.pentatonic.util.Constants.Companion.COLOR_SELECTED_SECONDARY
import com.nimoroshix.pentatonic.util.TouchUtils
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 03/01/2018.
 */
class PentatonicFillView : PentatonicAbstractView {
    companion object {
        val TAG = "PentatonicFillView"
        val a = listOf<Float>(4F, 45F, 25F, 4F, 45F)
        val b = listOf<Float>(21F, 21F, 41F, 61F, 61F)
    }

    override fun update(o: Observable?, arg: Any?) {

        if (arg == Grid.VALUE) {
            invalidate()
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.style = Paint.Style.FILL
        notValid = Color.RED
        valid = Color.BLUE
    }


    private val notValid: Int
    private val valid: Int

    override fun performClick(): Boolean {
        super.performClick()
        // Handle the action for the custom click here

        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                return false
            }
            MotionEvent.ACTION_DOWN -> {
                val pos: Position? = TouchUtils.touchToPosition(event.x, event.y, offsetLeft,
                        offsetTop, cellSize, grid.nbLines, grid.nbColumns)
                return if (pos != null) {
                    Log.d(TAG, "We got position: $pos")
                    performClick()
                    grid.select(pos.nLine, pos.nColumn)
                    true
                } else {
                    grid.unselect()
                    invalidate()
                    true
                }
            }
            MotionEvent.ACTION_UP -> {
                return false
            }
            else -> return false
        }
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        for ((i, row) in grid.cells.withIndex()) {
            for ((j, cell) in row.withIndex()) {
                // Background (selected or not)
                when (cell.selection) {
                    CellState.UNSELECTED -> {
                    }
                    else -> {
                        when (cell.selection) {
                            CellState.SELECTED -> paint.color = COLOR_SELECTED
                            CellState.SECONDARY_SELECTION -> paint.color = COLOR_SELECTED_SECONDARY
                            else -> {
                                // nothing because it is NOT unselected anyway
                            }
                        }
                        // it is either selected or secondary selected, so draw background
                        canvas.drawRect(cell.position.nColumn.times(cellSize).plus(offsetLeft),
                                cell.position.nLine.times(cellSize).plus(offsetTop),
                                (cell.position.nColumn + 1).times(cellSize).plus(offsetLeft),
                                (cell.position.nLine + 1).times(cellSize).plus(offsetTop),
                                paint)
                    }
                }
                if (!cell.enonce) {
                    // Is cell valid?
                    when {
                        !cell.valid -> paint.color = notValid
                        else -> paint.color = valid
                    }

                    when (cell.values.size) {
                        0 -> {// nothing
                            Log.d(TAG, "size=0")
                        }
                        1 -> {
                            // Big number
                            paint.textSize = desiredTextSizeUnique
                            Log.d(TAG, "drawing this big number: ${cell.values[0].toString()}")
                            canvas.drawText(cell.values[0].toString(),
                                    offsetLeft + j * cellSize + (cellSize - desiredWidthUnique) / 2,
                                    offsetTop + i * cellSize + (cellSize + textHeightUnique) / 2,
                                    paint)
                        }
                        else -> {
                            // Small numbers
                            paint.textSize = desiredTextSizeMultiple
                            for ((k, value) in cell.values.withIndex()) {
                                canvas.drawText(value.toString(),
                                        offsetLeft + cellSize * (j + a[k] / 64f),
                                        offsetTop + cellSize * (i + b[k] / 64f), paint)
                            }
                        }
                    }
                }
            }
        }
    }

}