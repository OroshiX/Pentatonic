package com.nimoroshix.pentatonic.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.util.Constants.Companion.DEFAULT_KEYS
import com.nimoroshix.pentatonic.util.Constants.Companion.PREF_KEYS
import com.nimoroshix.pentatonic.util.Constants.Companion.SHARED_PREFERENCES_NAME
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 02/01/2018.
 */
class PentatonicKeyView : LinearLayout, Observer {
    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        orientation = VERTICAL

        val typeface = ResourcesCompat.getFont(context, R.font.mono)
        var button: Button
        var linearLayout: LinearLayout

        val array = Array(2, { i -> i.toChar() })
        Log.d(TAG, "On a une liste de char : ${Gson().toJson(array)}")

        val keys = getKeys(context)
        keys.forEach { row ->
            linearLayout = LinearLayout(context)
            linearLayout.orientation = HORIZONTAL
            linearLayout.weightSum = row.size.toFloat()
            row.forEach {
                assert(it.isNotEmpty())
                button = Button(context)
                button.text = it
                button.setAllCaps(false)
                button.typeface = typeface
                button.layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                button.setOnClickListener { _ -> grid.toggleValue(it[0]) }
                linearLayout.addView(button)
            }
            addView(linearLayout)
        }

        paint.style = Paint.Style.STROKE

    }

    private fun getKeys(context: Context): List<List<String>> {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val keysJson = sharedPreferences.getString(PREF_KEYS, DEFAULT_KEYS)
        val type = object : TypeToken<ArrayList<ArrayList<String>>>() {}.type
        return Gson().fromJson(keysJson, type)
    }

    companion object {
        @JvmField
        val TAG = "PentatonicKeyView"
        const val NUMBER_BUTTON_FIRST_ROW = 5
    }

    private var paint: Paint = Paint()
    var grid: Grid = Grid(1, 1)

    private var widthButton: Float = 30f
    private var heightButton: Float = 30f
    private val marginBetween: Float = 10f
    private var marginHorizontal: Float = 14f
    private var marginVertical: Float = 12f

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        widthButton = (width - (NUMBER_BUTTON_FIRST_ROW - 1) * marginBetween - 2 * marginHorizontal) / NUMBER_BUTTON_FIRST_ROW
        heightButton = (height - marginBetween - 2 * marginVertical) / 2
    }
}