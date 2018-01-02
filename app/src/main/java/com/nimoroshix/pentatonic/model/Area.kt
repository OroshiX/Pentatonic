package com.nimoroshix.pentatonic.model

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Area(var id: Char, var size: Int) {
    constructor(id: Char) : this(id, 0)
    constructor() : this('0', 0)

}