package com.base.hamoud.puffpuff.ui.settings.model

import com.base.hamoud.puffpuff.ui.main.MainNavScreen

object SettingsItem {
    const val SWITCH_THEME_LABEL = "Switch Theme"
    const val SET_NEXT_TOKE_REMINDER_LABEL = "Set Next Toke Reminder"
    const val SET_STARTUP_PAGE_LABEL = "Set Startup Page"
    const val CLEAR_DATA_LABEL = "Clear Data"
    const val ABOUT_LABEL = "About"

    var optionStartupPage = MainNavScreen.JOURNAL_SCREEN

    val itemList =
          arrayListOf(
                SWITCH_THEME_LABEL,
                SET_NEXT_TOKE_REMINDER_LABEL,
                SET_STARTUP_PAGE_LABEL,
                CLEAR_DATA_LABEL,
                ABOUT_LABEL
          )
}