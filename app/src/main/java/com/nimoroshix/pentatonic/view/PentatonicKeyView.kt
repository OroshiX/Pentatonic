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

    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.style = Paint.Style.STROKE
    }

    companion object {
        @JvmField
        val TAG = "PentatonicKeyView"
        val NUMBER_BUTTON_FIRST_ROW = 5
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

    var widthButton: Float = 30f
    var heightButton: Float = 30f
    val marginBetween: Float = 10f
    var marginHorizontal: Float = 14f
    var marginVertical: Float = 12f

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        widthButton = (width - (NUMBER_BUTTON_FIRST_ROW - 1) * marginBetween - 2 * marginHorizontal) / NUMBER_BUTTON_FIRST_ROW
        heightButton = (height - marginBetween - 2 * marginVertical) / 2
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {

        for (i in 0 until NUMBER_BUTTON_FIRST_ROW) {
            canvas.drawRect(marginHorizontal + i * widthButton + i * marginBetween, marginVertical,
                    marginHorizontal + (i + 1) * widthButton + i * marginBetween,
                    marginVertical + heightButton, paint)
        }
    }

}