package com.nimoroshix.pentatonic.model

import java.util.*

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Grid(var nbLines: Int, var nbColumns: Int) : Observable() {
    companion object {
        @JvmField
        val STRUCTURE = "structure"
        @JvmField
        val VALUE = "value"
        @JvmField
        val SELECTED = "selected"
    }

    var cells: Array<Array<Cell>> = Array(nbLines, { i ->
        Array(nbColumns, { j ->
            Cell(i, j)
        })
    })
        set(value) {
            field = value
            setChanged()
            notifyObservers(STRUCTURE)
        }

    fun toggleValue(nLine: Int, nColumn: Int, value: Char) {
        val values = cells[nLine][nColumn].values
        if (values.contains(value)) {
            values.remove(value)
        } else {
            values.add(value)
        }
        cells[nLine][nColumn].dirty = true
        setChanged()
        notifyObservers(VALUE)
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
        cells.forEach { row ->
            row.forEach { c ->
                if (c.values.contains(oldValue)) {
                    c.values.forEachIndexed { index, t ->
                        if (t == oldValue) {
                            c.values[index] = newValue
                        }
                    }
                }
            }
        }
        setChanged()
        notifyObservers(VALUE)
    }

    override fun toString(): String {
        return "Grid(nbLines=$nbLines, nbColumns=$nbColumns, cells=${Arrays.deepToString(cells)})"
    }
}