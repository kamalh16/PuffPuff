package com.base.hamoud.puffpuff.ui.main

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.base.hamoud.puffpuff.Constants
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.ui.main.MainNavScreen.rootScreenList
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppTheme()
        setContentView(R.layout.activity_main)

        // read intents extra message, if a shortcut caused the intent we read the data and
        //      direct the user to the correct page
//        readIntentsAndPrepareRedirectionsForShortcuts()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareView()
        prepareBottomNavigationView()

        // triggers
    }

    override fun onResume() {
        super.onResume()
        readIntentsAndPrepareRedirectionsForShortcuts()
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
            MainNavScreen.STATS_SCREEN -> {
                navController.currentDestination?.label = MainNavScreen.STATS_SCREEN
                navController.navigate(
                    R.id.stats_screen, null, rootScreenNavOptions
                )
            }
            MainNavScreen.ADD_TOKE_SCREEN -> {
                navController.currentDestination?.label = MainNavScreen.JOURNAL_SCREEN
                navController.navigate(
                    R.id.journal_screen, null, rootScreenNavOptions
                )
                navController.currentDestination?.label = MainNavScreen.ADD_TOKE_SCREEN
                navController.navigate(
                    R.id.add_toke_screen, null, rootScreenNavOptions
                )
            }
            MainNavScreen.JOURNAL_SCREEN -> {
                navController.currentDestination?.label = MainNavScreen.JOURNAL_SCREEN
                navController.navigate(
                    R.id.journal_screen, null, rootScreenNavOptions
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

    private fun prepareView() {
        // prepare shortcuts
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            prepareShortcuts()
        }
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
                MainNavScreen.STATS_SCREEN -> {
                    navController.currentDestination?.label = MainNavScreen.STATS_SCREEN
                    navController.navigate(
                        R.id.stats_screen, null, rootScreenNavOptions
                    )
                }
                MainNavScreen.ADD_TOKE_SCREEN -> {
                    navController.currentDestination?.label = MainNavScreen.ADD_TOKE_SCREEN
                    navController.navigate(
                        R.id.add_toke_screen, null, null
                    )
                }
                MainNavScreen.JOURNAL_SCREEN -> {
                    navController.currentDestination?.label = MainNavScreen.JOURNAL_SCREEN
                    navController.navigate(
                        R.id.journal_screen, null, rootScreenNavOptions
                    )
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun prepareShortcuts() {
        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)

        // Stats
        val statsIntent = Intent(this, MainActivity::class.java)
        statsIntent.action = Intent.ACTION_VIEW
        statsIntent.putExtra(MainNavScreen.toString(), MainNavScreen.STATS_SCREEN)

        val statsLabel = resources.getString(R.string.label_stats)
        val statsShortcut = ShortcutInfo.Builder(this, "shortcut_stats")
            .setShortLabel(statsLabel)
            .setIcon(Icon.createWithResource(this, R.drawable.ic_assessment_outline_24dp))
            .setIntent(
                statsIntent
            )
            .build()

        // Add Toke
        val addTokeIntent = Intent(this, MainActivity::class.java)
        addTokeIntent.action = Intent.ACTION_VIEW
        addTokeIntent.putExtra(MainNavScreen.toString(), MainNavScreen.ADD_TOKE_SCREEN)

        val addTokeLabel = resources.getString(R.string.label_add_toke)
        val addTokeShortcut = ShortcutInfo.Builder(this, "shortcut_add_toke")
            .setShortLabel(addTokeLabel)
            .setIcon(Icon.createWithResource(this, R.drawable.ic_toke_log_black_24dp))
            .setIntent(
                addTokeIntent
            )
            .build()

        // Journal
        val journalIntent = Intent(this, MainActivity::class.java)
        journalIntent.action = Intent.ACTION_VIEW
        journalIntent.putExtra(MainNavScreen.toString(), MainNavScreen.JOURNAL_SCREEN)

        val journalLabel = resources.getString(R.string.label_journal)
        val journalShortcut = ShortcutInfo.Builder(this, "shortcut_toke_log")
            .setShortLabel(journalLabel)
            .setIcon(Icon.createWithResource(this, R.drawable.ic_toke_log_black_24dp))
            .setIntent(
                journalIntent
            )
            .build()

        // set
        shortcutManager!!.dynamicShortcuts =
            Arrays.asList(statsShortcut, addTokeShortcut, journalShortcut)
    }

    private fun setAppTheme() {
        // FIXME: viewModel's job through repo
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        if (prefs.getString("pref_theme", "Light Theme") == "Dark Theme") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setDarkThemeStatusBarColor()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setLightThemeStatusBarColor()
        }
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
                        R.id.stats_screen -> {
                            navController.currentDestination?.label = MainNavScreen.STATS_SCREEN
                            navController.navigate(
                                R.id.stats_screen, null, rootScreenNavOptions
                            )
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.journal_screen -> {
                            navController.currentDestination?.label = MainNavScreen.JOURNAL_SCREEN
                            navController.navigate(
                                R.id.journal_screen, null, rootScreenNavOptions
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
