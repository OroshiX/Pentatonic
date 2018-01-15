package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class RemoveMultipleAction(var char: Char, positions: List<Position>) : MultipleAction(positions) {
    override fun applyUndo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            val cell = grid.cells[pos.nLine][pos.nColumn]
            if (cell.values.contains(char)) {
                res = false
            } else {
                res = cell.values.add(char) && res
            }
        }
        return res
    }

    override fun applyRedo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            val cell = grid.cells[pos.nLine][pos.nColumn]
            if (cell.values.contains(char)) {
                res = cell.values.remove(char) && res
            } else {
                res = false
            }
        }
        return res
    }
}