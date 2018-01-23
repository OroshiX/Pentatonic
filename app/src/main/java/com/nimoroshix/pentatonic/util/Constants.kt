package com.nimoroshix.pentatonic.util

import android.graphics.Color

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 05/01/2018.
 */
class Constants {
    companion object {
        val PROPORTION_NUMBER_CELL: Float = 3f / 4f
        val PROPORTION_HINT_SMALL_NUMBER_CELL: Float = 1f / 5f
        val PROPORTION_SMALL_NUMBER_CELL: Float = 9f / 32f
        val PROPORTION_MARGIN_SMALL_NUMBER_CELL: Float = 1f / 20f
        val COLOR_SELECTED: Int = Color.parseColor("#5541A5E1")
        //        val COLOR_SELECTED: Int = Color.parseColor("#525F9DBE")
        val COLOR_SELECTED_SECONDARY: Int = Color.parseColor("#5287C8EE")
        //        val COLOR_SELECTED_SECONDARY: Int = Color.parseColor("#52C3E5F8")
        val MAX_SIZE = 5
        val PATH_PENTA = "pentatonic"
        val MAX_DIFFICULTY = 5
    }
}