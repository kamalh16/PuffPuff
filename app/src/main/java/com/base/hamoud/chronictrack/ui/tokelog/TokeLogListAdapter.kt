package com.base.hamoud.chronictrack.ui.tokelog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Hit
import java.time.format.DateTimeFormatter


class TokeLogListAdapter internal constructor(
      var context: Context) : RecyclerView.Adapter<TokeLogListAdapter.HitViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var hits = mutableListOf<Hit>()// cached copy of hits

    inner class HitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hitTimeTV: TextView = itemView.findViewById(R.id.item_toke_time_field)
        val hitDateTV: TextView = itemView.findViewById(R.id.item_toke_date_field)
        val chronicTypeTV: TextView = itemView.findViewById(R.id.item_toke_type_field)
        val chronicStrainTV: TextView = itemView.findViewById(R.id.item_toke_strain_field)
        val toolUsedTV: TextView = itemView.findViewById(R.id.item_toke_tool_field)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder {
        val itemView = inflater.inflate(R.layout.item_toke, parent, false)
        return HitViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return hits.size
    }

    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val current = hits[position]
        holder.hitTimeTV.text = current.hitTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
        holder.hitDateTV.text = current.hitDate.format(DateTimeFormatter.ISO_DATE)
        holder.chronicTypeTV.text = current.hitType
        holder.chronicStrainTV.text = current.strain
        holder.toolUsedTV.text = current.toolUsed
    }

    internal fun setHits(hits: MutableList<Hit>) {
        this.hits = hits
        notifyDataSetChanged()
    }

}