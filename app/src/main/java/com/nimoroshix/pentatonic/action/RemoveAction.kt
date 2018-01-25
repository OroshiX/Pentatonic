package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.ACTION_REMOVE
import com.nimoroshix.pentatonic.util.*

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class RemoveAction(char: Char, position: Position) : SingleAction(char, position) {
    override fun applyUndo(grid: Grid): Set<Cell> {
        val cell = grid.cells[position.nLine][position.nColumn]
        if (cell.values.contains(char)) return emptySet()
        cell.values.add(char)
        return setOf(cell)
    }

    override fun applyRedo(grid: Grid): Set<Cell> {
        grid.cells[position.nLine][position.nColumn].values.remove(char)
        return setOf(grid.cells[position.nLine][position.nColumn])
    }

    constructor(parcel: Parcel) :
            this(parcel.readChar(), parcel.readTypedObjectCompat(Position.CREATOR)!!)

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeChar(char)
        writeTypedObjectCompat(position, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::RemoveAction)
    }

    constructor() : this(' ', Position(0, 0))

    override fun toStringSerialization(): String {
        return "$ACTION_REMOVE $char ${position.toStringSerialization()}";
    }

    override fun fromStringSerialization(serialization: String) {
        val bits = serialization.split(' ')
        assert(bits.size == 3)
        assert(bits[1].length == 1)
        char = bits[1][0]
        position.fromStringSerialization(bits[2])
    }

    override fun toString(): String {
        return toStringSerialization()
    }
}