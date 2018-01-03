package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.nimoroshix.pentatonic.model.Grid
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 03/01/2018.
 */
class PentatonicFillView : View, Observer {
    private lateinit var grid: Grid

    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
        if (arg == Grid.VALUE)
            invalidate()
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        canvas.toString()
    }

}