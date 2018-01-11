package com.nimoroshix.pentatonic.model

import java.lang.Math.abs
import java.util.*
import kotlin.collections.HashSet

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

    var positionSelected: Position? = null
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
        cells[nLine][nColumn].toggleValue(value)
        setChanged()
        notifyObservers(VALUE)
    }

    fun toggleValue(value: Char) {
        if (positionSelected != null) {
            toggleValue(positionSelected!!.nLine, positionSelected!!.nColumn, value)
        }
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
        positionSelected = null
        cells.flatten().forEach { c -> c.selection = CellState.UNSELECTED }
    }

    fun select(nLine: Int, nColumn: Int) {
        unselect()
        val cell = cells[nLine][nColumn]
        cell.selection = CellState.SELECTED
        positionSelected = cell.position
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
        var res = false
        cells.flatten().forEach { c -> res = c.replace(oldValue, newValue) || res }
        if (res) {
            setChanged()
            notifyObservers(VALUE)
        }
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
        setChanged()
        notifyObservers(VALUE)
    }

    override fun toString(): String {
        return "Grid(nbLines=$nbLines, nbColumns=$nbColumns, cells=${Arrays.deepToString(cells)})"
    }

    fun reset() {
        cells.flatten().forEach { cell ->
            if (!cell.enonce) {
                cell.values.clear()
            }
        }
        setChanged()
        notifyObservers(VALUE)
    }

    fun findAllValues(): HashSet<Char> {
        val set = HashSet<Char>()
        cells.flatten().forEach { cell ->
            set.addAll(cell.values)
        }
        return set
    }


}