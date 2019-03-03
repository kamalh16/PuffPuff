package com.base.hamoud.chronictrack.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.base.hamoud.chronictrack.Constants
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.ui.main.MainNavScreen.rootScreenList
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: MainViewModel

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppTheme()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareBottomNavigationView()

        // observe
        observeOnUserLoggedInLive()
    }

    override fun onSupportNavigateUp() =
          findNavController(R.id.main_nav_host_fragment).navigateUp()

    override fun onBackPressed() {
        val navController = findNavController(R.id.main_nav_host_fragment)
        // handle onBackPressed while on root screens:
        // - if we're on a root screen finish() (close) the activity
        // - if we're not on a root screen, pop the back stack
        val currentDestinationLabel = navController.currentDestination?.label
        if (currentDestinationLabel in rootScreenList) {
            finish()
        } else {
            navController.popBackStack()
        }
    }

    private fun setAppTheme() {
        val prefs = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(Constants.PREF_IS_DARK_THEME, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setDarkThemeStatusBarColor()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setLightThemeStatusBarColor()
        }
    }

    private fun observeOnUserLoggedInLive() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                Timber.i("logged in user: ${it.username}, ${it.id}")
                loggedInUser = it
            }
        })
    }

    /**
     * Setup BottomNavigationView's with [NavigationUI.setupWithNavController]
     * to handle navigating to different screens.
     */
    private fun prepareBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView?.let { bottomNav ->
            bottomNav.apply {
                // setup with nav controller
                val navController = findNavController(R.id.main_nav_host_fragment)
                NavigationUI.setupWithNavController(this, navController)
                // NOTE ---
                // You need to have the same id as your fragment in your R.menu.menu_bottom_nav
                // and in your R.navigation.nav_graph for Navigation-UI to work properly with
                // selecting the right bottomNavigationView tab to highlight. ~ Moe

                // handle onClick
                setOnNavigationItemSelectedListener {

                    val rootScreenNavOptions = NavOptions
                          .Builder()
                          .setPopUpTo(it.itemId, true)
                          .setLaunchSingleTop(true)
                          .build()

                    when (it.itemId) {
                        R.id.home_screen -> {
                            navController.currentDestination?.label = MainNavScreen.HOME_SCREEN
                            navController.navigate(
                                  R.id.home_screen, null, rootScreenNavOptions
                            )
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.toke_log_screen -> {
                            navController.currentDestination?.label = MainNavScreen.TOKE_LOG_SCREEN
                            navController.navigate(
                                  R.id.toke_log_screen, null, rootScreenNavOptions
                            )
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.settings_screen -> {
                            navController.currentDestination?.label = MainNavScreen.SETTINGS_SCREEN
                            navController.navigate(
                                  R.id.settings_screen, null, rootScreenNavOptions
                            )
                            return@setOnNavigationItemSelectedListener true
                        }
                    }
                    false
                }

                // prevents ability to reselect tab
                setOnNavigationItemReselectedListener {
                    return@setOnNavigationItemReselectedListener
                }
            }
        }
    }

    /**
     * Invert the status bar icon colors (like Google Keep or Calendar) when in light theme
     * and set the status bar color and icons for dark theme.
     *
     * @see <a href="https://stackoverflow.com/a/45196710/2340813">StackOverFlow source</a>
     */
    @SuppressLint("ResourceType")
    private fun setDarkThemeStatusBarColor() {
        // dark
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
              View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.parseColor(getString(R.color.statusBarColor))
    }

    /**
     * Invert the status bar icon colors (like Google Keep or Calendar) when in dark theme
     * and set the status bar color and icons for light theme.
     *
     * @see <a href="https://stackoverflow.com/a/45196710/2340813">StackOverFlow source</a>
     */
    @SuppressLint("ResourceType")
    private fun setLightThemeStatusBarColor() {
        // light
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
              View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
              View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.parseColor(getString(R.color.statusBarColor))
    }

}
