package com.base.hamoud.chronictrack.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.Constants
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.ui.main.MainActivity
import com.base.hamoud.chronictrack.ui.settings.model.SettingsItem


class SettingsListAdapter(private val viewModel: SettingsViewModel) :
      RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {

    var optionsList: ArrayList<String> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemLabel: TextView = itemView.findViewById(R.id.item_settings_option_label)
        val itemIcon: ImageView = itemView.findViewById(R.id.item_settings_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_setting, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get the data model based on position
        val item = optionsList[position]

        // set row label
        holder.itemLabel.text = item

        // set row icon
        when (item) {
            SettingsItem.SWITCH_THEME -> {
                holder.itemIcon.setImageResource(R.drawable.ic_invert_colors_black_24dp)
            }
            SettingsItem.SET_NEXT_TOKE_REMINDER -> {
                holder.itemIcon.setImageResource(R.drawable.ic_add_alert_black_24dp)
            }
            SettingsItem.SET_STARTUP_PAGE -> {
                holder.itemIcon.setImageResource(R.drawable.ic_toke_log_black_24dp)
            }
            SettingsItem.CLEAR_DATA -> {
                holder.itemIcon.setImageResource(R.drawable.ic_delete_sweep_black_24dp)
            }
            SettingsItem.ABOUT -> {
                holder.itemIcon.setImageResource(R.drawable.ic_info_black_24dp)
            }
        }

        // handle row onclick
        holder.itemView.setOnClickListener {
            when (item) {
                SettingsItem.SWITCH_THEME -> {
                    onClickSwitchThemeRow(holder.itemView.context)
                }
                SettingsItem.SET_NEXT_TOKE_REMINDER -> {
                    // TODO
                    Toast.makeText(holder.itemView.context, "TODO: Set Toke Reminder", Toast.LENGTH_SHORT).show()
                }
                SettingsItem.SET_STARTUP_PAGE -> {
                    // TODO
                    Toast.makeText(holder.itemView.context, "TODO: Set Home Page", Toast.LENGTH_SHORT).show()
                }
                SettingsItem.CLEAR_DATA -> {
                    showClearDataConfirmationDialog(holder.itemView.context)
                }
                SettingsItem.ABOUT -> {
                    // TODO
                    Toast.makeText(holder.itemView.context, "TODO: Go to About", Toast.LENGTH_SHORT).show()
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
     * @param itemList settings options [ArrayList<String>]
     */
    fun setData(itemList: ArrayList<String>) {
        optionsList = itemList
        notifyDataSetChanged()
    }

    /**
     * Prepare and show this confirmation dialog when [SettingsItem.CLEAR_DATA] is selected
     *
     * @param ctx the [Context] in order to display a Toast message
     */
    private fun showClearDataConfirmationDialog(ctx: Context) {
        val dialog = AlertDialog.Builder(ctx)
        dialog
              .setTitle("Clear all data?")
              .setMessage("This will permanently delete all your saved Tokes data.")
              .setPositiveButton("Clear") { dialogBox, _ ->
                  viewModel.clearAllData()
                  dialogBox.dismiss()

                  Toast.makeText(
                        ctx,
                        "Deleted all saved Tokes data.",
                        Toast.LENGTH_SHORT
                  ).show()
              }
              .setNegativeButton("Cancel") { dialogBox, _ ->
                  dialogBox.dismiss()
              }
              .show()
    }

    private fun onClickSwitchThemeRow(ctx: Context) {
        val preferences = ctx.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // change to LIGHT THEME
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            // update shared prefs
            preferences.edit { putBoolean(Constants.PREF_IS_DARK_THEME, false) }
            // recreate activity for changes to take effect
            (ctx as MainActivity).recreate()
        } else {
            // change to DARK THEME
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            // update shared prefs
            preferences.edit { putBoolean(Constants.PREF_IS_DARK_THEME, true) }
            // recreate activity for changes to take effect
            (ctx as MainActivity).recreate()
        }
    }

}