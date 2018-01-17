package com.nimoroshix.pentatonic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun onPlay(view: View) {

    }

    fun onSettings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun onAbout(view: View) {
        startActivity(Intent(this, AboutActivity::class.java))
    }
}
