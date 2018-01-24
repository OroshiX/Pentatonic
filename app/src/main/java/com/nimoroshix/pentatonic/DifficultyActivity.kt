package com.nimoroshix.pentatonic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nimoroshix.pentatonic.util.Constants.Companion.BUNDLE_DIFFICULTY

class DifficultyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)
    }

    private fun startChooseDifficulty(difficulty: Int) {
        val intent = Intent(this, ChoosePentatonicActivity::class.java)
        intent.putExtra(BUNDLE_DIFFICULTY, difficulty)
        startActivity(intent)
    }

    fun onBeginner(view: View) {
        startChooseDifficulty(1)
    }

    fun onEasy(view: View) {
        startChooseDifficulty(2)
    }

    fun onMedium(view: View) {
        startChooseDifficulty(3)
    }

    fun onHard(view: View) {
        startChooseDifficulty(4)
    }

    fun onVeryHard(view: View) {
        startChooseDifficulty(5)
    }
}
