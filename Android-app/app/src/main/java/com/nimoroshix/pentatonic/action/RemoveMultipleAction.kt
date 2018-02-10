package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.ACTION_REMOVE_MULTIPLE
import com.nimoroshix.pentatonic.util.parcelableCreator
import com.nimoroshix.pentatonic.util.readChar
import com.nimoroshix.pentatonic.util.writeChar

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class RemoveMultipleAction(var char: Char, positions: List<Position>) : MultipleAction(positions) {

    constructor() : this(' ', mutableListOf())

    override fun applyUndo(grid: Grid): Set<Cell> {
        val res = mutableSetOf<Cell>()
        positions.forEach { pos ->
            val cell = grid.cells[pos.nLine][pos.nColumn]
            if (!cell.values.contains(char)) {
                cell.values.add(char)
                res.add(cell)
            }
        }
        return res
    }

    override fun applyRedo(grid: Grid): Set<Cell> {
        val res = mutableSetOf<Cell>()
        positions.forEach { pos ->
            val cell = grid.cells[pos.nLine][pos.nColumn]
            if (cell.values.contains(char)) {
                cell.values.remove(char)
                res.add(cell)
            }
        }
        return res
    }

    constructor(parcel: Parcel) : this(parcel.readChar(),
            listOf<Position>().apply { parcel.readTypedList(this, Position.CREATOR) })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeChar(char)
        parcel.writeTypedList(this.positions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::RemoveMultipleAction)
    }

    override fun toStringSerialization(): String {
        return "$ACTION_REMOVE_MULTIPLE $char ${positions.joinToString(
                ";") { it.toStringSerialization() }}"
    }

    override fun fromStringSerialization(serialization: String) {
        val bits = serialization.split(' ')
        assert(bits.size == 3)
        assert(bits[1].length == 1)
        char = bits[1][0]
        val positionsDeserialized = mutableListOf<Position>()
        val positionStrings = bits[2].split(";")
        positionStrings.forEach { p ->
            val pos = Position(0, 0)
            pos.fromStringSerialization(p)
            positionsDeserialized.add(pos)
        }
        positions = positionsDeserialized
    }

    override fun toString(): String {
        return toStringSerialization()
    }

}