package com.nimoroshix.pentatonic.util

import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 07/01/2018.
 */
class TouchUtils {
    companion object {
        /**
         * Given the parameters of the view, takes the position of a touch input, and converts it to a position in the grid
         * @param x x position in the view
         * @param y y position in the view
         * @param offsetLeft offset configured in the view
         * @param offsetTop offset configured in the view
         * @param cellSize the cell size in the view
         * @param nbLines the number of lines of the grid
         * @param nbColumns the number of columns of the grid
         * @return null if the position is outside the grid, else the position in the grid (i,j)
         */
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