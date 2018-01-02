package com.nimoroshix.pentatonic.model

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Grid(var nbLines: Int, var nbColumns: Int) {
    lateinit var cells: Array<Array<Cell>>

    fun generate() {
        cells = Array(nbLines, { i ->
            Array(nbColumns, { j ->
                Cell(i, j)
            })
        })
    }

    fun getAreaCells(area: Area): List<Cell> {
        var res: List<Cell> = List(area.size, { _ -> Cell() })

        // TODO to complete
        return res
    }

    fun getAdjacentCells(cell: Cell): List<Cell> {
        var res: List<Cell> = List(0, { _ -> Cell() })
        // TODO to complete
        return res
    }

    /**
     * Replace all occurences of one char into another one in the grid
     *
     * For example:
     * alpha -> 1
     * or
     * alpha -> beta
     */
    fun replace(oldValue: Char, newValue: Char) {
        // TODO
    }

}