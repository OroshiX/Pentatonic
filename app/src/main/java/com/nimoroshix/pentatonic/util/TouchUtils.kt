package com.nimoroshix.pentatonic.util

import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 07/01/2018.
 */
class TouchUtils {
    companion object {
        fun touchToPosition(x: Float, y: Float, offsetLeft: Float, offsetTop: Float, cellSize: Float, nbLines: Int, nbColumns: Int): Position? {
            if (x < offsetLeft || y < offsetTop || x > nbColumns * cellSize + offsetLeft || y > nbLines * cellSize + offsetTop) {
                return null
            }
            val nCol: Int = ((x - offsetLeft) / cellSize).toInt()
            val nLine: Int = ((y - offsetTop) / cellSize).toInt()
            return Position(nLine, nCol)
        }
    }
}