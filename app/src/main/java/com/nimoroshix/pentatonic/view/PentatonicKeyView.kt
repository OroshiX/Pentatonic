package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.model.Grid
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 02/01/2018.
 */
class PentatonicKeyView : LinearLayout, Observer {
    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
    }

    val numbers: LinearLayout

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        orientation = VERTICAL
        numbers = LinearLayout(context)
        numbers.orientation = HORIZONTAL
        numbers.weightSum = 5f
        val typeface = ResourcesCompat.getFont(context, R.font.mono)
        var button: Button
        for (i in '1'..'5') {
            button = Button(context)
            button.text = i.toString()
            button.setOnClickListener { _ -> grid.toggleValue(i) }
            button.typeface = typeface
            button.layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            numbers.addView(button)
        }
        addView(numbers)
        var linearLayout: LinearLayout
        val inputStream: InputStream = context.resources.openRawResource(R.raw.keyboard)
        val reader = InputStreamReader(inputStream)
        reader.forEachLine { line ->
            linearLayout = LinearLayout(context)
            linearLayout.orientation = HORIZONTAL
            linearLayout.weightSum = line.length.toFloat()
            line.forEach { c ->
                button = Button(context)
                button.text = c.toString()
                button.typeface = typeface
                button.layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                button.setOnClickListener { _ -> grid.toggleValue(c) }
                linearLayout.addView(button)
            }
            addView(linearLayout)
        }

        paint.style = Paint.Style.STROKE

    }

    companion object {
        @JvmField
        val TAG = "PentatonicKeyView"
        val NUMBER_BUTTON_FIRST_ROW = 5
    }

    private var paint: Paint = Paint()
    var grid: Grid = Grid(1, 1)

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
}