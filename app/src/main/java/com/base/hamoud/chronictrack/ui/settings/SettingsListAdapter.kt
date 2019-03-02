package com.base.hamoud.chronictrack.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.ui.settings.model.SettingsItem


class SettingsListAdapter(private val viewModel: SettingsViewModel) :
      RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {

    var optionsList: ArrayList<String> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemLabel: TextView = itemView.findViewById(R.id.item_settings_option_label)
        private val itemDeleteIcon: ImageView = itemView.findViewById(R.id.item_settings_delete_all_icon)
        private val itemDarkThemeIcon: ImageView = itemView.findViewById(R.id.item_settings_dark_theme_icon)
        private val itemAboutIcon: ImageView = itemView.findViewById(R.id.item_settings_about_icon)

        fun showDeleteIcon() {
            itemDeleteIcon.visibility = View.VISIBLE
            hideDarkThemeIcon()
            hideAboutIcon()
        }

        private fun hideDeleteIcon() {
            itemDeleteIcon.visibility = View.INVISIBLE
        }

        fun showDarkThemeIcon() {
            itemDarkThemeIcon.visibility = View.VISIBLE
            hideDeleteIcon()
            hideAboutIcon()
        }

        private fun hideDarkThemeIcon() {
            itemDarkThemeIcon.visibility = View.INVISIBLE
        }

        fun showAboutIcon() {
            itemAboutIcon.visibility = View.VISIBLE
            hideDeleteIcon()
            hideDarkThemeIcon()
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
            SettingsItem.DARK_MODE -> {
                holder.showDarkThemeIcon()
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
                SettingsItem.DARK_MODE -> {
                    Toast.makeText(holder.itemView.context, "Switched to Dark Mode", Toast.LENGTH_SHORT).show()
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

}