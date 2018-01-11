package com.nimoroshix.pentatonic.model

import com.nimoroshix.pentatonic.util.Constants.Companion.MAX_SIZE

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Cell(nLine: Int, nColumn: Int) {
    var position: Position = Position(nLine, nColumn)
    var area: Area = Area()
    var values: MutableList<Char> = mutableListOf<Char>()
    var dirty = false
    var valid: Boolean = true
    var sister: Char? = null
    var differenceOne: Cell? = null
        set(value) {
            field = when (value) {
                null -> value
                else -> {
                    if (value.position == position) {
                        throw IllegalArgumentException("DifferenceOne and this position should not be the same")
                    }
                    if (value.position.isNear(position)) {
                        value
                    } else {
                        throw IllegalArgumentException("DifferenceOne and this position must be near each other")
                    }
                }
            }
        }
    var enonce: Boolean = false
    var selection: CellState = CellState.UNSELECTED

    fun fullToString(): String {
        return "Cell(area=$area, values=$values, dirty=$dirty, valid=$valid, sister=$sister, differenceOne=$differenceOne, position=$position, enonce=$enonce, selection=$selection)"
    }

    override fun toString(): String {
        return "$values (${area.id})"
    }

    fun toggleValue(c: Char): Boolean {
        val res: Boolean
        when {
            enonce -> res = false
            values.contains(c) -> {
                values.remove(c)
                dirty = true
                res = true
            }
            else -> res = if (values.size < MAX_SIZE) {
                values.add(c)
                values.sort()
                dirty = true
                true
            } else false
        }
        return res
    }

    fun replace(oldValue: Char, newValue: Char): Boolean {
        var changed = false
        if (!enonce && values.contains(oldValue)) {
            val it = values.listIterator()
            while (it.hasNext()) {
                val c = it.next()
                if (c == oldValue) {
                    if (values.contains(newValue)) {
                        // We don't want to duplicate values, so just remove the oldValue
                        it.remove()
                    } else {
                        it.set(newValue)
                    }
                    changed = true
                }
            }
        }
        return changed
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
        if (differenceOne != null && other.differenceOne != null &&
                differenceOne!!.position != other.differenceOne!!.position) return false
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
        result = 31 * result + (differenceOne?.position?.hashCode() ?: 0)
        result = 31 * result + position.hashCode()
        result = 31 * result + enonce.hashCode()
        result = 31 * result + selection.hashCode()
        return result
    }


}
