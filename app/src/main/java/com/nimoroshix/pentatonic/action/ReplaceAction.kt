package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Grid

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class ReplaceAction(private var oldChar: Char, private var newChar: Char, positions: List<PositionReplace>) : MultipleAction(positions) {


    override fun applyUndo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            pos as PositionReplace
            val cell = grid.cells[pos.nLine][pos.nColumn]
            // Deduplicated means that before the replace action, we already had newChar. So the replace action had only removed oldChar
            // and not added any new char
            if (!pos.deduplicated) {
                res = cell.values.remove(newChar) && res
            }
            res = cell.values.add(oldChar) && res
        }
        return res
    }

    override fun applyRedo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            pos as PositionReplace
            val cell = grid.cells[pos.nLine][pos.nColumn]
            res = cell.values.remove(oldChar) && res
            if (!pos.deduplicated) {
                res = cell.values.add(newChar) && res
            }
        }
        return res
    }
}