package com.nimoroshix.pentatonic.model

import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs

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

    fun getAreaCells(cell: Cell): Set<Cell> {
        val idArea = cell.area.id
        return cells.flatten().filter { c ->
            when {
                c.area.id != idArea -> false // We want the same area
                c.position == cell.position -> false // We don't want the same cell
                else -> true
            }
        }.toSet()
    }

    fun getAreaCells(area: Area): Set<Cell> {
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

    fun unselect() {
        cells.flatten().forEach { c -> c.selection = CellState.UNSELECTED }
    }

    fun select(nLine: Int, nColumn: Int) {
        unselect()
        val cell = cells[nLine][nColumn]
        cell.selection = CellState.SELECTED
        val area = getAreaCells(cell)
        val adjacent = getAdjacentCells(nLine, nColumn)
        area.union(adjacent).forEach { c -> c.selection = CellState.SECONDARY_SELECTION }
        setChanged()
        notifyObservers(VALUE)
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

    fun getAdjacentCells(nLine: Int, nColumn: Int): Set<Cell> {
        return cells.flatten().filter { cell ->
            when {
                cell.position.nLine == nLine && cell.position.nColumn == nColumn -> false // We don't want the same cell
                abs(cell.position.nLine - nLine) <= 1 && abs(cell.position.nColumn - nColumn) <= 1 -> true // we want the cells nearby
                else -> false // and we don't want any other cell
            }
        }.toSet()
    }

    /**
     * Replace all occurrences of one char into another one in the grid
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

    /**
     * Remove all occurrences of one char in the grid
     */
    fun remove(oldValue: Char) {
        cells.flatten().forEach { c ->
            if (c.values.contains(oldValue)) {
                c.values.remove(oldValue)
            }
        }
    }

    override fun toString(): String {
        return "Grid(nbLines=$nbLines, nbColumns=$nbColumns, cells=${Arrays.deepToString(cells)})"
    }
}