package com.base.hamoud.puffpuff.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.ui.main.MainActivity
import com.base.hamoud.puffpuff.ui.settings.model.Settings
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_ABOUT
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_CLEAR_DATA
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_SET_NEXT_TOKE_REMINDER
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_SWITCH_THEME
import kotlin.collections.ArrayList


class SettingsListAdapter(
    private val viewModel: SettingsViewModel,
    private val settings: Settings
) :
    RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {

    private var optionsList: ArrayList<String> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemLabel: TextView = itemView.findViewById(R.id.item_settings_row_label)
        val itemSubLabel: TextView = itemView.findViewById(R.id.item_settings_row_sub_label)
        val itemIcon: ImageView = itemView.findViewById(R.id.item_settings_row_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_setting, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO break down below code blocks into separate methods.

        // get the data model based on position
        val item = optionsList[position]

        // set row label
        holder.itemLabel.text = item

        // set row icon
        when (item) {
            ROW_SWITCH_THEME -> {
                holder.itemIcon.setImageResource(R.drawable.ic_invert_colors_black_24dp)
                holder.itemSubLabel.text = settings.theme
            }
            ROW_SET_NEXT_TOKE_REMINDER -> {
                holder.itemIcon.setImageResource(R.drawable.ic_add_alert_black_24dp)

                if (settings.nextTokeReminderTime > 0) {
                    holder.itemSubLabel.text =
                        settings.nextTokeReminderTime.toString()//TODO format Long > readable time
                } else {
                    holder.itemSubLabel.text = "Not set"
                }
            }
            ROW_CLEAR_DATA -> {
                holder.itemIcon.setImageResource(R.drawable.ic_delete_sweep_black_24dp)
                holder.itemSubLabel.text = "Delete saved data"
            }
            ROW_ABOUT -> {
                holder.itemIcon.setImageResource(R.drawable.ic_info_black_24dp)
                holder.itemSubLabel.visibility = View.GONE
            }
        }

        // handle row onclick
        holder.itemView.setOnClickListener {
            when (item) {
                ROW_SWITCH_THEME -> {
                    switchAppTheme(holder.itemView.context)
                }
                ROW_SET_NEXT_TOKE_REMINDER -> {
                    // TODO - handle functionality
                    Toast.makeText(
                        holder.itemView.context,
                        "TODO: Set Toke Reminder", Toast.LENGTH_SHORT
                    ).show()
                }
                ROW_CLEAR_DATA -> {
                    showClearDataConfirmationDialog(holder.itemView.context)
                }
                ROW_ABOUT -> {
                    // TODO - handle functionality
                    Toast.makeText(
                        holder.itemView.context,
                        "TODO: Go to About",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return optionsList.count()
    }

    /**
     * Set the [SettingsListAdapter] data and call [notifyDataSetChanged]
     *
     * @param options settings options [ArrayList<String>]
     */
    fun setData(options: ArrayList<String>) {
        optionsList = options
        notifyDataSetChanged()
    }

    /**
     * Prepare and show this confirmation dialog when [Settings.ROW_CLEAR_DATA] is selected
     *
     * @param ctx the [Context] in order to display a Toast message
     */
    private fun showClearDataConfirmationDialog(ctx: Context) {
        MaterialDialog(ctx).show {
            title(R.string.dialog_confirmation_are_you_sure)
            message(R.string.dialog_clear_data_description)
            positiveButton(R.string.clear) { dialog ->
                // clear and dismiss
                viewModel.clearAllData()
                dialog.dismiss()

                Toast.makeText(
                    ctx,
                    "Deleted all saved Tokes data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            negativeButton(R.string.cancel) { dismiss() }
        }
    }

    private fun switchAppTheme(ctx: Context) {
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                // save settings
                settings.apply {
                    theme = Settings.Value.LIGHT_THEME
                    viewModel.saveSettings(this)
                }
                // change to LIGHT THEME & recreate activity for changes to take effect
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                (ctx as MainActivity).recreate()
            }
            else -> {
                // save settings
                settings.apply {
                    theme = Settings.Value.DARK_THEME
                    viewModel.saveSettings(this)
                }
                // change to DARK THEME & recreate activity for changes to take effect
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                (ctx as MainActivity).recreate()
            }
        }
    }

}