package com.nimoroshix.pentatonic.model

import android.graphics.Matrix
import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.action.*
import com.nimoroshix.pentatonic.util.getNumericValue
import com.nimoroshix.pentatonic.util.getOnlyValueList
import com.nimoroshix.pentatonic.util.getOnlyValueSet
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
        val ZOOM = "zoom"
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
            checkValidityOneCell(nLine, nColumn)

            // update undo
            undo.addAction(action)

            // Notify observers
            setChanged()
            notifyObservers(VALUE)
        }
    }

    fun checkEveryCell() {
        dirtifyEverything()
        checkDirtyValidity()
    }

    private fun checkValidityOneCell(cell: Cell) {
        checkValidityOneCell(cell.position.nLine, cell.position.nColumn)
    }

    private fun checkValidityOneCell(nLine: Int, nColumn: Int) {
        dirtifyAppropriateCells(nLine, nColumn)
        checkDirtyValidity()
    }

    private fun checkValidityMultipleCells(cells: Set<Cell>) {
        cells.forEach { c -> dirtifyAppropriateCells(c.position.nLine, c.position.nColumn) }
        checkDirtyValidity()
    }

    private fun checkValidityMultipleCells(positions: Iterable<Position>) {
        positions.forEach { p -> dirtifyAppropriateCells(p.nLine, p.nColumn) }
        checkDirtyValidity()
    }

    /**
     * Update the dirty propriety of nearby cells and same area cells and sister
     *
     * And also oneself
     */
    private fun dirtifyAppropriateCells(nLine: Int, nColumn: Int) =
            getAllConnectedCells(nLine, nColumn).union(listOf(cells[nLine][nColumn]))
                    .union(getSisterCells(nLine, nColumn))
                    .union(getDiffOneCells(nLine, nColumn))
//                    .filter { it.values.size == 1 } // Only cells with one value
                    .forEach { it.dirty = true }


    private fun dirtifyEverything() = cells.flatten().forEach { it.dirty = true }
    /**
     * Adjust the cells whether they are valid or not, when they are dirty
     *
     * => See if the dirty cells are valid or not
     */
    private fun checkDirtyValidity() {
        cells.flatten().filter { it.dirty }.forEach { c ->
            if (c.values.size > 1 || c.values.isEmpty()) { // if more than 1 value or 0 values, it is valid
                c.valid = true
                c.dirty = false
                return@forEach
            }
            // exactly 1 value
            val value = c.values[0]
            if (!value.isDigit()) c.valid = true
            else {
                val n = value.getNumericValue()
                val connectedValues = getAllConnectedCells(c).getOnlyValueList()

                val sisterValues = getSisterCells(c).union(setOf(c)).getOnlyValueSet()

                val diffOneValues = getDiffOneCells(c).getOnlyValueList()

                // valid if:
                // * no neighbour or area has the same value
                // * sisters all have the same value
                // * either cell is not a number or all diffOne have a difference of one with it
                c.valid = n <= c.area.size
                        && !connectedValues.contains(n)
                        && sisterValues.size <= 1
                        && diffOneValues.all { abs(it - n) == 1 }
            }
            c.dirty = false
        }
    }

    fun undo(): Boolean {
        var res = emptySet<Cell>()
        if (undo.canUndo()) {
            res = undo.undo(this)
            // check validity here for all in res
            checkValidityMultipleCells(res)
            setChanged()
            notifyObservers(VALUE)
        }
        return res.isEmpty()
    }

    fun redo(): Boolean {
        var res = emptySet<Cell>()
        if (undo.canRedo()) {
            res = undo.redo(this)
            // check validity here for all in res
            checkValidityMultipleCells(res)
            setChanged()
            notifyObservers(VALUE)
        }
        return res.isEmpty()
    }

    /**
     * Toggles the value and add the action to the undo stack if the action completed correctly
     */
    fun toggleValue(value: Char) {
        if (positionSelected != null) {
            toggleValue(positionSelected!!.nLine, positionSelected!!.nColumn, value)
        }
    }

    /**
     * Clears the values of the cell, and adds the action to the undo stack if the action completed correctly
     */
    fun resetCell(): ResetAction? {
        var res: ResetAction? = null
        if (positionSelected != null) {
            val cell = cells[positionSelected!!.nLine][positionSelected!!.nColumn]
            val formerValues = cell.values.toList()
            if (formerValues.isNotEmpty()) {
                cell.values.clear()
                res = ResetAction(formerValues, Position(cell.position))
                undo.addAction(res)
                checkValidityOneCell(cell)
                setChanged()
                notifyObservers(VALUE)
            }
        }
        return res

    }

    /**
     * Unselects any cell
     */
    fun unselect() {
        positionSelected = null
        cells.flatten().forEach { c -> c.selection = CellState.UNSELECTED }
    }

    /**
     * Selects the cell situated at
     * @param nLine line number nLine
     * @param nColumn column number nColumn
     */
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
                abs(cell.position.nLine - nLine) <= 1 && abs(
                        cell.position.nColumn - nColumn) <= 1                    -> true // we want the cells nearby
                else                                                             -> false // and we don't want any other cell
            }
        }.toSet()
    }

    fun getSisterCells(cell: Cell): Set<Cell> {
        if (cell.sister == null) return emptySet()
        return cells.flatten().filter { c ->
            when {
                c.position.nLine == cell.position.nLine && c.position.nColumn == cell.position.nColumn -> false // we don't want the same cell
                c.sister == cell.sister                                                                -> true // We want the sister
                else                                                                                   -> false // and we don't want any other cell
            }
        }.toSet()
    }

    fun getSisterCells(nLine: Int, nColumn: Int): Set<Cell> {
        return getSisterCells(cells[nLine][nColumn])
    }

    private fun getDiffOneCells(cell: Cell): Set<Cell> {
        if (cell.differenceOne == null) return emptySet()
        return setOf(cells[cell.differenceOne!!.nLine][cell.differenceOne!!.nColumn])
    }

    private fun getDiffOneCells(nLine: Int, nColumn: Int): Set<Cell> {
        return getDiffOneCells(cells[nLine][nColumn])
    }


    fun getAreaCells(nLine: Int, nColumn: Int): Set<Cell> {
        return getAreaCells(cells[nLine][nColumn])
    }

    fun getAllConnectedCells(cell: Cell): Set<Cell> {
        return getAllConnectedCells(cell.position.nLine, cell.position.nColumn)
    }

    /**
     * Get area and adjacent cells of the given cell at the given position
     *
     * @param nLine at position nLine
     * @param nColumn at position nColumn
     *
     * @return all connected cells
     */
    fun getAllConnectedCells(nLine: Int, nColumn: Int): Set<Cell> {
        return getAreaCells(nLine, nColumn).union(getAdjacentCells(nLine, nColumn))
    }

    fun getAreaCells(cell: Cell): Set<Cell> {
        val idArea = cell.area.id
        return cells.flatten().filter { c ->
            when {
                c.area.id != idArea         -> false // We want the same area
                c.position == cell.position -> false // We don't want the same cell
                else                        -> true
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
            checkValidityMultipleCells(positions)
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
            checkValidityMultipleCells(positions)
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
                cell.dirty = false
                cell.valid = true
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

    fun setScales(scale: Float, dx: Float, dy: Float, scaleFocusX: Float, scaleFocusY: Float) {
        viewMatrix = Matrix(unitMatrix)
        viewMatrix.postScale(scale, scale, scaleFocusX, scaleFocusY)
        invertMatrix = Matrix(viewMatrix)
        invertMatrix.invert(invertMatrix)

        setChanged()
        notifyObservers(ZOOM)
    }

    fun addTranslation(dxFromLastScroll: Float, dyFromLastScroll: Float) {
        viewMatrix.postTranslate(dxFromLastScroll, dyFromLastScroll)
        invertMatrix = Matrix(viewMatrix)
        invertMatrix.invert(invertMatrix)

        setChanged()
        notifyObservers(ZOOM)
    }

    var viewMatrix: Matrix = Matrix()

    var invertMatrix: Matrix = Matrix()
    val unitMatrix = Matrix()


}