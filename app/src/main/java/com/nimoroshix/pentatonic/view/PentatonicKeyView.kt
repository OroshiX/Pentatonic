package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 02/01/2018.
 */
class PentatonicKeyView : LinearLayout, Observer {
    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
    }

    var views: List<View> = mutableListOf()

    var lowercase1 = listOf<Char>('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l')
    var lowercase2 = listOf<Char>('m', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z')

    var uppercase1 = lowercase1.map { c -> c.toUpperCase() }
    var uppercase2 = lowercase2.map { c -> c.toUpperCase() }

    var letterGreeks1 = listOf<Char>('α', 'β', 'γ', 'δ', 'ε', 'θ', 'λ', 'μ', 'ν', 'η', 'ι', 'κ')
    var letterGreeks2 = listOf<Char>('ζ', 'ξ', 'π', 'ρ', 'σ', 'τ', 'ο', 'υ', 'φ', 'ψ', 'χ', 'ω')

    val numbers: LinearLayout
    val letters1: LinearLayout
    val letters2: LinearLayout

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        orientation = VERTICAL
        numbers = LinearLayout(context)
        numbers.orientation = HORIZONTAL
        numbers.weightSum = 5f
        var button: Button
        for (i in '1'..'5') {
            button = Button(context)
            button.text = i.toString()
            button.setOnClickListener { _ -> grid.toggleValue(i) }
            button.layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            numbers.addView(button)
        }
        addView(numbers)
        letters1 = LinearLayout(context)
        letters1.orientation = HORIZONTAL
        letters1.weightSum = lowercase1.size.toFloat()
        for (l in lowercase1) {
            button = Button(context)
            button.text = l.toString()
            button.layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            button.setOnClickListener { _ -> grid.toggleValue(l) }
            letters1.addView(button)
        }
        addView(letters1)
        letters2 = LinearLayout(context)
        letters2.orientation = HORIZONTAL
        letters2.weightSum = lowercase2.size.toFloat()
        for (l in lowercase2) {
            button = Button(context)
            button.text = l.toString()
            button.layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            button.setOnClickListener { _ -> grid.toggleValue(l) }
            letters2.addView(button)
        }
        addView(letters2)

        paint.style = Paint.Style.STROKE

    }

    companion object {
        @JvmField
        val TAG = "PentatonicKeyView"
        val NUMBER_BUTTON_FIRST_ROW = 5
    }

    private var paint: Paint = Paint()
    var grid: Grid = Grid(1, 1)

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

}