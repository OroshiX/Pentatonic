package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.model.CellState
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.util.Constants.Companion.COLOR_SELECTED
import com.nimoroshix.pentatonic.util.Constants.Companion.COLOR_SELECTED_SECONDARY
import com.nimoroshix.pentatonic.util.TouchUtils
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 03/01/2018.
 */
class PentatonicFillView : PentatonicAbstractView, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {
    override fun onShowPress(p0: MotionEvent?) {
        Log.v(TAG, "onShowPress($p0)")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.v(TAG, "onSingleTapUp($event)")
        val pos: Position? = TouchUtils.touchToPosition(event.x, event.y, offsetLeft,
                offsetTop, cellSize, grid.nbLines, grid.nbColumns)
        return if (pos != null) {
            performClick()
            grid.select(pos.nLine, pos.nColumn)
            true
        } else {
            grid.unselect()
            invalidate()
            true
        }
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        Log.v(TAG, "onDown($p0)")
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.v(TAG, "onFling($p0, $p1, $p2, $p3)")
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.v(TAG, "onScroll($p0, $p1, $p2, $p3)")
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
        Log.v(TAG, "onLongPress($p0)")
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        Log.v(TAG, "onDoubleTap($p0)")
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        Log.v(TAG, "onDoubleTapEvent($p0)")
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        Log.v(TAG, "onSingleTapConfirmed($event)")
        return true
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
        Log.v(TAG, "onScaleBegin($p0)")
        return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector?) {
        Log.v(TAG, "onScaleEnd($p0)")
    }

    override fun onScale(p0: ScaleGestureDetector?): Boolean {
        Log.v(TAG, "onScale($p0)")
        return true
    }


    companion object {
        val TAG = "PentatonicFillView"
        val a = listOf(4F, 45F, 25F, 4F, 45F)
        val b = listOf(21F, 21F, 41F, 61F, 61F)
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg == Grid.VALUE) {
            invalidate()
        }
    }

    private val typeface: Typeface

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.style = Paint.Style.FILL
        notValid = Color.RED
        valid = Color.BLUE
        typeface = ResourcesCompat.getFont(context, R.font.mono)!!
        paint.typeface = typeface
    }


    private val notValid: Int
    private val valid: Int

    override fun performClick(): Boolean {
        super.performClick()
        // Handle the action for the custom click here

        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout($changed, $left, $top, $right, $bottom)")
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawRect(offsetLeft, offsetTop, width - offsetLeft, height - offsetTop, paint)

        paint.style = Paint.Style.FILL
        for ((i, row) in grid.cells.withIndex()) {
            for ((j, cell) in row.withIndex()) {
                // Background (selected or not)
                when (cell.selection) {
                    CellState.UNSELECTED -> {
                    }
                    else                 -> {
                        when (cell.selection) {
                            CellState.SELECTED            -> paint.color = COLOR_SELECTED
                            CellState.SECONDARY_SELECTION -> paint.color = COLOR_SELECTED_SECONDARY
                            else                          -> {
                                // nothing because it is NOT unselected anyway
                            }
                        }
                        // it is either selected or secondary selected, so draw background
                        canvas.drawRect(cell.position.nColumn * cellSize + offsetLeft,
                                cell.position.nLine * cellSize + offsetTop,
                                (cell.position.nColumn + 1) * cellSize + offsetLeft,
                                (cell.position.nLine + 1) * cellSize + offsetTop,
                                paint)
                    }
                }
                if (!cell.enonce) {
                    // Is cell valid?
                    when {
                        !cell.valid -> paint.color = notValid
                        else        -> paint.color = valid
                    }

                    when (cell.values.size) {
                        0    -> {// nothing
                        }
                        1    -> {
                            // Big number
                            paint.textSize = desiredTextSizeUnique
                            canvas.drawText(cell.values[0].toString(),
                                    offsetLeft + j * cellSize + (cellSize - desiredWidthUnique) / 2,
                                    offsetTop + i * cellSize + (cellSize + textHeightUnique) / 2,
                                    paint)
                        }
                        else -> {
                            // Small numbers
                            paint.textSize = desiredTextSizeMultiple
                            for ((k, value) in cell.values.withIndex()) {
                                canvas.drawText(value.toString(),
                                        offsetLeft + cellSize * (j + a[k] / 64f),
                                        offsetTop + cellSize * (i + b[k] / 64f), paint)
                            }
                        }
                    }
                }
            }
        }
    }

}