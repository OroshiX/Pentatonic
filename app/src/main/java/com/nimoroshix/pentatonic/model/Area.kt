package com.nimoroshix.pentatonic.model

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Area(var id: Char, var size: Int) {
    constructor(id: Char) : this(id, 0)
    constructor() : this('0', 0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (id != other.id) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + size
        return result
    }
}