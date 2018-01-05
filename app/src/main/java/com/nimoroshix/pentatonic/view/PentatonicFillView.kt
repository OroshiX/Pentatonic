package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.nimoroshix.pentatonic.model.CellState
import com.nimoroshix.pentatonic.model.Grid
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 03/01/2018.
 */
class PentatonicFillView : PentatonicAbstractView {
    override fun update(o: Observable?, arg: Any?) {
        if (arg == Grid.VALUE) {
            invalidate()
        }
    }

    val colorSelected: Int

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.style = Paint.Style.FILL
        colorSelected = Color.parseColor("#525F9DBE")
        colorSelectedSecondary = Color.parseColor("#52C3E5F8")
        notValid = Color.RED
        valid = Color.BLUE
    }

    private val colorSelectedSecondary: Int

    private val notValid: Int
    private val valid: Int

    override fun onDraw(canvas: Canvas) {
        grid.cells.forEach { row ->
            row.forEach { cell ->
                if (!cell.enonce) {
                    // We can draw

                    // Background (selected or not)
                    when (cell.selection) {
                        CellState.UNSELECTED -> {
                        }
                        else -> {
                            when (cell.selection) {
                                CellState.SELECTED -> paint.color = colorSelected
                                CellState.SECONDARY_SELECTION -> paint.color = colorSelectedSecondary
                                else -> {
                                    // nothing because it is NOT unselected anyway
                                }
                            }
                            // it is either selected or secondary selected, so draw background
                            canvas.drawRect(cell.position.nColumn.times(cellSize).plus(offsetLeft),
                                    cell.position.nLine.times(cellSize).plus(offsetTop),
                                    (cell.position.nColumn + 1).times(cellSize).plus(offsetLeft),
                                    (cell.position.nLine + 1).times(cellSize).plus(offsetTop), paint)
                        }
                    }
                    // Is cell valid?
                    when {
                        !cell.valid -> paint.color = notValid
                        else -> paint.color = valid
                    }

                    when (cell.values.size) {
                        0 -> {// nothing
                        }
                        1 -> {
                            // Big number
                            // TODO("draw the big number")
//                            canvas.drawText(cell.values[0].toString(),)
                        }
                        else -> {
                            // Small numbers
                            // TODO("draw the small numbers")
                        }
                    }
                }
            }
        }
    }

}