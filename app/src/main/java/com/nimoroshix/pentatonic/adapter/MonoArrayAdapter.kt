package com.nimoroshix.pentatonic.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.nimoroshix.pentatonic.R

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 11/01/2018.
 */

class MonoArrayAdapter<T>(context: Context, resource: Int, objects: List<T>) : ArrayAdapter<T>(context, resource, objects) {

    private val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.mono)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View = super.getView(position, convertView, parent)
        (v as TextView).typeface = typeface
        return v
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = super.getDropDownView(position, convertView, parent)
        (v as TextView).typeface = typeface
        return v
    }

}