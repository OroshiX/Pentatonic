package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    private var grid: Grid = Grid(5,6)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        canvas.drawRGB(255, 255, 0)

        val width = width
        paint.setARGB(255, 255, 0, 0)
        canvas.drawLine(0f, 30f, width.toFloat(), 30f, paint)
        paint.strokeWidth = 4f
        canvas.drawLine(0f, 60f, width.toFloat(), 60f, paint)
    }

}