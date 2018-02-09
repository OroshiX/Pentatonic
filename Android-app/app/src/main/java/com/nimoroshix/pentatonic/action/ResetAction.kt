package com.nimoroshix.pentatonic.action

import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.ACTION_RESET
import com.nimoroshix.pentatonic.util.parcelableCreator

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class ResetAction(var allChars: List<Char>, var position: Position) : Action, Parcelable {
    constructor() : this(mutableListOf(), Position(0, 0))

    override fun applyUndo(grid: Grid): Set<Cell> {
        val res = mutableSetOf<Cell>()
        allChars.forEach { c ->
            grid.cells[position.nLine][position.nColumn].values.add(c)
            res.add(grid.cells[position.nLine][position.nColumn])
        }
        return res
    }

    override fun applyRedo(grid: Grid): Set<Cell> {
        val res = mutableSetOf<Cell>()
        allChars.forEach { c ->
            grid.cells[position.nLine][position.nColumn].values.remove(c)
            res.add(grid.cells[position.nLine][position.nColumn])
        }
        return res
    }

    constructor(parcel: Parcel) :
            this(listOf<Char>().apply { parcel.readList(this, Char::class.java.classLoader) },
                    parcel.readParcelable(Position::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(position, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ResetAction)
    }

    override fun toStringSerialization(): String {
        return "$ACTION_RESET ${allChars.joinToString(",")} $position"
    }

    override fun fromStringSerialization(serialization: String) {
        val bits = serialization.split(' ')
        assert(bits.size == 3)
        val charsDeserialized = mutableListOf<Char>()
        val chars = bits[1].split(',')
        chars.forEach { c ->
            charsDeserialized.add(c[0])
        }
        allChars = charsDeserialized

        position.fromStringSerialization(bits[2])
    }

    override fun toString(): String {
        return toStringSerialization()
    }
}