package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class RemoveAction(char: Char, position: Position) : SingleAction("REMOVE", char, position) {
    override fun applyUndo(grid: Grid): Boolean {
        val cell = grid.cells[position.nLine][position.nColumn]
        if (cell.values.contains(char)) return false
        return cell.values.add(char)
    }

    override fun applyRedo(grid: Grid): Boolean {
        return grid.cells[position.nLine][position.nColumn].values.remove(char)
    }

}