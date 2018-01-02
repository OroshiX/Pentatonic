package com.nimoroshix.pentatonic.model

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Position(var nLine: Int, var nColumn: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (nLine != other.nLine) return false
        if (nColumn != other.nColumn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nLine
        result = 31 * result + nColumn
        return result
    }
}