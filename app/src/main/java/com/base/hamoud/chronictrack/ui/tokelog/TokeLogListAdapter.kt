package com.base.hamoud.chronictrack.ui.tokelog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.ui.edittoke.EditTokeScreen
import java.time.OffsetDateTime


class TokeLogListAdapter internal constructor(val context: Context) :
      RecyclerView.Adapter<TokeLogListAdapter.HitViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tokeList = listOf<Toke>()// cached copy of tokeList

    inner class HitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tokeTimeView: TextView = itemView.findViewById(R.id.item_toke_time_field)
        val tokeDateView: TextView = itemView.findViewById(R.id.item_toke_date_field)
        val chronicTypeView: TextView = itemView.findViewById(R.id.item_toke_type_field)
        val chronicStrainView: TextView = itemView.findViewById(R.id.item_toke_strain_field)
        val toolUsedView: TextView = itemView.findViewById(R.id.item_toke_tool_field)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder {
        val itemView = inflater.inflate(R.layout.item_toke, parent, false)
        return HitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val current = tokeList[position]
        holder.tokeTimeView.text = timeCreator(current.tokeDateTime)
        holder.tokeDateView.text = context.getString(R.string.today)
        holder.chronicTypeView.text = current.tokeType
        holder.chronicStrainView.text = current.strain
        holder.toolUsedView.text = current.toolUsed

        // handle onClick
        holder.itemView.setOnClickListener {
            val args = EditTokeScreen.bundleArgs(tokeId = current.id)
            findNavController(holder.itemView)
                  .navigate(R.id.action_toke_log_screen_to_edit_toke_screen, args)
        }
    }

    override fun getItemCount(): Int {
        return tokeList.size
    }

    internal fun setTokeList(tokes: List<Toke>) {
        this.tokeList = tokes
        notifyDataSetChanged()
    }

    private fun dateCreator(date: OffsetDateTime) = "${date.dayOfMonth} / ${date.monthValue} / ${date.year}"

    private fun timeCreator(date: OffsetDateTime): String {
        val hour: String = date.hour.toString()
        val minuteCalendar = date.minute
        val minutes: String = if (minuteCalendar < 10) {
            "0$minuteCalendar"
        } else {
            minuteCalendar.toString()
        }
        return "$hour:$minutes"
    }
}