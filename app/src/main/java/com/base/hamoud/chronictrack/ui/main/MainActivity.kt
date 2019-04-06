package com.base.hamoud.chronictrack.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.drawable.Icon
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
import java.util.*


class MainActivity : AppCompatActivity() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: MainViewModel

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppTheme()
        setContentView(R.layout.activity_main)

        // read intents extra message, if a shortcut caused the intent we read the data and
        //      direct the user to the correct page
        readIntentsAndPrepareRedirectionsForShortcuts()

        // prepare shortcuts
        prepareShortcuts()
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareBottomNavigationView()

        // observe
        observeOnUserLoggedInLive()
    }

    override fun onResume() {
        super.onResume()
        readIntentsAndPrepareRedirectionsForShortcuts()
    }

    private fun readIntentsAndPrepareRedirectionsForShortcuts() {
        val intentMsg = intent?.extras?.get(MainNavScreen.toString())
        Timber.i("IntentMsg: $intentMsg")
        intentMsg.let {
            val navController = findNavController(R.id.main_nav_host_fragment)
            val rootScreenNavOptions = NavOptions
                .Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()

            when (intentMsg) {
                MainNavScreen.HOME_SCREEN -> {
                    navController.currentDestination?.label = MainNavScreen.HOME_SCREEN
                    navController.navigate(
                        R.id.home_screen, null, rootScreenNavOptions
                    )
                }
                MainNavScreen.ADD_TOKE_SCREEN -> {
                    navController.currentDestination?.label = MainNavScreen.TOKE_LOG_SCREEN
                    navController.navigate(
                        R.id.toke_log_screen, null, rootScreenNavOptions
                    )
                    navController.currentDestination?.label = MainNavScreen.ADD_TOKE_SCREEN
                    navController.navigate(
                        R.id.add_toke_screen, null, rootScreenNavOptions
                    )
                }
                MainNavScreen.TOKE_LOG_SCREEN -> {
                    navController.currentDestination?.label = MainNavScreen.TOKE_LOG_SCREEN
                    navController.navigate(
                        R.id.toke_log_screen, null, rootScreenNavOptions
                    )
                }
            }
        }
    }

    private fun prepareShortcuts() {
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)

        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.action = Intent.ACTION_VIEW
        homeIntent.putExtra(MainNavScreen.toString(), MainNavScreen.HOME_SCREEN)

        val homeShortcut = ShortcutInfo.Builder(this, "home")
            .setShortLabel("Home")
            .setLongLabel("Go Home")
            .setIcon(Icon.createWithResource(this, R.drawable.ic_home_alt_outline_black_24dp))
            .setIntent(
                homeIntent
            )
            .build()

        val addTokeIntent = Intent(this, MainActivity::class.java)
        addTokeIntent.action = Intent.ACTION_VIEW
        addTokeIntent.putExtra(MainNavScreen.toString(), MainNavScreen.ADD_TOKE_SCREEN)

        val addTokeShortcut = ShortcutInfo.Builder(this, "add_toke")
            .setShortLabel("Add Toke")
            .setLongLabel("Add a Toke")
            .setIcon(Icon.createWithResource(this, R.drawable.ic_toke_log_black_24dp))
            .setIntent(
                addTokeIntent
            )
            .build()

        val tokeLogIntent = Intent(this, MainActivity::class.java)
        tokeLogIntent.action = Intent.ACTION_VIEW
        tokeLogIntent.putExtra(MainNavScreen.toString(), MainNavScreen.TOKE_LOG_SCREEN)

        val tokeLogShortcut = ShortcutInfo.Builder(this, "toke_log")
            .setShortLabel("Toke Log")
            .setLongLabel("Go to Toke Log")
            .setIcon(Icon.createWithResource(this, R.drawable.ic_toke_log_black_24dp))
            .setIntent(
                tokeLogIntent
            )
            .build()

        shortcutManager!!.dynamicShortcuts = Arrays.asList(homeShortcut, addTokeShortcut, tokeLogShortcut)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.i(intent.toString())
        val extras = intent?.extras
        val navController = findNavController(R.id.main_nav_host_fragment)
        val rootScreenNavOptions = NavOptions
            .Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .build()
        Timber.i(extras?.getString(MainNavScreen.toString()))
        when (extras?.getString(MainNavScreen.toString())) {
            MainNavScreen.HOME_SCREEN -> {

                navController.currentDestination?.label = MainNavScreen.HOME_SCREEN
                navController.navigate(
                    R.id.home_screen, null, rootScreenNavOptions
                )
            }
            MainNavScreen.ADD_TOKE_SCREEN -> {
                navController.currentDestination?.label = MainNavScreen.TOKE_LOG_SCREEN
                navController.navigate(
                    R.id.toke_log_screen, null, rootScreenNavOptions
                )
                navController.currentDestination?.label = MainNavScreen.ADD_TOKE_SCREEN
                navController.navigate(
                    R.id.add_toke_screen, null, rootScreenNavOptions
                )
            }
            MainNavScreen.TOKE_LOG_SCREEN -> {
                navController.currentDestination?.label = MainNavScreen.TOKE_LOG_SCREEN
                navController.navigate(
                    R.id.toke_log_screen, null, rootScreenNavOptions
                )
            }
        }
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
//                Timber.i("logged in user: ${it.username}, ${it.id}")
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
                        .setEnterAnim(R.anim.fade_in)
                        .setExitAnim(R.anim.fade_out)
                        .setPopUpTo(it.itemId, true)
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
        window.statusBarColor = Color.parseColor(getString(R.color.colorStatusBar))
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
        window.statusBarColor = Color.parseColor(getString(R.color.colorStatusBar))
    }

}
