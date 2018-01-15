package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Grid

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
interface Action {
    fun applyUndo(grid: Grid): Boolean

    fun applyRedo(grid: Grid): Boolean

}