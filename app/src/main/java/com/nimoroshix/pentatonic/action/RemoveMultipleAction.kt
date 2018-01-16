package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.util.parcelableCreator
import com.nimoroshix.pentatonic.util.readChar

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class RemoveMultipleAction(var char: Char, positions: List<Position>) : MultipleAction(positions) {

    constructor() : this(' ', mutableListOf())

    override fun applyUndo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            val cell = grid.cells[pos.nLine][pos.nColumn]
            if (cell.values.contains(char)) {
                res = false
            } else {
                res = cell.values.add(char) && res
            }
        }
        return res
    }

    override fun applyRedo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            val cell = grid.cells[pos.nLine][pos.nColumn]
            if (cell.values.contains(char)) {
                res = cell.values.remove(char) && res
            } else {
                res = false
            }
        }
        return res
    }

    constructor(parcel: Parcel) : this(parcel.readChar(), listOf<Position>().apply { parcel.readTypedList(this, Position.CREATOR) })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(char.toInt())

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::RemoveMultipleAction)
    }

    override fun toStringSerialization(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromStringSerialization(serialization: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}