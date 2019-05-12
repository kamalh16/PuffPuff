package com.base.hamoud.puffpuff.ui.settings.model

/**
 * Encapsulate in-app Settings from a data source (i.e. shared prefs)
 */
data class Settings(
    var theme: String = Value.LIGHT_THEME,
    var nextTokeReminderTime: Long = 0
) {

    /**
     * Settings row option
     */
    object RowOption {
        /**
         * label names
         */
        const val ROW_SWITCH_THEME = "Switch Theme"
        const val ROW_SET_NEXT_TOKE_REMINDER = "Next Toke Reminder"
        const val ROW_CLEAR_DATA = "Clear Data"
        const val ROW_ABOUT = "About"

        /**
         * Array of label names
         */
        val list =
            arrayListOf(
                ROW_SWITCH_THEME,
                ROW_SET_NEXT_TOKE_REMINDER,
                ROW_CLEAR_DATA,
                ROW_ABOUT
            )
    }

    /**
     * Values for a Settings row, these are usually
     * the values persisted in SharedPreferences for a
     * particular [Settings.RowOption]
     */
    object Value {
        const val DARK_THEME = "Dark Theme"
        const val LIGHT_THEME = "Light Theme"
    }
}