package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.*
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

    private enum class Mode {
        NONE, DRAG, ZOOM
    }

    private var viewMatrix: Matrix = Matrix()
    private var mode = Mode.NONE
    private var scale = 1.0f
    private var lastScaleFactor = 1f

    // Where the finger first touches the screen
    private var startX = 0f
    private var startY = 0f

    // Focus point
    private var scaleFocusX = 0f
    private var scaleFocusY = 0f

    // How much to translate the canvas
    private var dx = 0f
    private var dy = 0f
    private var prevDx = 0f
    private var prevDy = 0f
    private var isScaling = false

    override fun onShowPress(p0: MotionEvent?) {
        Log.v(TAG, "onShowPress($p0)")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.v(TAG, "onSingleTapUp(${event.x}, ${event.y})")
        event.transform(grid.invertMatrix)
        Log.v(TAG, "after transform: (${event.x}, ${event.y})")

        val pos: Position? = TouchUtils.touchToPosition(event.x, event.y, offsetLeft,
                offsetTop, cellSize, grid.nbLines, grid.nbColumns)
        if (pos != null) {
            performClick()
            grid.select(pos.nLine, pos.nColumn)
        } else {
            grid.unselect()
            invalidate()
        }
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        Log.v(TAG, "onDown($p0)")
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.v(TAG, "onFling($p0, $p1, $p2, $p3)")
        return true
    }

    override fun onScroll(firstEvent: MotionEvent, currentMotionEvent: MotionEvent,
                          dxFromLastScroll: Float, dyFromLastScroll: Float): Boolean {
        Log.v(TAG, "Scrolling: dx= $dxFromLastScroll, dy = $dyFromLastScroll")
        grid.addTranslation(-dxFromLastScroll, -dyFromLastScroll)
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Log.v(TAG, "onLongPress($event)")
        val pos: Position? = TouchUtils.touchToPosition(event.x, event.y, offsetLeft, offsetTop, cellSize, grid.nbLines, grid.nbColumns)
        if (pos != null) {
            performClick()
            grid.select(pos.nLine, pos.nColumn)
            grid.resetCell()
        }
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

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        Log.v(TAG, "onScaleBegin($detector)")
        startX = detector.focusX
        startY = detector.focusY
        isScaling = true
        return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector?) {
        Log.v(TAG, "onScaleEnd($p0)")
        isScaling = false
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        Log.v(TAG, "onScale($detector)")
        val scaleFactor = detector.scaleFactor

        if (lastScaleFactor == 0f || Math.signum(scaleFactor) == Math.signum(lastScaleFactor)) {
            scale *= scaleFactor
            // Don't let the object get too small or too large
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM))
            scaleFocusX = detector.focusX
            scaleFocusY = detector.focusY
            dx = scaleFocusX - startX
            dy = scaleFocusY - startY

            lastScaleFactor = scaleFactor
        } else {
            lastScaleFactor = 0f
        }
        grid.setScales(scale, dx, dy, scaleFocusX, scaleFocusY)
        return true
    }


    companion object {
        const val TAG = "PentatonicFillView"
        val a = listOf(4F, 45F, 25F, 4F, 45F)
        val b = listOf(21F, 21F, 41F, 61F, 61F)
        private const val MIN_ZOOM = 0.9f
        private const val MAX_ZOOM = 4.0f
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg == Grid.VALUE || arg == Grid.ZOOM) {
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

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw, viewMatrix = ${grid.viewMatrix}")
//        canvas.save()
        canvas.concat(grid.viewMatrix)
//        canvas.scale(grid.scale, grid.scale, grid.scaleFocusX, grid.scaleFocusY)
//        canvas.translate(grid.dx, grid.dy)

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