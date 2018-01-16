package com.nimoroshix.pentatonic

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Spinner
import com.nimoroshix.pentatonic.adapter.MonoArrayAdapter
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.persistence.AppDatabase
import com.nimoroshix.pentatonic.persistence.Pentatonic
import com.nimoroshix.pentatonic.serializer.Serializer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class GameActivity : AppCompatActivity() {

    companion object {
        val TAG = "GameActivity"
    }

    private lateinit var grid: Grid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        insertDummyData()
        viewDummyPentatonic()
//        getDbData()
        val scaleDetector = ScaleGestureDetector(this, pentatonicValues)
        val gestureDetector = GestureDetector(this, pentatonicValues)
        pentatonicValues.setOnTouchListener { _: View?, motionEvent: MotionEvent? ->
            scaleDetector.onTouchEvent(motionEvent)
            gestureDetector.onTouchEvent(motionEvent)
        }

    }

    private fun viewDummyPentatonic() {
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

    private fun getDbData() {
        Observable.just(AppDatabase.getInstance(this).pentatonicDao()).subscribeOn(
                Schedulers.io()).subscribe { dao ->
            grid = Serializer.fromDbToGrid(dao.getPentatonicById(1))
            pentatonicEnonce.grid = grid
            pentatonicValues.grid = grid
            pentatonicKeys.grid = grid
            grid.addObserver(pentatonicEnonce)
            grid.addObserver(pentatonicValues)
        }
    }

    private fun insertDummyData() {

        val penta = Pentatonic(6, 7)
        penta.difficulty = 3
        penta.areas = "1223344\n" +
                "5523364\n" +
                "7522666\n" +
                "7558999\n" +
                "788899a\n" +
                "78bbbba"
        penta.enonce = "-3,0,2,1\n" +
                "-1,4,0,5"
        penta.progress = null

        // Insert a pentatonic in db
        Observable.just(AppDatabase.getInstance(this).pentatonicDao())
                .subscribeOn(Schedulers.io())
                .subscribe { dao -> dao.insertPentatonic(penta) }

    }

    private fun displayReplaceDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.replace_dialog_layout, null)

        val alert: AlertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.title_replace_dialog).setView(view)
                .setPositiveButton(R.string.replace_positive_button,
                        { dialogInterface: DialogInterface, _: Int ->
                            val dialog: AlertDialog = dialogInterface as AlertDialog
                            doReplace(dialog.findViewById<Spinner>(
                                    R.id.sp_old_val).selectedItem.toString()[0],
                                    dialog.findViewById<Spinner>(
                                            R.id.sp_new_val).selectedItem.toString()[0])
                            dialogInterface.dismiss()
                        })
                .setNegativeButton(R.string.cancel,
                        { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() })
                .create()
        val listOld = mutableListOf<Char>()
        val allValues = grid.findAllValues()
        listOld.addAll(allValues)
        val adapterOld = MonoArrayAdapter(baseContext, android.R.layout.simple_spinner_item,
                listOld)
        adapterOld.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.sp_old_val).adapter = adapterOld

        val listNew = mutableListOf<Char>()
        listNew.addAll('1'..'5')
        listNew.addAll(allValues)
        val adapterNew = MonoArrayAdapter(this, android.R.layout.simple_spinner_item,
                listNew.distinct())
        adapterNew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.sp_new_val).adapter = adapterNew

        alert.show()
    }

    private fun displayResetDialog() {
        AlertDialog.Builder(this).setTitle("Reset the game")
                .setMessage(
                        "Do you really want to reset the game. Any number/letter you put will be erased.")
                .setPositiveButton("Reset", { _, _ -> doReset() })
                .setNegativeButton("Cancel",
                        { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() })
                .show()
    }


    private fun doReplace(oldVal: Char, newVal: Char) {
        grid.replace(oldVal, newVal)
    }

    private fun doReset() {
        grid.reset()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_undo -> {
                return grid.undo()
            }
            R.id.menu_redo -> {
                return grid.redo()
            }
            R.id.menu_replace -> {
                displayReplaceDialog()
                return true
            }
            R.id.menu_reset -> {
                displayResetDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
