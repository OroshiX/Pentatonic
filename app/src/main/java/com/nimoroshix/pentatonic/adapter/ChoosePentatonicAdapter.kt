package com.nimoroshix.pentatonic.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nimoroshix.pentatonic.R
import com.nimoroshix.pentatonic.model.ItemPenta
import com.nimoroshix.pentatonic.util.inflate
import kotlinx.android.synthetic.main.item_pentatonic.view.*

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 24/01/2018.
 */
class ChoosePentatonicAdapter(val items: List<ItemPenta>, val listener: (ItemPenta) -> Unit) : RecyclerView.Adapter<ChoosePentatonicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.item_pentatonic))


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ItemPenta, listener: (ItemPenta) -> Unit) = with(itemView) {
            tv_id.text = item.id.toString()
            setOnClickListener { listener(item) }
        }
    }

}