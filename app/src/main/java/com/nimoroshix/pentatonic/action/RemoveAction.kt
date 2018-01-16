package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.util.*

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class RemoveAction(char: Char, position: Position) : SingleAction(char, position) {
    override fun applyUndo(grid: Grid): Boolean {
        val cell = grid.cells[position.nLine][position.nColumn]
        if (cell.values.contains(char)) return false
        return cell.values.add(char)
    }

    override fun applyRedo(grid: Grid): Boolean {
        return grid.cells[position.nLine][position.nColumn].values.remove(char)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromStringSerialization(serialization: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}