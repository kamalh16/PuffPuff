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

        var itemLabel: TextView = itemView.findViewById(R.id.item_settings_option_label)
        private val itemDeleteIcon: ImageView = itemView.findViewById(R.id.item_settings_delete_all_icon)
        private val itemThemeIcon: ImageView = itemView.findViewById(R.id.item_settings_theme_icon)
        private val itemAboutIcon: ImageView = itemView.findViewById(R.id.item_settings_about_icon)

        fun showDeleteIcon() {
            itemDeleteIcon.visibility = View.VISIBLE
            showThemeIcon()
            hideAboutIcon()
        }

        private fun hideDeleteIcon() {
            itemDeleteIcon.visibility = View.INVISIBLE
        }

        fun showThemeSwitch() {
            itemThemeIcon.visibility = View.VISIBLE
            hideDeleteIcon()
            hideAboutIcon()
        }

        private fun showThemeIcon() {
            itemThemeIcon.visibility = View.INVISIBLE
        }

        fun showAboutIcon() {
            itemAboutIcon.visibility = View.VISIBLE
            hideDeleteIcon()
            showThemeIcon()
        }

        private fun hideAboutIcon() {
            itemAboutIcon.visibility = View.INVISIBLE
        }

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
                holder.showThemeSwitch()
            }
            SettingsItem.CLEAR_DATA -> {
                holder.showDeleteIcon()
            }
            SettingsItem.ABOUT -> {
                holder.showAboutIcon()
            }
        }

        // handle row onclick
        holder.itemView.setOnClickListener {
            when (item) {
                SettingsItem.SWITCH_THEME -> {
                    onClickSwitchThemeRow(holder.itemView.context)
                }
                SettingsItem.CLEAR_DATA -> {
                    showClearDataConfirmationDialog(holder.itemView.context)
                }
                SettingsItem.ABOUT -> {
                    Toast.makeText(holder.itemView.context, "Go to About", Toast.LENGTH_SHORT).show()
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
                        "Deleted all Tokes data.",
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