package com.nimoroshix.pentatonic.action

import android.os.Parcelable
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
interface Action : Parcelable, StringSerializable {
    fun applyUndo(grid: Grid): Set<Cell>

    fun applyRedo(grid: Grid): Set<Cell>
}