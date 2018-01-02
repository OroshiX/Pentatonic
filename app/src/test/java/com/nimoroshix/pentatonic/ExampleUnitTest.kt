package com.nimoroshix.pentatonic

import com.nimoroshix.pentatonic.model.Area
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.serializer.Serializer
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val textGrid = "4 5\n" +
            "11233\n" +
            "11223\n" +
            "45266\n" +
            "55556\n" +
            "1,2,2\n" +
            "3,3,2\n" +
            "a,0,2\n" +
            "a,0,0\n" +
            "-2,0,3,1"

    @Test
    fun serializationIsCorrect() {
        val grid = Serializer.serialize(textGrid)
        print(grid)

        var cell = Cell(0, 0)
        cell.area = Area('1', 4)
        cell.sister = 'a'
        assertEquals(cell, grid.cells[0][0])

        cell = Cell(0, 2)
        cell.area = Area('2', 4)
        cell.sister = 'a'
        assertEquals(cell, grid.cells[0][2])

        cell = Cell(2, 2)
        cell.area = Area('2', 4)
        cell.values.add('1')
        cell.enonce = true
        assertEquals(cell, grid.cells[2][2])

        cell = Cell(3, 2)
        cell.area = Area('5', 5)
        cell.values.add('3')
        cell.enonce = true
        assertEquals(cell, grid.cells[3][2])

        cell = Cell(2, 0)
        cell.area = Area('4', 1)
        cell.differenceOne = Cell(3, 1)
        cell.differenceOne!!.area = Area('5', 5)
        cell.differenceOne!!.differenceOne = cell
        assertEquals(cell, grid.cells[2][0])
    }

    @Test
    fun replaceIsCorrect() {
        val grid = Serializer.serialize(textGrid)
        grid.toggleValue(0, 1, 'q')
        grid.toggleValue(3, 4, 'q')

        // We replace all 'q's into 3
        grid.replace('q', '3')

        var cell = Cell(0, 1)
        cell.area = Area('1', 4)
        cell.values.add('3')
        cell.dirty = true

        assertEquals(cell, grid.cells[0][1])

        cell = Cell(3, 4)
        cell.area = Area('6', 3)
        cell.values.add('3')
        cell.dirty = true

        assertEquals(cell, grid.cells[3][4])
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
