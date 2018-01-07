package com.nimoroshix.pentatonic

import kotlinx.android.synthetic.main.activity_main.*

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer

class MainActivity : AppCompatActivity() {

    private lateinit var grid: Grid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        grid = Serializer.serialize("6 7\n" +
                "1223344\n" +
                "5523364\n" +
                "7522666\n" +
                "7558999\n" +
                "788899a\n" +
                "78bbbba\n" +
                "-3,0,2,1\n" +
                "-1,4,0,5")
        pentatonicEnonce.grid = grid
        pentatonicValues.grid = grid
        grid.addObserver(pentatonicEnonce)
        grid.addObserver(pentatonicValues)

    }
}
