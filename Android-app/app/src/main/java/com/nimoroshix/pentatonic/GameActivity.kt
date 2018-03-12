package com.nimoroshix.pentatonic

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Spinner
import com.nimoroshix.pentatonic.adapter.MonoArrayAdapter
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.persistence.AppDatabase
import com.nimoroshix.pentatonic.serializer.Serializer
import com.nimoroshix.pentatonic.util.Constants.Companion.BUNDLE_ID_PENTA
import com.nimoroshix.pentatonic.util.saveBitmap
import com.nimoroshix.pentatonic.util.shareIt
import com.nimoroshix.pentatonic.util.takeScreenshot
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class GameActivity : AppCompatActivity() {

    companion object {
        const val TAG = "GameActivity"
        const val PARCEL_GRID = "parcelableGrid"

    }

    private lateinit var grid: Grid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.w(TAG, "onCreate")
        setContentView(R.layout.activity_main)

        val idPenta: Long = intent.getLongExtra(BUNDLE_ID_PENTA, 1L)


//        insertDummyData()
//        viewDummyPentatonic()
        getDbData(idPenta)
        val scaleDetector = ScaleGestureDetector(this, pentatonicValues)
        val gestureDetector = GestureDetector(this, pentatonicValues)
        pentatonicValues.setOnTouchListener { _: View?, motionEvent: MotionEvent? ->
            scaleDetector.onTouchEvent(motionEvent)
            gestureDetector.onTouchEvent(motionEvent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.w(TAG, "onSaveInstanceState")
        outState.putParcelable(PARCEL_GRID, grid)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.w(TAG, "onRestoreInstanceState")
        setGridAndObservers(savedInstanceState.getParcelable(PARCEL_GRID))

    }

    override fun onPause() {
        super.onPause()
        Log.w(TAG, "onPause")
        saveProgressToDB()
    }

    private fun saveProgressToDB() {
        Observable.fromCallable {
            AppDatabase.getInstance(this).pentatonicDao()
                    .insertPentatonic(Serializer.fromGridToDb(grid))
            return@fromCallable
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun onResume() {
        super.onResume()
        Log.w(TAG, "onResume")
    }

    private fun setGridAndObservers(grid: Grid) {
        this.grid = grid
        pentatonicEnonce.grid = grid
        pentatonicValues.grid = grid
        pentatonicKeys.grid = grid
        grid.addObserver(pentatonicEnonce)
        grid.addObserver(pentatonicValues)
        pentatonicEnonce.invalidate()
        pentatonicValues.invalidate()
    }

    private fun getDbData(idPenta: Long) {
        Observable.fromCallable({
            val pentatonic = AppDatabase.getInstance(this).pentatonicDao().getPentatonicById(
                    idPenta)
            return@fromCallable Serializer.fromDbToGrid(pentatonic)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    setGridAndObservers(it)
                }
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

    private fun displayRemoveAllOccurrencesDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.remove_dialog_layout, null)
        val alert: AlertDialog = AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Remove a character everywhere")
                .setPositiveButton("Delete", { dialogInterface: DialogInterface, _: Int ->
                    val dialog: AlertDialog = dialogInterface as AlertDialog
                    doDelete(dialog.findViewById<Spinner>(R.id.sp_value).selectedItem.toString()[0])
                    dialog.dismiss()
                })
                .setNegativeButton("Cancel",
                        { dialog: DialogInterface, _: Int -> dialog.dismiss() })
                .create()

        val listAllValues = grid.findAllValues()
        val adapter = MonoArrayAdapter(baseContext, android.R.layout.simple_spinner_item,
                listAllValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.sp_value).adapter = adapter
        alert.show()
    }

    private fun doDelete(c: Char) {
        grid.remove(c)
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
            R.id.menu_undo    -> return grid.undo()
            R.id.menu_redo    -> return grid.redo()
            R.id.menu_replace -> {
                displayReplaceDialog()
                return true
            }
            R.id.menu_reset   -> {
                displayResetDialog()
                return true
            }
            R.id.menu_remove  -> {
                displayRemoveAllOccurrencesDialog()
                return true
            }
            R.id.menu_share   -> {
                val bitmap = takeScreenshot(framePentatonic)
                val savedBitmap = saveBitmap(this, bitmap)
                shareIt(this, savedBitmap,
                        "Have a look at this pentatonic. Do you think you can solve it?",
                        "Just sharing a Pentatonic")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
