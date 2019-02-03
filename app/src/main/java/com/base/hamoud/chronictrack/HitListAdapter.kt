package com.base.hamoud.chronictrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.data.entity.Hit

class HitListAdapter internal constructor(context: Context): RecyclerView.Adapter<HitListAdapter.HitViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var hits = emptyList<Hit>() // cached copy of hits

    inner class HitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val hitItemView: TextView = itemView.findViewById(R.id.hit_item_title_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder {
        val itemView = inflater.inflate(R.layout.item_hit, parent, false)
        return HitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return hits.size
    }

    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val current = hits[position]
        holder.hitItemView.text = current.hitTime
    }

    internal fun setHits(hits: List<Hit>) {
        this.hits = hits
        notifyDataSetChanged()
    }
}