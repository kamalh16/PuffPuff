package com.base.hamoud.chronictrack.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Hit
import com.google.android.material.snackbar.Snackbar


class HitListAdapter internal constructor(
      var context: Context) : RecyclerView.Adapter<HitListAdapter.HitViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var hits = mutableListOf<Hit>()// cached copy of hits

    private var tempDeletedItem: Hit? = null
    private var tempDeletedItemPosition: Int = -1

    inner class HitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hitTimeTV: TextView = itemView.findViewById(R.id.hit_time_text_tiew)
        val hitDateTV: TextView = itemView.findViewById(R.id.hit_date_text_tiew)
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
        holder.hitDateTV.text = current.hitDate
        holder.chronicTypeTV.text = current.hitType
        holder.chronicStrainTV.text = current.strain
        holder.toolUsedTV.text = current.toolUsed
    }

    internal fun setHits(hits: MutableList<Hit>) {
        this.hits = hits
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        // store item to delete in temp
        val hit = hits[position]
        tempDeletedItem = hit
        tempDeletedItemPosition = position

        // delete item from list
        hits.removeAt(position)
        notifyItemRemoved(position)

//        showUndoSnackbar()
    }


    private fun showUndoSnackbar() {
//        val activity = (context as MainActivity)
//        val view = activity.findViewById<CoordinatorLayout>(R.id.snackbar_view)
//
//        Snackbar
//              .make(view, "Item deleted.", Snackbar.LENGTH_SHORT)
//              .setAction("Undo") { undoDeleteItem() }
//              .show()
    }

    private fun undoDeleteItem() {
        tempDeletedItem?.let {
            hits.add(tempDeletedItemPosition, it)
            notifyItemInserted(tempDeletedItemPosition)
        }
    }
}