package com.base.hamoud.chronictrack.ui.tokelog

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.model.Tools
import com.base.hamoud.chronictrack.ui.edittoke.EditTokeScreen
import com.github.marlonlom.utilities.timeago.TimeAgo
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


class TokeLogListAdapter internal constructor(val context: Context) :
    RecyclerView.Adapter<TokeLogListAdapter.HitViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tokeList = listOf<Toke>()// cached copy of tokeList

    inner class HitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemContainer: ConstraintLayout = itemView.findViewById(R.id.item_toke_container)
        val tokeIconView: ImageView = itemView.findViewById(R.id.item_toke_icon)
        val tokeTimeView: TextView = itemView.findViewById(R.id.item_toke_date_time_field)
        val tokeTypeView: TextView = itemView.findViewById(R.id.item_toke_type_field)
        val tokeStrainView: TextView = itemView.findViewById(R.id.item_toke_strain_field)
        val tokeTookView: TextView = itemView.findViewById(R.id.item_toke_tool_field)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewHolder {
        val itemView = inflater.inflate(R.layout.item_toke, parent, false)
        return HitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HitViewHolder, position: Int) {
        val tokeItem = tokeList[position]
        val tokeTime = formatTime(tokeItem.tokeDateTime)
        val tokeTimeAgo = getTimeAgo(tokeItem.tokeDateTime.toInstant().toEpochMilli())

        holder.tokeTimeView.text = String.format(
            "%s %s | %s", context.getString(R.string.today_at), tokeTime, tokeTimeAgo
        )
        setTokeToolIcon(holder.tokeIconView, tokeItem.toolUsed)
        holder.tokeTypeView.text = tokeItem.tokeType
        holder.tokeStrainView.text = tokeItem.strain
        holder.tokeTookView.text = tokeItem.toolUsed

        // handle onClick
        holder.itemContainer.setOnClickListener {
            val args = EditTokeScreen.bundleArgs(tokeId = tokeItem.id)
            findNavController(it).navigate(R.id.action_toke_log_screen_to_edit_toke_screen, args)
        }
    }

    override fun getItemCount(): Int {
        return tokeList.size
    }

    /**
     * Set new tokes and calls [notifyDataSetChanged] to refresh the [TokeLogListAdapter]
     *
     * @param tokes [List] of [Toke]s
     */
    internal fun setTokeList(tokes: List<Toke>) {
        this.tokeList = tokes
        notifyDataSetChanged()
    }

    private fun setTokeToolIcon(itemIcon: ImageView, toolUsed: String) {
        when (toolUsed) {
            Tools.Joint.name ->
                itemIcon.setImageResource(R.drawable.icon_joint)
            Tools.Vape.name ->
                itemIcon.setImageResource(R.drawable.icon_vape)
            Tools.Bong.name ->
                itemIcon.setImageResource(R.drawable.icon_bong)
            Tools.Pipe.name ->
                itemIcon.setImageResource(R.drawable.icon_pipe)
            Tools.Edible.name ->
                itemIcon.setImageResource(R.drawable.icon_edible)
            Tools.Dab.name ->
                itemIcon.setImageResource(R.drawable.icon_dab)
            else ->
                itemIcon.setImageResource(R.drawable.icon_joint)
        }
    }

    private fun dateCreator(date: OffsetDateTime) =
        "${date.dayOfMonth} / ${date.monthValue} / ${date.year}"

    /**
     * Format [date] to a readable time format based on the
     * device's format (whether 24-hour format is set in the device's system settings).
     *
     * @param date [OffsetDateTime] to format
     *
     * @return formatted [OffsetDateTime] as [String]
     */
    private fun formatTime(date: OffsetDateTime): String {
        val isDevice24HourClock = DateFormat.is24HourFormat(context.applicationContext)
        val pattern = if (isDevice24HourClock) {
            "H:mm a"
        } else {
            "h:mm a"
        }
        // apply pattern and return
        return DateTimeFormatter
            .ofPattern(pattern)
            .format(date)
            .replace("AM", "am")
            .replace("PM", "pm")
    }

    /**
     * Get how long ago the [timeInMillis] was.
     *
     * @param timeInMillis
     *
     * @return [String] time of how long ago [timeInMillis] occurred i.e "10 mins ago"
     */
    private fun getTimeAgo(timeInMillis: Long): String {
        return TimeAgo.using(timeInMillis)
            .replace("minutes", "mins")
            .replace("hours", "hrs")
    }
}