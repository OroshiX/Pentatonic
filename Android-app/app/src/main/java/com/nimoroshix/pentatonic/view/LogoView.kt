package com.nimoroshix.pentatonic.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.util.ViewUtils
import kotlin.math.min

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 17/01/2018.
 */
class LogoView : View {
    private val typefacePenta: Typeface?
    private val typefaceSignature: Typeface?
    private var padding: Float

    private val proportionPentaWidth = 3f / 4f
    private val proportionSignatureWidth = 1f / 2f
    private val proportionIconWidth = 1f / 3f

    private val minSizeText = 10f

    private var bitmap: Bitmap
    private var paint: Paint = Paint()

    private var baselinePos = 0f
    private var heightPenta = 0f
    private var heightSignature = 0f

    private var widthPenta = 0f
    private var widthSignature = 0f

    private var textSizePenta = 0f
    private var textSizeSignature = 0f
    private var descentSignature = 0f

    private val PENTATONIC_TEXT = "Pentatonic"
    private val SIGNATURE_TEXT = "by nim0roshix"

    private val proportionWidthHeight = 3f / 2f

    private val colorPentatonic: Int
    private val colorBackground: Int
    private val colorShadow: Int

    companion object {
        @JvmField
        val TAG = "LogoView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        paint.textSize = 100f
        paint.strokeWidth = 3f
        padding = 50f
        typefacePenta = ResourcesCompat.getFont(context, R.font.akashi)
        typefaceSignature = ResourcesCompat.getFont(context, R.font.ubuntutitle)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_redo)
        colorPentatonic = ResourcesCompat.getColor(resources, R.color.colorAccent, context.theme)
        colorBackground = ResourcesCompat.getColor(resources, R.color.colorPrimary, context.theme)
        colorShadow = ResourcesCompat.getColor(resources, R.color.shadow, context.theme)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var desiredHeight = 100f
        var desiredWidth = 100f
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSize == 0) throw IllegalArgumentException("You should always specify width > 0 in LogoView")

        val res = proportionWidthHeight * heightSize - widthSize
        when {
            res < 0 -> desiredWidth = proportionWidthHeight * heightSize
            res > 0 -> desiredHeight = widthSize / proportionWidthHeight
        }

        var width = 0
        var height = 0

        when (widthMode) {
            MeasureSpec.EXACTLY -> width = widthSize
            MeasureSpec.AT_MOST -> width = min(widthSize, desiredWidth.toInt())
            MeasureSpec.UNSPECIFIED -> width = desiredWidth.toInt()
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> height = heightSize
            MeasureSpec.AT_MOST -> height = min(heightSize, desiredHeight.toInt())
            MeasureSpec.UNSPECIFIED -> height = desiredHeight.toInt()
        }

        setMeasuredDimension(width, height)

        initMeasurements(width, height)
    }


    private fun initMeasurements(width: Int, height: Int) {

        paint.style = Paint.Style.FILL
        padding = min((height.toFloat() - 2 * minSizeText) / 4f, padding)
        if (padding < 0) padding = 0f

        paint.typeface = typefacePenta
        textSizePenta = ViewUtils.getFitTextSize(width.toFloat() * proportionPentaWidth - 2 * padding, height.toFloat() / 2 - 2 * padding, PENTATONIC_TEXT, paint)
        paint.textSize = textSizePenta
        baselinePos = -paint.fontMetrics.ascent
        heightPenta = paint.fontMetrics.bottom - paint.fontMetrics.top + paint.fontMetrics.leading
        widthPenta = paint.measureText(PENTATONIC_TEXT)

        //
        paint.typeface = typefaceSignature
        textSizeSignature = ViewUtils.getFitTextSize(width.toFloat() * proportionSignatureWidth - 2 * padding, height.toFloat() / 2 - 2 * padding, SIGNATURE_TEXT, paint)
        paint.textSize = textSizeSignature
        heightSignature = paint.fontMetrics.bottom - paint.fontMetrics.top + paint.fontMetrics.leading
        descentSignature = paint.fontMetrics.descent
        widthSignature = paint.measureText(SIGNATURE_TEXT)

        val maxHeightIcon = height - 2 * padding - heightPenta //- heightSignature
        val maxWidthIcon = (1f - proportionSignatureWidth) * width
        val preferredWidthIcon = width * proportionIconWidth
        val sizeIcon = min(maxWidthIcon, min(maxHeightIcon, preferredWidthIcon)).toInt()
        bitmap = Bitmap.createScaledBitmap(bitmap, sizeIcon, sizeIcon, false)
    }


    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.FILL

        // Draw shadow
        paint.color = colorShadow
        canvas.drawRoundRect(5f, 10f, width.toFloat(), height.toFloat(), 25f, 25f, paint)

        // draw background
        paint.color = colorBackground
        canvas.drawRoundRect(0f, 0f, width.toFloat() - 5, height.toFloat() - 10, 25f, 25f, paint)
//        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        paint.typeface = typefacePenta
        paint.textSize = textSizePenta
        paint.color = colorPentatonic

        canvas.drawText(PENTATONIC_TEXT, padding, baselinePos + padding, paint)

        paint.typeface = typefaceSignature
        paint.textSize = textSizeSignature

        canvas.drawText(SIGNATURE_TEXT, width - padding - widthSignature, height - descentSignature - padding, paint)

        // draw a pentatonic in the middle
        canvas.drawBitmap(bitmap, padding, padding + heightPenta, paint)
    }
}