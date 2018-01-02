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
     * b,5,3
     */
    fun serialize(gridText: String): Grid {
        val lines: Sequence<String> = gridText.lineSequence()
        val iterator = lines.iterator()
        // la 1ere ligne consiste en "nbLignes nbCol"
        val (nbLine, nbCol) = iterator.next().split(' ').map(String::toInt)
        var grid: Grid = Grid(nbLine, nbCol)
        var line: String
        for (i in 0 until nbLine) {
            line = iterator.next()
            for (j in 0 until nbCol) {
                val cell = Cell(i, j)
                cell.area = Area(line[j]) // TODO apres, il faudra trouver la taille de la zone
                grid.cells[i][j] = cell
            }
        }
        while (iterator.hasNext()) {
            line = iterator.next()
            // On a un chiffre ou bien une contrainte
            val (nb, nLine, nColumn) = line.split(',')
            if (nb.toCharArray().size != 1) {
                throw IllegalArgumentException()
            }
            val n = nb.toCharArray()[0]
            grid.cells[nLine.toInt()][nColumn.toInt()].values = charArrayOf(n).asList()
        }
        // TODO to correct and continue

        return grid
    }
}