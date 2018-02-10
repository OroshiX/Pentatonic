package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.ACTION_ADD
import com.nimoroshix.pentatonic.util.parcelableCreator
import com.nimoroshix.pentatonic.util.readChar
import com.nimoroshix.pentatonic.util.writeChar

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class AddAction(char: Char, position: Position) : SingleAction(char, position) {

    constructor() : this(' ', Position(0, 0))

    constructor(parcel: Parcel) :
            this(parcel.readChar(),
                    parcel.readParcelable<Position>(Position::class.java.classLoader))

    override fun applyUndo(grid: Grid): Set<Cell> {
        grid.cells[position.nLine][position.nColumn].values.remove(char)
        return setOf(grid.cells[position.nLine][position.nColumn])
    }

    override fun applyRedo(grid: Grid): Set<Cell> {
        val cell = grid.cells[position.nLine][position.nColumn]
        if (cell.values.contains(char)) return emptySet()
        cell.values.add(char)
        return setOf(cell)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeChar(char)
        parcel.writeParcelable(position, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::AddAction)
    }

    override fun toStringSerialization(): String {
        return "$ACTION_ADD $char ${position.toStringSerialization()}"
    }

    override fun fromStringSerialization(serialization: String) {
        val bits = serialization.split(' ')
        assert(bits.size == 3)
        assert("ADD" == bits[0])
        assert(bits[1].length == 1)
        char = bits[1][0]
        position.fromStringSerialization(bits[2])

    }

    override fun toString(): String {
        return toStringSerialization()
    }

}