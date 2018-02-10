package com.nimoroshix.pentatonic.model

import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.action.AddAction
import com.nimoroshix.pentatonic.action.PositionReplace
import com.nimoroshix.pentatonic.action.RemoveAction
import com.nimoroshix.pentatonic.action.SingleAction
import com.nimoroshix.pentatonic.util.*
import com.nimoroshix.pentatonic.util.Constants.Companion.MAX_SIZE

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Cell(nLine: Int, nColumn: Int) : Parcelable {
    var position: Position = Position(nLine, nColumn)
    var area = Area()
    var values: MutableList<Char> = mutableListOf()
    var dirty = false
    var valid: Boolean = true
    var sister: Char? = null
    var differenceOne: Position? = null
        set(value) {
            field = when (value) {
                null -> value
                else -> {
                    if (value == position) {
                        throw IllegalArgumentException("DifferenceOne and this position should not be the same")
                    }
                    if (value.isNear(position)) {
                        value
                    } else {
                        throw IllegalArgumentException("DifferenceOne and this position must be near each other")
                    }
                }
            }
        }
    var enonce: Boolean = false
    var selection: CellState = CellState.UNSELECTED
//
//    fun fullToString(): String {
//        return "Cell(area=$area, values=$values, dirty=$dirty, valid=$valid, sister=$sister, differenceOne=$differenceOne, position=$position, enonce=$enonce, selection=$selection)"
//    }

    override fun toString(): String {
        val dirt = if (dirty) "dirty " else ""
        return "$values $dirt(${area.id})"
    }

    fun toggleValue(c: Char): SingleAction? {
        val res: SingleAction?
        when {
            enonce -> res = null
            values.contains(c) -> {
                values.remove(c)
                dirty = true
                res = RemoveAction(c, Position(position))
            }
            else -> res = if (values.size < MAX_SIZE) {
                values.add(c)
                values.sort()
                dirty = true
                AddAction(c, Position(position))
            } else null
        }
        return res
    }

    fun replace(oldValue: Char, newValue: Char): PositionReplace? {
        var positionReplace: PositionReplace? = null
        if (!enonce && values.contains(oldValue)) {
            val it = values.listIterator()
            while (it.hasNext()) {
                val c = it.next()
                if (c == oldValue) {
                    positionReplace = if (values.contains(newValue)) {
                        // We don't want to duplicate values, so just remove the oldValue
                        it.remove()
                        PositionReplace(position.nLine, position.nColumn, true)
                    } else {
                        it.set(newValue)
                        PositionReplace(position.nLine, position.nColumn)
                    }
                }
            }
        }
        return positionReplace
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cell

        if (area != other.area) return false
        if (values != other.values) return false
        if (dirty != other.dirty) return false
        if (valid != other.valid) return false
        if (sister != other.sister) return false
        if (differenceOne != other.differenceOne) return false
        if (position != other.position) return false
        if (enonce != other.enonce) return false
        if (selection != other.selection) return false

        return true
    }

    override fun hashCode(): Int {
        var result = area.hashCode()
        result = 31 * result + values.hashCode()
        result = 31 * result + dirty.hashCode()
        result = 31 * result + valid.hashCode()
        result = 31 * result + (sister?.hashCode() ?: 0)
        result = 31 * result + (differenceOne?.hashCode() ?: 0)
        result = 31 * result + position.hashCode()
        result = 31 * result + enonce.hashCode()
        result = 31 * result + selection.hashCode()
        return result
    }

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt()) {
        values = parcel.createCharArray().toMutableList()
        dirty = parcel.readBool()
        valid = parcel.readBool()
        sister = parcel.readValue(Char::class.java.classLoader) as? Char
        differenceOne = parcel.readParcelable(Position::class.java.classLoader)
        enonce = parcel.readBool()
        selection = parcel.readEnum<CellState>()!!

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(position.nLine)
        parcel.writeInt(position.nColumn)
        parcel.writeCharArray(values.toCharArray())
        parcel.writeBool(dirty)
        parcel.writeBool(valid)
        parcel.writeValue(sister)
        parcel.writeParcelable(differenceOne, flags)
        parcel.writeBool(enonce)
        parcel.writeEnum(selection)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR = parcelableCreator(::Cell)
    }


}