package com.base.hamoud.chronictrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.data.entity.Hit

class HitListAdapter internal constructor(var context: Context): RecyclerView.Adapter<HitListAdapter.HitViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var hits = emptyList<Hit>() // cached copy of hits
    var hitsLiveData: MutableLiveData<Boolean> = MutableLiveData()

    inner class HitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val hitTimeTV: TextView = itemView.findViewById(R.id.hit_time_text_tiew)
        val chronicTypeTV: TextView = itemView.findViewById(R.id.chronic_type_text_view)
        val chronicStrainTV: TextView = itemView.findViewById(R.id.chronic_strain_text_view)
        val toolUsedTV: TextView = itemView.findViewById(R.id.tool_used_text_view)
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
        holder.hitTimeTV.text = current.hitTime
        holder.chronicTypeTV.text = current.hitType
        holder.chronicStrainTV.text = current.strain
        holder.toolUsedTV.text = current.toolUsed
    }

    internal fun setHits(hits: List<Hit>) {
        this.hits = hits
        hitsLiveData.postValue(true)
        notifyDataSetChanged()
    }
}