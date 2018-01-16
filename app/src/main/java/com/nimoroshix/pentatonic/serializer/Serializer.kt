package com.nimoroshix.pentatonic.serializer

import com.nimoroshix.pentatonic.action.*
import com.nimoroshix.pentatonic.model.Area
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.persistence.Pentatonic

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

        @Throws(IllegalArgumentException::class)
        fun serialize(gridText: String): Grid {
            val lines: Sequence<String> = gridText.lineSequence()
            val iterator = lines.iterator()
            // la 1ere ligne consiste en "nbLignes nbCol"
            val (nbLine, nbCol) = iterator.next().split(' ').map(String::toInt)
            val grid = Grid(nbLine, nbCol)
            fillAreas(grid, iterator)
            fillEnonce(grid, iterator)
            return grid
        }

        @Throws(IllegalArgumentException::class)
        fun fromDbToGrid(pentatonic: Pentatonic): Grid {
            val grid = Grid(pentatonic.lines, pentatonic.columns)
            val iteratorAreas = pentatonic.areas.lineSequence().iterator()
            val iteratorEnonce = pentatonic.enonce.lineSequence().iterator()
            fillAreas(grid, iteratorAreas)
            fillEnonce(grid, iteratorEnonce)
            val iteratorProgress = pentatonic.progress?.lineSequence()?.iterator() ?: return grid
            fillProgress(grid, iteratorProgress)
            return grid
        }

        fun fromGridToDb(grid: Grid): Pentatonic {
            val penta = Pentatonic(grid.nbLines, grid.nbColumns)
            penta.areas = grid.cells.joinToString("\n") { row ->
                row.joinToString("") { c -> c.area.id.toString() }
            }
            penta.enonce = grid.cells.flatten().filter { c -> c.enonce || c.sister != null || c.differenceOne != null }.joinToString("\n") { cell ->
                when {
                    cell.enonce -> "${cell.values[0]},${cell.position.nLine},${cell.position.nColumn}"
                    else -> {
                        val sb = StringBuilder()

                        if (cell.sister != null) {
                            sb.append("${cell.sister},${cell.position.nLine},${cell.position.nColumn}")
                        }
                        if (sb.isNotEmpty()) {
                            sb.append("\n")
                        }
                        if (cell.differenceOne != null) {
                            sb.append("-${cell.position.nLine},${cell.position.nColumn},${cell.differenceOne!!.nLine},${cell.differenceOne!!.nColumn}")
                            // TODO check with a Set that the diffOne was not already registered here before
                        }

                        sb.toString()
                    }
                }
            }
            penta.progress = grid.cells.flatten().filter { c -> !c.enonce && c.values.isNotEmpty() }.joinToString("\n") { cell ->
                "${cell.position.nLine},${cell.position.nColumn}:${cell.values.joinToString(",") { c -> c.toString() }}"
            }
            penta.hasDiffOne = grid.cells.flatten().any { c -> c.differenceOne != null }
            penta.hasSister = grid.cells.flatten().any { c -> c.sister != null }
            penta.version = grid.version
            penta.difficulty = grid.difficulty
            return penta
        }

        private fun fillAreas(grid: Grid, iterator: Iterator<String>) {
            val mapAreas = HashMap<Char, Area>()
            var line: String
            for (i in 0 until grid.nbLines) {
                line = iterator.next()
                for (j in 0 until grid.nbColumns) {
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
        }

        @Throws(IllegalArgumentException::class)
        private fun fillEnonce(grid: Grid, iterator: Iterator<String>) {
            var line: String
            while (iterator.hasNext()) {
                line = iterator.next()
                // We got a number or a constraint
                if (line.startsWith('-')) {
                    // It is a constraint [diffOne]
                    val (i1, j1, i2, j2) = line.substring(1).split(",").map { nb -> nb.toInt() }
                    grid.cells[i1][j1].differenceOne = grid.cells[i2][j2].position
                    grid.cells[i2][j2].differenceOne = grid.cells[i1][j1].position
                    continue
                }

                val (nb, nLine, nColumn) = line.split(',')
                if (nb.toCharArray().size != 1) {
                    throw IllegalArgumentException("Bad input: $nb in line $line")
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
        }

        @Throws(IllegalArgumentException::class)
        private fun fillProgress(grid: Grid, iterator: Iterator<String>) {
            var line: String
            while (iterator.hasNext()) {
                // Progress format:
                // i,j:a,b,c,d
                line = iterator.next()
                val progress = line.split(":")
                if (progress.size != 2) {
                    throw IllegalArgumentException("Bad progress: $progress in line $line")
                }
                val (i, j) = progress[0].split(",").map { v -> v.toInt() }
                val values: MutableList<Char> = progress[1].split(",").map { v ->
                    if (v.length != 1) throw IllegalArgumentException(
                            "Bad value in progress: $v in $line")
                    v[0]
                }.toMutableList()
                if (grid.cells[i][j].enonce) throw IllegalArgumentException(
                        "Cell($i,$j) should not have progress, because it is an enonce cell")
                grid.cells[i][j].values = values
            }
        }

        val ACTION_ADD = "ADD"
        val ACTION_REMOVE = "RM"
        val ACTION_REPLACE = "REPLACE"
        val ACTION_REMOVE_MULTIPLE = "RM_ALL"
        val ACTION_RESET = "RESET"
        fun deserializeActions(actionString: List<String>): MutableList<Action> {
            val res = mutableListOf<Action>()
            actionString.forEach { a ->
                val action: Action = when {
                    a.startsWith(ACTION_ADD) -> AddAction()
                    a.startsWith(ACTION_REMOVE) -> RemoveAction()
                    a.startsWith(ACTION_REPLACE) -> ReplaceAction()
                    a.startsWith(ACTION_REMOVE_MULTIPLE) -> RemoveMultipleAction()
                    a.startsWith(ACTION_RESET) -> ResetAction()
                    else -> {
                        throw IllegalArgumentException("action $a does not exist")
                    }
                }
                action.fromStringSerialization(a)
                res.add(action)
            }
            return res
        }

        fun serializeActions(actions: List<Action>): List<String> {
            val res = mutableListOf<String>()
            actions.forEach { a ->
                res.add(a.toStringSerialization())
            }
            return res
        }
    }
}