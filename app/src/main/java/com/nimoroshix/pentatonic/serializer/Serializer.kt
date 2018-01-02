package com.nimoroshix.pentatonic.serializer

import com.nimoroshix.pentatonic.model.Area
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid

/**
 * Project Pentatonic
 * Created by Jessica on 02/01/2018.
 */
class Serializer {
    /**
     * Exemple:
     *
     * 4 5
     * 11233
     * 11223
     * 45266
     * 55556
     * a,2,2
     * b,3,2
     */
    companion object {


        fun serialize(gridText: String): Grid {
            val lines: Sequence<String> = gridText.lineSequence()
            val iterator = lines.iterator()
            // la 1ere ligne consiste en "nbLignes nbCol"
            val (nbLine, nbCol) = iterator.next().split(' ').map(String::toInt)
            val mapAreas = HashMap<Char, Area>()

            val grid = Grid(nbLine, nbCol)
            var line: String
            for (i in 0 until nbLine) {
                line = iterator.next()
                for (j in 0 until nbCol) {
                    val cell = Cell(i, j)
                    var area = mapAreas[line[j]]
                    if (area == null) {
                        area = Area(line[j])
                        mapAreas.put(line[j], area)
                    }
                    cell.area = area
                    grid.cells[i][j] = cell
                }
            }

            grid.fillAreaSize()

            while (iterator.hasNext()) {
                line = iterator.next()
                // We got a number or a constraint
                if (line.startsWith('-')) {
                    // It is a constraint [diffOne]
                    val (i1, j1, i2, j2) = line.substring(1).split(",").map { nb -> nb.toInt() }
                    grid.cells[i1][j1].differenceOne = grid.cells[i2][j2]
                    grid.cells[i2][j2].differenceOne = grid.cells[i1][j1]
                    continue
                }

                val (nb, nLine, nColumn) = line.split(',')
                if (nb.toCharArray().size != 1) {
                    throw IllegalArgumentException("Bad input: $gridText")
                }
                val n = nb.toCharArray()[0]
                val cell = grid.cells[nLine.toInt()][nColumn.toInt()]
                if (n.isDigit()) {
                    // It is a number
                    cell.values = mutableListOf(n)
                    cell.enonce = true
                } else {
                    // It is a constraint [sister]
                    cell.sister = n
                }
            }
            return grid
        }
    }
}