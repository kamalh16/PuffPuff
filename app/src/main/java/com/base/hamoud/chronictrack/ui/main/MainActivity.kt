package com.base.hamoud.chronictrack.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.ui.home.HomeScreen
import com.base.hamoud.chronictrack.ui.log.LogScreen
import com.base.hamoud.chronictrack.ui.settings.SettingsScreen
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var loggedInUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColorToInvertedIcons()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareBottomNavigation()
//        prepareHitFormBottomSheet()
    }

    private fun goToScreen(screen: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, screen)
        transaction.commit()
    }

    private fun prepareBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigate_home_screen -> {
                    val homeScreen = HomeScreen.newInstance()
                    goToScreen(homeScreen)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigate_log_screen -> {
                    val logScreen = LogScreen.newInstance()
                    goToScreen(logScreen)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigate_settings_screen -> {
                    val settingsScreen = SettingsScreen.newInstance()
                    goToScreen(settingsScreen)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        // Set default selection
        bottomNavigationView.selectedItemId = R.id.navigate_home_screen
    }

    /**
     * Invert the status bar icon colors (like Google Keep or Calendar).
     *
     * @see <a href="https://stackoverflow.com/a/45196710/2340813">StackOverFlow source</a>
     */
    private fun setStatusBarColorToInvertedIcons() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
              View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
              View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.parseColor("#1A000000")
    }

}
