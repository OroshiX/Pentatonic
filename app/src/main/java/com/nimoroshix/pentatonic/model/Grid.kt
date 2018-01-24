package com.nimoroshix.pentatonic.model

import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.action.*
import com.nimoroshix.pentatonic.util.parcelableCreator
import java.lang.Math.abs
import java.util.*
import kotlin.collections.HashSet

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Grid(var nbLines: Int, var nbColumns: Int) : Observable(), Parcelable {
    companion object {
        @JvmField
        val STRUCTURE = "structure"
        @JvmField
        val VALUE = "value"
        @JvmField
        val SELECTED = "selected"
        @JvmField
        val CREATOR = parcelableCreator(::Grid)
    }

    private var positionSelected: Position? = null
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
    var version: Int = 0
    var difficulty: Int = 0
    var filename: String = ""
    var author: String = ""
    var dbId: Long = 0
    private var undo = UndoAction()

    /**
     * Toggles the value and add the action to the undo stack if the action completed correctly
     */
    fun toggleValue(nLine: Int, nColumn: Int, value: Char) {
        val action = cells[nLine][nColumn].toggleValue(value)
        if (action != null) {
            undo.addAction(action)
            setChanged()
            notifyObservers(VALUE)
        }
    }

    fun undo(): Boolean {
        var res = false
        if (undo.canUndo()) {
            res = undo.undo(this)
            setChanged()
            notifyObservers(VALUE)
        }
        return res
    }

    fun redo(): Boolean {
        var res = false
        if (undo.canRedo()) {
            res = undo.redo(this)
            setChanged()
            notifyObservers(VALUE)
        }
        return res
    }

    /**
     * Toggles the value and add the action to the undo stack if the action completed correctly
     */
    fun toggleValue(value: Char) {
        if (positionSelected != null) {
            toggleValue(positionSelected!!.nLine, positionSelected!!.nColumn, value)
        }
    }

    fun resetCell(): ResetAction? {
        var res: ResetAction? = null
        if (positionSelected != null) {
            val cell = cells[positionSelected!!.nLine][positionSelected!!.nColumn]
            val formerValues = cell.values.toList()
            if (formerValues.isNotEmpty()) {
                cell.values.clear()
                res = ResetAction(formerValues, Position(cell.position))
                undo.addAction(res)
                setChanged()
                notifyObservers(VALUE)
            }
        }
        return res

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
    fun replace(oldValue: Char, newValue: Char): ReplaceAction? {
        var res: ReplaceAction? = null
        if (oldValue == newValue) {
            return null
        }
        val positions = mutableListOf<PositionReplace>()

        cells.flatten().forEach { c ->
            val posReplace = c.replace(oldValue, newValue)
            if (posReplace != null) {
                positions.add(posReplace)
            }
        }
        if (positions.isNotEmpty()) {
            setChanged()
            notifyObservers(VALUE)
            res = ReplaceAction(oldValue, newValue, positions)
            undo.addAction(res)
        }
        return res
    }

    /**
     * Remove all occurrences of one char in the grid
     */
    fun remove(oldValue: Char): RemoveMultipleAction? {
        var res: RemoveMultipleAction? = null
        val positions = mutableListOf<Position>()

        cells.flatten().filter { !it.enonce }.forEach { c ->
            if (c.values.contains(oldValue)) {
                c.values.remove(oldValue)
                positions.add(Position(c.position))
            }
        }
        if (positions.isNotEmpty()) {
            setChanged()
            notifyObservers(VALUE)
            res = RemoveMultipleAction(oldValue, positions)
            undo.addAction(res)
        }
        return res
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
        undo.clear()
        setChanged()
        notifyObservers(VALUE)
    }

    /**
     * Find all values except the ones in the enonce
     */
    fun findAllValues(): List<Char> {
        val list = mutableListOf<Char>()
        cells.flatten().filter { c -> !c.enonce }.flatMapTo(list, { c -> c.values })
        return list.distinct()
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt()) {
        positionSelected = parcel.readParcelable(Position::class.java.classLoader)
        undo = parcel.readParcelable(UndoAction::class.java.classLoader)
        for (i in 0 until nbLines) {
            cells[i] = parcel.readParcelableArray(Cell::class.java.classLoader) as Array<Cell>
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(nbLines)
        parcel.writeInt(nbColumns)
        parcel.writeParcelable(positionSelected, flags)
        parcel.writeParcelable(undo, flags)
        for (i in 0 until nbLines) {
            parcel.writeParcelableArray(cells[i], flags)
        }
    }

    override fun describeContents(): Int {
        return 0
    }


}