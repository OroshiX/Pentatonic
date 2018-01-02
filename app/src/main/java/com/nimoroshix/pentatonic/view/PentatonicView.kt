package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class PentatonicView : View, Observer {
    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
        if (arg as String == Grid.STRUCTURE)
            invalidate()
    }

    companion object {
        @JvmField
        val TAG = "PentatonicView"
    }

    var offsetLeft: Float = 10f
    var offsetTop: Float = 10f
    var cellSize: Float = 40f

    private var paint: Paint = Paint()
    private var pathEffectDotted: PathEffect = DashPathEffect(floatArrayOf(10F, 50F), 0F)
    var grid: Grid = Serializer.serialize("4 5\n" +
            "11233\n" +
            "11223\n" +
            "45266\n" +
            "55556\n" +
            "1,2,2\n" +
            "3,3,2\n" +
            "a,0,2\n" +
            "a,0,0\n" +
            "-2,0,3,1")

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    fun setGrid(textGrid: String) {
        grid = Serializer.serialize(textGrid)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // Calculate offsets
        Log.d(TAG, "onLayout($changed, $left, $top, $right, $bottom)")

        // Calculate cellSize
        val maxCellHeight: Float = (height - 2 * offsetLeft) / grid.nbLines
        val maxCellWidth: Float = (width - 2 * offsetTop) / grid.nbColumns
        cellSize = Math.min(maxCellHeight, maxCellWidth)

        offsetTop = (height - cellSize * grid.nbLines) / 2
        offsetLeft = (width - cellSize * grid.nbColumns) / 2

    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        canvas.drawRGB(252, 247, 219)

        // Draw a rectangle (m * cellSize) * (n * cellSize)
        paint.strokeWidth = 5f
//        paint.pathEffect = null
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
    }

}