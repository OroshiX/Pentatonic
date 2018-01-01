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

}