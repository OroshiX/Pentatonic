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

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class PentatonicView : View {
    companion object {
        @JvmField
        val TAG = "PentatonicView"
    }

    private var paint: Paint = Paint()
    var grid: Grid = Grid(5, 6)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var pathEffectDotted: PathEffect = DashPathEffect(floatArrayOf(10F, 20F), 0F)

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        canvas.drawRGB(252, 247, 219)
        val width = width

        // Calculate offsets
        var offsetLeft = 10F
        var offsetTop = 10F

        // Calculate cellSize
        var cellSize = 60 // TODO calculate it dynamically
        // Draw a rectangle (m * cellSize) * (n * cellSize)
        paint.strokeWidth = 5f
//        paint.pathEffect = null
        paint.style = Paint.Style.STROKE

        canvas.drawRect(offsetLeft, offsetTop, offsetLeft + grid.nbColumns * cellSize, offsetTop + grid.nbLines * cellSize, paint)

        // Draw a dotted grid
        paint.pathEffect = pathEffectDotted
        paint.strokeWidth = 1f

        for (i in 0 until grid.nbLines) {
            canvas.drawLine(offsetLeft, offsetTop + i * cellSize, offsetLeft + grid.nbColumns * cellSize, offsetTop + i * cellSize, paint)
        }

        for (j in 0 until grid.nbColumns) {
            canvas.drawLine(offsetLeft + j * cellSize, offsetTop, offsetLeft + j * cellSize, offsetTop + grid.nbLines * cellSize, paint)
        }
    }

}