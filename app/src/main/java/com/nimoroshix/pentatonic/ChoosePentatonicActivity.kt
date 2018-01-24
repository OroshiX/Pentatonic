package com.nimoroshix.pentatonic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.nimoroshix.pentatonic.adapter.ChoosePentatonicAdapter
import com.nimoroshix.pentatonic.model.ItemPenta
import com.nimoroshix.pentatonic.persistence.AppDatabase
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
        val difficulty: Int = savedInstanceState?.get(BUNDLE_DIFFICULTY) as Int? ?: 1

        recycler_choose.setHasFixedSize(true)

        // linear layout manager
        val layoutManager = LinearLayoutManager(this)
        recycler_choose.layoutManager = layoutManager

        Observable.just(AppDatabase.getInstance(this).pentatonicDao())
                .switchMap({ dao -> Observable.just(dao.findPentatonicByDifficulty(difficulty).map { pentatonic -> ItemPenta(pentatonic.id) }) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<ItemPenta> ->
                    recycler_choose.adapter = ChoosePentatonicAdapter(list, {
                        val intent = Intent(this@ChoosePentatonicActivity, GameActivity::class.java)
                        intent.putExtra(BUNDLE_ID_PENTA, it.id)
                        startActivity(intent)
                    })
                })
    }
}
