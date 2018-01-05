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
        grid = Serializer.serialize("7 6\n" +
                "122223\n" +
                "445533\n" +
                "645537\n" +
                "644587\n" +
                "688887\n" +
                "6999aa\n" +
                "bb99aa\n" +
                "×,0,1\n" +
                "×,6,5")
        pentatonicEnonce.grid = grid
        pentatonicValues.grid = grid
        grid.addObserver(pentatonicEnonce)
        grid.addObserver(pentatonicValues)
        grid.select(2, 1)

    }
}
