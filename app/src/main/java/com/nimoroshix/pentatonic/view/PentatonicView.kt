package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer
import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class PentatonicView : View, Observer {
    override fun update(o: Observable?, arg: Any?) {
        grid = o as Grid
        if (arg == Grid.STRUCTURE)
            invalidate()
    }

    private var paint: Paint = Paint()

    private var pathEffectDotted: PathEffect = DashPathEffect(floatArrayOf(10F, 50F), 0F)
    var grid: Grid = Serializer.serialize(
            "7 6\n" +
                    "122223\n" +
                    "445533\n" +
                    "645537\n" +
                    "644587\n" +
                    "688887\n" +
                    "6999aa\n" +
                    "bb99aa\n" +
                    "×,0,1\n" +
                    "×,6,5")


//            "4 5\n" +
//            "11233\n" +
//            "11223\n" +
//            "45266\n" +
//            "55556\n" +
//            "1,2,2\n" +
//            "3,3,2\n" +
//            "a,0,2\n" +
//            "a,0,0\n" +
//            "-2,0,3,1")

    var offsetLeft: Float = 10f
    var offsetTop: Float = 10f
    var cellSize: Float = 40f

    private var desiredWidthUnique: Float = 0f
    private var desiredWidthMultiple: Float = 0f
    private var desiredTextSizeUnique: Float = 0f
    private var desiredTextSizeMultiple: Float = 0f
    private var textHeightUnique: Float = 0f
    private var textHeightMultiple: Float = 0f

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.isAntiAlias = true
        paint.strokeWidth = 5f
    }

    companion object {
        @JvmField
        val TAG = "PentatonicView"
        val PROPORTION_NUMBER_CELL: Float = 3f / 4f
        val PROPORTION_SMALL_NUMBER_CELL: Float = 1f / 5f
        val PROPORTION_MARGIN_SMALL_NUMBER_CELL: Float = 1f / 20f
    }

    /**
     * https://stackoverflow.com/questions/12166476/android-canvas-drawtext-set-font-size-from-width
     * Sets the text size for a Paint object so a given string of text will be given width.
     *
     * @param desiredWidth the desired width
     * @param text         the text that should be that width
     */
    private fun getTextSizeForWidth(desiredWidth: Float, text: String): Float {
        val testTextSize = 48f
        // Get the bounds of the text, using our testTextSize
        paint.textSize = testTextSize
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        // calculate the desired size as a proportion of our testTextSize
        return testTextSize * desiredWidth / bounds.height()
    }

    private fun getTextHeightForSize(textSize: Float, text: String): Float {
        paint.textSize = textSize
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        return bounds.height().toFloat()
    }

    fun setGrid(textGrid: String) {
        grid = Serializer.serialize(textGrid)
        resetSizeAndOffsets()
    }


    private fun resetSizeAndOffsets() {
        // Calculate cellSize
        val maxCellHeight: Float = (height - 2 * offsetLeft) / grid.nbLines
        val maxCellWidth: Float = (width - 2 * offsetTop) / grid.nbColumns
        cellSize = Math.min(maxCellHeight, maxCellWidth)

        offsetTop = (height - cellSize * grid.nbLines) / 2
        offsetLeft = (width - cellSize * grid.nbColumns) / 2

        desiredWidthUnique = cellSize * PROPORTION_NUMBER_CELL
        desiredTextSizeUnique = getTextSizeForWidth(desiredWidthUnique, "5")
        desiredWidthMultiple = cellSize * PROPORTION_SMALL_NUMBER_CELL
        desiredTextSizeMultiple = getTextSizeForWidth(desiredWidthMultiple, "5")

        textHeightUnique = getTextHeightForSize(desiredTextSizeUnique, "5")
        textHeightMultiple = getTextHeightForSize(desiredTextSizeMultiple, "5")
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // Calculate offsets
        Log.d(TAG, "onLayout($changed, $left, $top, $right, $bottom)")
        resetSizeAndOffsets()
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(TAG, "onDraw")
        canvas.drawRGB(252, 247, 219)

        // Draw a rectangle (m * cellSize) * (n * cellSize)
        paint.style = Paint.Style.STROKE

        canvas.drawRect(offsetLeft, offsetTop, offsetLeft + grid.nbColumns * cellSize,
                offsetTop + grid.nbLines * cellSize, paint)

        // Draw a dotted grid
        paint.pathEffect = pathEffectDotted
        paint.strokeWidth = 1f

        for (i in 0 until grid.nbLines) {
            canvas.drawLine(offsetLeft, offsetTop + i * cellSize,
                    offsetLeft + grid.nbColumns * cellSize, offsetTop + i * cellSize, paint)
        }

        for (j in 0 until grid.nbColumns) {
            canvas.drawLine(offsetLeft + j * cellSize, offsetTop, offsetLeft + j * cellSize,
                    offsetTop + grid.nbLines * cellSize, paint)
        }

        // Draw the different areas (bold)
        paint.strokeWidth = 6f
        paint.pathEffect = null

        // compare two horizontal cells area ids
        for (i in 0 until grid.nbLines) {
            (1 until grid.nbColumns).filter {
                // Compare the value of the area of the 2 adjacent cells
                grid.cells[i][it].area.id != grid.cells[i][it - 1].area.id
            }.forEach {
                // Different areas, so draw a line
                canvas.drawLine(offsetLeft + it * cellSize, offsetTop + i * cellSize,
                        offsetLeft + it * cellSize, offsetTop + (i + 1) * cellSize, paint)
            }
        }

        // compare two vertical cells area ids
        for (j in 0 until grid.nbColumns) {
            (1 until grid.nbLines)
                    .filter { grid.cells[it][j].area.id != grid.cells[it - 1][j].area.id }
                    .forEach {
                        canvas.drawLine(offsetLeft + j * cellSize, offsetTop + it * cellSize,
                                offsetLeft + (j + 1) * cellSize, offsetTop + it * cellSize, paint)
                    }
        }

        // Draw the initial numbers
        paint.strokeWidth = 2f
        paint.style = Paint.Style.FILL_AND_STROKE
        for (i in 0 until grid.nbLines) {
            for (j in 0 until grid.nbColumns) {
                val cell = grid.cells[i][j]
                val values = cell.values
                // We want to draw the unique value from the initial numbers
                if (cell.enonce) {
                    assert(values.size == 1)
                    // Draw the unique value in the center of the cell
                    paint.textSize = desiredTextSizeUnique
                    canvas.drawText(values[0].toString(),
                            offsetLeft + j * cellSize + (cellSize - desiredWidthUnique) / 2,
                            offsetTop + i * cellSize + (cellSize + textHeightUnique) / 2,
                            paint)
                }
                if (cell.sister != null) {
                    paint.textSize = desiredTextSizeMultiple
                    // Draw the sister symbol on the bottom-right corner
                    desiredTextSizeMultiple = getTextSizeForWidth(desiredWidthMultiple,
                            cell.sister.toString())
                    textHeightMultiple = getTextHeightForSize(desiredTextSizeMultiple,
                            cell.sister.toString())
                    canvas.drawText(cell.sister.toString(),
                            offsetLeft + j * cellSize + (1f - PROPORTION_MARGIN_SMALL_NUMBER_CELL) * cellSize - desiredWidthMultiple,
                            offsetTop + i * cellSize + (1f - PROPORTION_MARGIN_SMALL_NUMBER_CELL) * cellSize,
                            paint)
                }

                if (cell.differenceOne != null) {
                    // TODO find the other
                }
            }
        }
    }

}