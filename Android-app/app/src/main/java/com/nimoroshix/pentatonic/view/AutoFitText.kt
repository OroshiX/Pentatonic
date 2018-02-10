package com.nimoroshix.pentatonic.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import com.nimoroshix.pentatonic.R
import java.util.*


/**
 * https://stackoverflow.com/a/16174468/2075875 This class builds a new android Widget named AutoFitText which can be used instead of a TextView to
 * have the text font size in it automatically fit to match the screen width. Credits go largely to Dunni, gjpc, gregm and speedplane from
 * Stackoverflow, method has been (style-) optimized and rewritten to match android coding standards and our MBC. This version upgrades the original
 * "AutoFitTextView" to now also be adaptable to height and to accept the different TextView types (Button, TextClock etc.)
 *
 * @author pheuschk
 * @createDate: 18.04.2013
 *
 * combined with: https://stackoverflow.com/a/7197867/2075875
 */
class AutoFitText : TextView {

    /** Global min and max for text size. Remember: values are in pixels!  */
    private val MIN_TEXT_SIZE = 10
    private val MAX_TEXT_SIZE = 400

    /** Flag for singleLine  */
    private var mSingleLine = false

    /**
     * A dummy [TextView] to test the text size without actually showing anything to the user
     */
    private var mTestView: TextView? = null

    /**
     * A dummy [Paint] to test the text size without actually showing anything to the user
     */
    private var mTestPaint: Paint? = null

    /**
     * Scaling factor for fonts. It's a method of calculating independently (!) from the actual density of the screen that is used so users have the
     * same experience on different devices. We will use DisplayMetrics in the Constructor to get the value of the factor and then calculate SP from
     * pixel values
     */
    private var mScaledDensityFactor: Float = 0.toFloat()

    /**
     * Defines how close we want to be to the factual size of the Text-field. Lower values mean higher precision but also exponentially higher
     * computing cost (more loop runs)
     */
    private val mThreshold = 0.5f

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context,
            attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs,
            defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        //TextViewPlus part https://stackoverflow.com/a/7197867/2075875
        val a = context.obtainStyledAttributes(attrs, R.styleable.AutoFitText)
        val customFont = a.getString(R.styleable.AutoFitText_customFont)
        setCustomFont(context, customFont)
        a.recycle()

        // AutoFitText part
        mScaledDensityFactor = context.getResources().getDisplayMetrics().scaledDensity
        mTestView = TextView(context)

        mTestPaint = Paint()
        mTestPaint!!.set(this.paint)

        this.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                // make an initial call to onSizeChanged to make sure that refitText is triggered
                onSizeChanged(this@AutoFitText.width, this@AutoFitText.height, 0, 0)
                // Remove the LayoutListener immediately so we don't run into an infinite loop
                //AutoFitText.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                removeOnGlobalLayoutListener(this@AutoFitText, this)
            }
        })
    }

    fun setCustomFont(ctx: Context, asset: String?): Boolean {
        var tf: Typeface? = null
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset)
        } catch (e: Exception) {
            Log.e(TAG, "Could not get typeface: " + e.message)
            return false
        }

        typeface = tf
        return true
    }

    /**
     * Main method of this widget. Resizes the font so the specified text fits in the text box assuming the text box has the specified width. This is
     * done via a dummy text view that is refit until it matches the real target width and height up to a certain threshold factor
     *
     * @param targetFieldWidth The width that the TextView currently has and wants filled
     * @param targetFieldHeight The width that the TextView currently has and wants filled
     */
    private fun refitText(text: String, targetFieldWidth: Int, targetFieldHeight: Int) {
        var targetFieldWidth = targetFieldWidth
        var targetFieldHeight = targetFieldHeight

        // Variables need to be visible outside the loops for later use. Remember size is in pixels
        var lowerTextSize = MIN_TEXT_SIZE.toFloat()
        var upperTextSize = MAX_TEXT_SIZE.toFloat()

        // Force the text to wrap. In principle this is not necessary since the dummy TextView
        // already does this for us but in rare cases adding this line can prevent flickering
        this.maxWidth = targetFieldWidth

        // Padding should not be an issue since we never define it programmatically in this app
        // but just to to be sure we cut it off here
        targetFieldWidth = targetFieldWidth - this.paddingLeft - this.paddingRight
        targetFieldHeight = targetFieldHeight - this.paddingTop - this.paddingBottom

        // Initialize the dummy with some params (that are largely ignored anyway, but this is
        // mandatory to not get a NullPointerException)
        mTestView!!.layoutParams = LayoutParams(targetFieldWidth, targetFieldHeight)

        // maxWidth is crucial! Otherwise the text would never line wrap but blow up the width
        mTestView!!.maxWidth = targetFieldWidth

        if (mSingleLine) {
            // the user requested a single line. This is very easy to do since we primarily need to
            // respect the width, don't have to break, don't have to measure...

            /*************************** Converging algorithm 1  */
            run {
                var testSize: Float
                while (upperTextSize - lowerTextSize > mThreshold) {

                    // Go to the mean value...
                    testSize = (upperTextSize + lowerTextSize) / 2

                    mTestView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                            testSize / mScaledDensityFactor)
                    mTestView!!.text = text
                    mTestView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

                    if (mTestView!!.measuredWidth >= targetFieldWidth) {
                        upperTextSize = testSize // Font is too big, decrease upperSize
                    } else {
                        lowerTextSize = testSize // Font is too small, increase lowerSize
                    }
                }
            }
            /** */

            // In rare cases with very little letters and width > height we have vertical overlap!
            mTestView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            if (mTestView!!.measuredHeight > targetFieldHeight) {
                upperTextSize = lowerTextSize
                lowerTextSize = MIN_TEXT_SIZE.toFloat()

                /*************************** Converging algorithm 1.5  */
                var testSize: Float
                while (upperTextSize - lowerTextSize > mThreshold) {

                    // Go to the mean value...
                    testSize = (upperTextSize + lowerTextSize) / 2

                    mTestView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                            testSize / mScaledDensityFactor)
                    mTestView!!.text = text
                    mTestView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

                    if (mTestView!!.measuredHeight >= targetFieldHeight) {
                        upperTextSize = testSize // Font is too big, decrease upperSize
                    } else {
                        lowerTextSize = testSize // Font is too small, increase lowerSize
                    }
                }
                /** */
            }
        } else {

            /*********************** Converging algorithm 2  */
            // Upper and lower size converge over time. As soon as they're close enough the loop
            // stops
            // TODO probe the algorithm for cost (ATM possibly O(n^2)) and optimize if possible
            var testSize: Float
            while (upperTextSize - lowerTextSize > mThreshold) {

                // Go to the mean value...
                testSize = (upperTextSize + lowerTextSize) / 2

                // ... inflate the dummy TextView by setting a scaled textSize and the text...
                mTestView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, testSize / mScaledDensityFactor)
                mTestView!!.text = text

                // ... call measure to find the current values that the text WANTS to occupy
                mTestView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val tempHeight = mTestView!!.measuredHeight
                // int tempWidth = mTestView.getMeasuredWidth();

                // LOG.debug("Measured: " + tempWidth + "x" + tempHeight);
                // LOG.debug("TextSize: " + testSize / mScaledDensityFactor);

                // ... decide whether those values are appropriate.
                if (tempHeight >= targetFieldHeight) {
                    upperTextSize = testSize // Font is too big, decrease upperSize
                } else {
                    lowerTextSize = testSize // Font is too small, increase lowerSize
                }
            }
            /** */

            // It is possible that a single word is wider than the box. The Android system would
            // wrap this for us. But if you want to decide fo yourself where exactly to break or to
            // add a hyphen or something than you're going to want to implement something like this:
            mTestPaint!!.textSize = lowerTextSize
            val words = ArrayList<String>()

            for (s in text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                Log.i("tag", "Word: " + s)
                words.add(s)
            }
            for (word in words) {
                if (mTestPaint!!.measureText(word) >= targetFieldWidth) {
                    val pieces = ArrayList<String>()
                    // pieces = breakWord(word, mTestPaint.measureText(word), targetFieldWidth);

                    // Add code to handle the pieces here...
                }
            }
        }

        /**
         * We are now at most the value of threshold away from the actual size. To rather undershoot than overshoot use the lower value. To match
         * different screens convert to SP first. See [://developer.android.com/guide/topics/resources/more-resources.html#Dimension][http] for
         * more details
         */
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, lowerTextSize / mScaledDensityFactor)
        return
    }

    /**
     * This method receives a call upon a change in text content of the TextView. Unfortunately it is also called - among others - upon text size
     * change which means that we MUST NEVER CALL [.refitText] from this method! Doing so would result in an endless loop that would
     * ultimately result in a stack overflow and termination of the application
     *
     * So for the time being this method does absolutely nothing. If you want to notify the view of a changed text call [.setText]
     */
    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int,
                               lengthAfter: Int) {
        // Super implementation is also intentionally empty so for now we do absolutely nothing here
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        if (width != oldWidth && height != oldHeight) {
            refitText(this.text.toString(), width, height)
        }
    }

    /**
     * This method is guaranteed to be called by [TextView.setText] immediately. Therefore we can safely add our modifications
     * here and then have the parent class resume its work. So if text has changed you should always call [TextView.setText] or
     * [TextView.setText] if you know whether the [BufferType] is normal, editable or spannable. Note: the
     * method will default to [BufferType.NORMAL] if you don't pass an argument.
     */
    override fun setText(text: CharSequence, type: BufferType) {

        val targetFieldWidth = this.width
        val targetFieldHeight = this.height

        if (targetFieldWidth <= 0 || targetFieldHeight <= 0 || text == "") {
            // Log.v("tag", "Some values are empty, AutoFitText was not able to construct properly");
        } else {
            refitText(text.toString(), targetFieldWidth, targetFieldHeight)
        }
        super.setText(text, type)
    }

    /**
     * TODO add sensibility for [.setMaxLines] invocations
     */
    override fun setMaxLines(maxLines: Int) {
        // TODO Implement support for this. This could be relatively easy. The idea would probably
        // be to manipulate the targetHeight in the refitText-method and then have the algorithm do
        // its job business as usual. Nonetheless, remember the height will have to be lowered
        // dynamically as the font size shrinks so it won't be a walk in the park still
        if (maxLines == 1) {
            this.setSingleLine(true)
        } else {
            throw UnsupportedOperationException(
                    "MaxLines != 1 are not implemented in AutoFitText yet, use TextView instead")
        }
    }

    override fun setSingleLine(singleLine: Boolean) {
        // save the requested value in an instance variable to be able to decide later
        mSingleLine = singleLine
        super.setSingleLine(singleLine)
    }

    companion object {

        private val TAG = AutoFitText::class.java.simpleName

        @SuppressLint("NewApi")
        fun removeOnGlobalLayoutListener(v: View,
                                         listener: ViewTreeObserver.OnGlobalLayoutListener) {

            v.viewTreeObserver.removeOnGlobalLayoutListener(listener)

        }
    }
}