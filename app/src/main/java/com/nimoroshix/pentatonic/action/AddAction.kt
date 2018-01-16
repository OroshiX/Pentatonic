package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.util.parcelableCreator

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class AddAction(char: Char, position: Position) : SingleAction(char, position) {

    constructor() : this(' ', Position(0, 0))

    constructor(parcel: Parcel) :
            this(parcel.readInt().toChar(), parcel.readParcelable<Position>(Position::class.java.classLoader))

    override fun applyUndo(grid: Grid): Boolean {
        return grid.cells[position.nLine][position.nColumn].values.remove(char)
    }

    override fun applyRedo(grid: Grid): Boolean {
        val cell = grid.cells[position.nLine][position.nColumn]
        if (cell.values.contains(char)) return false
        return cell.values.add(char)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(char.toInt())
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
        return "ADD $char ${position.toStringSerialization()}"
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