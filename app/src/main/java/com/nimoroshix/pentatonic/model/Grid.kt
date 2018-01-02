package com.nimoroshix.pentatonic.model

import java.util.*

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

    fun getAreaCells(area: Area): HashSet<Cell> {
        val set = HashSet<Cell>()
        (0 until cells.size).flatMapTo(set) { row ->
            (0 until cells[row].size).filter { col ->
                cells[row][col].area.id == area.id
            }.map { col ->
                cells[row][col]
            }
        }
        return set
    }

    private fun getAllAreas(): Set<Area> {
        val set = HashSet<Area>()
        (0 until cells.size).flatMapTo(set) { row ->
            (0 until cells[row].size).map { col ->
                cells[row][col].area
            }
        }
        return set
    }

    fun fillAreaSize() {
        val allAreas = getAllAreas()
        allAreas.forEach { area ->
            run {
                val areaCells = getAreaCells(area)
                val size = areaCells.size
                areaCells.forEach { c ->
                    c.area.size = size
                }
            }
        }
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

    override fun toString(): String {
        return "Grid(nbLines=$nbLines, nbColumns=$nbColumns, cells=${Arrays.deepToString(cells)})"
    }
}