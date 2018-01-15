package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class PositionReplace(nLine: Int, nColumn: Int, var deduplicated: Boolean = false) : Position(nLine, nColumn) {
}