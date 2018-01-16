package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.util.parcelableCreator
import com.nimoroshix.pentatonic.util.readChar
import com.nimoroshix.pentatonic.util.writeChar

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class ReplaceAction(private var oldChar: Char, private var newChar: Char, positions: List<PositionReplace>) : MultipleAction(positions) {
    constructor() : this(' ', ' ', mutableListOf())

    override fun applyUndo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            pos as PositionReplace
            val cell = grid.cells[pos.nLine][pos.nColumn]
            // Deduplicated means that before the replace action, we already had newChar. So the replace action had only removed oldChar
            // and not added any new char
            if (!pos.deduplicated) {
                res = cell.values.remove(newChar) && res
            }
            res = cell.values.add(oldChar) && res
        }
        return res
    }

    override fun applyRedo(grid: Grid): Boolean {
        var res = true
        positions.forEach { pos ->
            pos as PositionReplace
            val cell = grid.cells[pos.nLine][pos.nColumn]
            res = cell.values.remove(oldChar) && res
            if (!pos.deduplicated) {
                res = cell.values.add(newChar) && res
            }
        }
        return res
    }

    constructor(parcel: Parcel) : this(parcel.readChar(), parcel.readChar(), listOf<PositionReplace>().apply {
        parcel.readTypedList(this, PositionReplace.CREATOR)
    })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeChar(oldChar)
        parcel.writeChar(newChar)
        parcel.writeTypedList(positions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ReplaceAction)
    }


    override fun toStringSerialization(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromStringSerialization(serialization: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}