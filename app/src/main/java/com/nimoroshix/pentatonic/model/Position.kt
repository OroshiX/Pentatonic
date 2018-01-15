package com.nimoroshix.pentatonic.model

import com.nimoroshix.pentatonic.model.RelativePosition.*
import kotlin.math.abs

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
open class Position(var nLine: Int, var nColumn: Int) { // "open" means inheritable (contrary of final)
    constructor(position: Position) : this(position.nLine, position.nColumn)

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

    fun isNear(position: Position): Boolean {
        if (abs(position.nColumn - nColumn) > 1 || abs(position.nLine - nLine) > 1) {
            return false // far away
        }
        if (position.nColumn == nColumn && position.nLine == nLine) {
            return false // we said near, not equals! so no, not near
        }
        return true
    }

    fun getPositionRelativeToMe(position: Position): RelativePosition {
        if (!position.isNear(this)) {
            return ILLEGAL
        }
        /**
         * bottom == 1 => bottom
         * bottom == 0 => middle
         * bottom == -1 => top
         */
        val bottom = position.nLine - nLine
        /**
         * right == 1 => right
         * right == 0 => middle
         * right == -1 => left
         */
        val right = position.nColumn - nColumn
        return when {
            abs(bottom) > 1 || abs(right) > 1 -> ILLEGAL
            bottom < 0 && right < 0 -> TOP_LEFT
            bottom < 0 && right == 0 -> TOP
            bottom < 0 && right > 0 -> TOP_RIGHT
            bottom == 0 && right < 0 -> LEFT
            bottom == 0 && right > 0 -> RIGHT
            bottom > 0 && right < 0 -> BOTTOM_LEFT
            bottom > 0 && right == 0 -> BOTTOM
            bottom > 0 && right > 0 -> BOTTOM_RIGHT
            else -> ILLEGAL
        }
    }

    override fun toString(): String {
        return "(nLine=$nLine, nColumn=$nColumn)"
    }

}