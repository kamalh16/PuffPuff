package com.base.hamoud.puffpuff.ui.journal

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
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.data.entity.Toke
import com.base.hamoud.puffpuff.data.model.Tools
import com.base.hamoud.puffpuff.ui.edittoke.EditTokeScreen
import com.github.marlonlom.utilities.timeago.TimeAgo
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*


class JournalListAdapter internal constructor(val context: Context) :
    RecyclerView.Adapter<JournalListAdapter.HitViewHolder>() {

    private val calendar = Calendar.getInstance()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tokeList = mutableListOf<Toke>()// cached copy of tokeList

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
        calendar.timeInMillis = tokeItem.tokeDateTime
        val tokeDayAt = getTokeDayAt(DateTime(tokeItem.tokeDateTime))
        val tokeTimeAt = getTokeTimeAt(calendar)
        val tokeTimeAgo = getTokeTimeAgo(tokeItem.tokeDateTime)

        holder.tokeTimeView.text = String.format(
            "%s, %s - %s",
            tokeDayAt, tokeTimeAt, tokeTimeAgo
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
     * Set new tokes and calls [notifyDataSetChanged] to refresh the [JournalListAdapter]
     *
     * @param tokes [List] of [Toke]s
     */
    internal fun setTokeList(tokes: MutableList<Toke>) {
        this.tokeList = tokes
        notifyDataSetChanged()
    }

    /**
     * Clear [tokeList] and call [notifyDataSetChanged]
     */
    internal fun clearTokeList() {
        tokeList.clear()
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

    /**
     * Format [date] to a readable time decimalFormat based on the
     * device's decimalFormat (whether 24-hour decimalFormat is set in the device's system settings).
     *
     * @param date [Calendar] to decimalFormat
     *
     * @return formatted [Calendar] as [String]
     */
    private fun getTokeTimeAt(date: Calendar): String {
        val isDevice24HourClock = DateFormat.is24HourFormat(context.applicationContext)
        val pattern = if (isDevice24HourClock) {
            "H:mm a"
        } else {
            "h:mm a"
        }
        // apply pattern and return
        val spf = SimpleDateFormat(pattern, Locale.getDefault())
        return spf.format(date.time)
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
    private fun getTokeTimeAgo(timeInMillis: Long): String {
        return TimeAgo.using(timeInMillis)
            .replace("minutes", "mins")
            .replace("hours", "hrs")
    }

    /**
     * Get the day of the month for the passed in [dateTime].
     *
     * @param dateTime the toked at time
     *
     * @return [String] If its the same as today then return [R.string.label_today]
     * otherwise return the name of the day of the month.
     */
    private fun getTokeDayAt(dateTime: DateTime): String {
        return if (dateTime.dayOfMonth == DateTime.now().dayOfMonth) {
            context.resources.getString(R.string.label_today)
        } else {
            dateTime.dayOfMonth().asText
        }
    }

}