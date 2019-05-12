package com.base.hamoud.puffpuff.ui.settings

import android.app.Activity
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
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.ui.main.MainActivity
import com.base.hamoud.puffpuff.ui.settings.model.Settings
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_ABOUT
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_CLEAR_DATA
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_SET_NEXT_TOKE_REMINDER
import com.base.hamoud.puffpuff.ui.settings.model.Settings.RowOption.ROW_SWITCH_THEME
import kotlin.collections.ArrayList


class SettingsListAdapter(
    private val activity: Activity,
    private val viewModel: SettingsViewModel
) :
    RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {

    private var settings: Settings = Settings()
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
        val item = optionsList[position]
        setRowLabel(holder.itemLabel, item)
        setRowIcon(holder, item)
        handleRowOnClick(holder.itemView, item)
    }

    private fun setRowLabel(itemLabel: TextView, item: String) {
        itemLabel.text = item
    }

    private fun setRowIcon(holder: ViewHolder, item: String) {
        when (item) {
            ROW_SWITCH_THEME -> {
                holder.itemIcon.setImageResource(R.drawable.ic_invert_colors_black_24dp)
                holder.itemSubLabel.text = settings.theme
            }
            ROW_SET_NEXT_TOKE_REMINDER -> {
                holder.itemIcon.setImageResource(R.drawable.ic_add_alert_black_24dp)

                if (settings.nextTokeReminderTime > 0) {
                    holder.itemSubLabel.text =
                        "${settings.nextTokeReminderTime} mins"
                } else {
                    holder.itemSubLabel.text = "Off"
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
    }

    private fun handleRowOnClick(itemView: View, item: String) {
        // handle row onclick
        itemView.setOnClickListener {
            when (item) {
                ROW_SWITCH_THEME -> {
                    switchAppTheme(it.context)
                }
                ROW_SET_NEXT_TOKE_REMINDER -> {
                    showSetNextTokeReminderDialog(it.context)
                }
                ROW_CLEAR_DATA -> {
                    showClearDataConfirmationDialog(it.context)
                }
                ROW_ABOUT -> {
                    // TODO - handle functionality
                    Toast.makeText(
                        it.context,
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

    fun setUserSettings(userSettings: Settings) {
        settings = userSettings
        notifyDataSetChanged()
    }

    /**
     * Prepare and show this confirmation dialog when [Settings.RowOption.ROW_CLEAR_DATA] is selected
     *
     * @param ctx the [Context] in order to display a Toast message
     */
    private fun showClearDataConfirmationDialog(ctx: Context) {
        MaterialDialog(ctx).show {
            title(R.string.dialog_title_are_you_sure)
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

    /**
     * Prepare and show this confirmation dialog when [Settings.RowOption.ROW_SET_NEXT_TOKE_REMINDER] is selected
     *
     * @param ctx the [Context] in order to display a Toast message
     */
    private fun showSetNextTokeReminderDialog(ctx: Context) {
        val userSettingsNextTokeReminder =
            getNextTokeReminderIndexPosition(settings.nextTokeReminderTime)
        MaterialDialog(ctx).show {
            title(R.string.dialog_title_set_next_toke_reminder)
            message(R.string.dialog_set_next_toke_reminder_description)
            listItemsSingleChoice(
                res = R.array.next_toke_reminder_values_arr,
                initialSelection = userSettingsNextTokeReminder,
                waitForPositiveButton = false
            ) { dialog, index, text ->

                // apply and save user choice
                settings.apply {
                    nextTokeReminderTime = convertNextTokeReminderStringToLongValue(text)
                }

                viewModel.saveSettings(settings)
                viewModel.getUserSettings()

                // TODO: start next time user takes a toke.
                // TODO: for now use to test here.
                if (settings.nextTokeReminderTime > 0) {
                    (activity as MainActivity).startNextTokeReminderNotificationService()
                } else {
                    (activity as MainActivity).stopNextTokeReminderNotificationService()
                }

                Toast.makeText(
                    ctx,
                    "Set next Toke reminder to: $text",
                    Toast.LENGTH_SHORT
                ).show()

                dialog.dismiss()
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


    private fun convertNextTokeReminderStringToLongValue(string: String): Long {
        return when (string) {
            "5 mins" -> {
                5
            }
            "10 mins" -> {
                10
            }
            "15 mins" -> {
                15
            }
            "30 mins" -> {
                30
            }
            "45 mins" -> {
                45
            }
            "60 mins" -> {
                60
            }
            else -> {
                0 // "Off"
            }
        }
    }

    private fun getNextTokeReminderIndexPosition(reminderTime: Long): Int {
        return when (reminderTime) {
            5L -> {
                1
            }
            10L -> {
                2
            }
            15L -> {
                3
            }
            30L -> {
                4
            }
            45L -> {
                5
            }
            60L -> {
                6
            }
            else -> {
                0 // "Off"
            }
        }
    }


}