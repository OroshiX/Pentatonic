package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class ResetAction(var allChars: List<Char>, var position: Position) : Action {

    override fun applyUndo(grid: Grid): Boolean {
        var res = true
        allChars.forEach { c ->
            res = grid.cells[position.nLine][position.nColumn].values.add(c) && res
        }
        return res
    }

    override fun applyRedo(grid: Grid): Boolean {
        var res = true
        allChars.forEach { c ->
            res = grid.cells[position.nLine][position.nColumn].values.remove(c) && res
        }
        return res
    }
}