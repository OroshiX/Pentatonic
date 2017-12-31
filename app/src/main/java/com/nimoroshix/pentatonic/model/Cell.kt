package com.nimoroshix.pentatonic.model

/**
 * Project Pentatonic
 * Created by Jessica on 31/12/2017.
 */
class Cell() {
    constructor(nLine: Int, nColumn: Int) : this() {
        this.position = Position(nLine, nColumn)
    }

    var area: Area = Area()
    var values: List<Char?> = List(0, { _ -> null })

    var dirty = false
    var valid: Boolean = true
    var sister: Char? = null
    var differenceOne: Cell? = null
    var position: Position? = null
    var enonce: Boolean = false
    var selection: CellState = CellState.UNSELECTED

}
