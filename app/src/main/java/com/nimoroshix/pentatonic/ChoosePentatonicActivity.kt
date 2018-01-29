package com.nimoroshix.pentatonic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.nimoroshix.pentatonic.adapter.ChoosePentatonicAdapter
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.persistence.AppDatabase
import com.nimoroshix.pentatonic.serializer.Serializer
import com.nimoroshix.pentatonic.util.Constants.Companion.BUNDLE_DIFFICULTY
import com.nimoroshix.pentatonic.util.Constants.Companion.BUNDLE_ID_PENTA
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_choose_pentatonic.*

class ChoosePentatonicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_pentatonic)
        val difficulty: Int = intent.getIntExtra(BUNDLE_DIFFICULTY, 1)

        recycler_choose.setHasFixedSize(true)

        // linear layout manager
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_choose.layoutManager = layoutManager

        Observable.fromCallable {
            return@fromCallable AppDatabase.getInstance(this).pentatonicDao().findPentatonicByDifficulty(difficulty)
                    .map { pentatonic -> Serializer.fromDbToGrid(pentatonic) }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<Grid> ->
                    recycler_choose.adapter = ChoosePentatonicAdapter(list, {
                        val intent = Intent(this@ChoosePentatonicActivity, GameActivity::class.java)
                        intent.putExtra(BUNDLE_ID_PENTA, it.dbId)
                        startActivity(intent)
                    })
                })
    }
}
