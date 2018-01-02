package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 02/01/2018.
 */
class PentatonicKeyView : View, Observer {
    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    companion object {
        @JvmField
        val TAG = "PentatonicKeyView"
    }

    private var paint: Paint = Paint()
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas?) {

    }

}