package com.nimoroshix.pentatonic.action

import android.os.Parcel
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.ACTION_REPLACE
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
        return "$ACTION_REPLACE $oldChar $newChar ${positions.joinToString(";") { position: Position -> position.toStringSerialization() }}"
    }

    override fun fromStringSerialization(serialization: String) {
        val bits = serialization.split(" ")
        assert(bits.size == 3)
        assert(bits[1].length == 1)
        assert(bits[2].length == 1)
        oldChar = bits[1][0]
        newChar = bits[2][0]
        val positionsDeserialized = mutableListOf<Position>()
        val positionStrings = bits[3].split(";")
        positionStrings.forEach { p ->
            val pos = PositionReplace(0, 0)
            pos.fromStringSerialization(p)
            positionsDeserialized.add(pos)
        }
        positions = positionsDeserialized
    }

}