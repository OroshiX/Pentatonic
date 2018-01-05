package com.nimoroshix.pentatonic.model

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Cell(nLine: Int, nColumn: Int) {
    var position: Position = Position(nLine, nColumn)
    var area: Area = Area()
    var values: MutableList<Char?> = mutableListOf<Char?>()
    var dirty = false
    var valid: Boolean = true
    var sister: Char? = null
    var differenceOne: Cell? = null
    var enonce: Boolean = false
    var selection: CellState = CellState.UNSELECTED

    fun fullToString(): String {
        return "Cell(area=$area, values=$values, dirty=$dirty, valid=$valid, sister=$sister, differenceOne=$differenceOne, position=$position, enonce=$enonce, selection=$selection)"
    }

    override fun toString(): String {
        return "$values (${area.id})"
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
        result = 31 * result + (differenceOne?.hashCode() ?: 0)
        result = 31 * result + position.hashCode()
        result = 31 * result + enonce.hashCode()
        result = 31 * result + selection.hashCode()
        return result
    }


}
