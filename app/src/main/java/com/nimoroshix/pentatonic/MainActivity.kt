package com.nimoroshix.pentatonic

import kotlinx.android.synthetic.main.activity_main.*

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private lateinit var grid: Grid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        grid = Serializer.serialize("9 10\n" +
                "1234555677\n" +
                "2234466687\n" +
                "2233469988\n" +
                "aa3bbcc998\n" +
                "daeebbccc8\n" +
                "daeeebfggg\n" +
                "ddhiifffgg\n" +
                "dhhiijfkkl\n" +
                "mmhjjjjkkl\n" +
                "5,2,1\n" +
                "5,3,3\n" +
                "5,4,9\n" +
                "5,8,4")
        pentatonicEnonce.grid = grid
        pentatonicValues.grid = grid
        pentatonicKeys.grid = grid
        grid.addObserver(pentatonicEnonce)
        grid.addObserver(pentatonicValues)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_undo, R.id.menu_redo, R.id.menu_replace -> {
                Log.d(TAG, "TODO") // TODO("implement menu click")
                Toast.makeText(applicationContext, "TODO Not implemented", Toast.LENGTH_SHORT).show()
                return false
            }
            R.id.menu_reset -> {
                Log.d(TAG, "Reset")
                grid.reset()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
