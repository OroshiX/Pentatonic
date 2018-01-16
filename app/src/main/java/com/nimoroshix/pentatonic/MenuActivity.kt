package com.nimoroshix.pentatonic

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import android.support.v7.app.AppCompatDelegate
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.widget.Toast
import android.view.Gravity
import android.view.View
import java.util.*


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        simulateDayNight(0)

        val aboutPage = AboutPage(this).isRTL(false).setImage(R.drawable.ic_launcher_foreground)
                .setDescription(resources.getString(R.string.description))
                .addItem(Element().setTitle("Version 0.1"))
                .addGroup("Connect with us")
                .addEmail("pentatonic+android@gmail.com")
                .addFacebook("pentatonic")
                .addTwitter("pentatonic")
                .addPlayStore("com.nimoroshix.pentatonic")
                .addGitHub("OroshiX")
                .addItem(getCopyRightsElement())
                .create()
        setContentView(aboutPage)
//        setContentView(R.layout.activity_menu)
    }

    private fun getCopyRightsElement(): Element {
        val copyRightsElement = Element()
        val copyrights = String.format(getString(R.string.copy_right),
                Calendar.getInstance().get(Calendar.YEAR))
        copyRightsElement.title = copyrights
        copyRightsElement.iconDrawable = R.drawable.ic_launcher_foreground
        copyRightsElement.iconTint = android.R.color.black
        copyRightsElement.iconNightTint = android.R.color.white
        copyRightsElement.gravity = Gravity.CENTER
        copyRightsElement.onClickListener = View.OnClickListener {
            Toast.makeText(this@MenuActivity, copyrights, Toast.LENGTH_SHORT).show()
        }
        return copyRightsElement
    }

    private fun simulateDayNight(currentSetting: Int) {
        val DAY = 0
        val NIGHT = 1
        val FOLLOW_SYSTEM = 3

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO)
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES)
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}