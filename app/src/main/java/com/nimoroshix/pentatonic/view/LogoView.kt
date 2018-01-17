package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.util.ViewUtils

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 17/01/2018.
 */
class LogoView : View {
    private val typefacePenta: Typeface?
    private val typefaceSignature: Typeface?
    private val padding: Float

    private val proportionPentaWidth = 3f / 4f
    private val proportionSignatureWidth = 1f / 2f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.color = Color.RED
        paint.textSize = 100f
        paint.strokeWidth = 3f
        padding = 50f
        typefacePenta = ResourcesCompat.getFont(context, R.font.akashi)
        typefaceSignature = ResourcesCompat.getFont(context, R.font.ubuntutitle)
    }

    private var paint: Paint = Paint()

    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        paint.typeface = typefacePenta
        var size = ViewUtils.getFitTextSize(width.toFloat() * proportionPentaWidth - 2 * padding, height.toFloat() / 2 - 2 * padding, "Pentatonic", paint)
        paint.style = Paint.Style.FILL
        paint.textSize = size
        var baselinePos = -paint.fontMetrics.ascent
        val heightPenta = paint.fontMetrics.bottom - paint.fontMetrics.top + paint.fontMetrics.leading
        canvas.drawText("Pentatonic", padding, baselinePos + padding, paint)

        paint.typeface = typefaceSignature
        size = ViewUtils.getFitTextSize(width.toFloat() * proportionSignatureWidth - 2 * padding, height.toFloat() / 2 - 2 * padding, "by nim0roshix", paint)
        paint.textSize = size
        baselinePos = -paint.fontMetrics.ascent
        canvas.drawText("by nim0roshix", width - padding - paint.measureText("by nim0roshix"), heightPenta + padding + baselinePos, paint)


    }
}