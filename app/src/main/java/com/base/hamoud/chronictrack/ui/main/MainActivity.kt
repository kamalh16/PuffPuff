package com.base.hamoud.chronictrack.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var loggedInUser: User? = null
    private lateinit var viewModel: MainViewModel

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColorToInvertedIcons()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareBottomNavigationView()

        // observe
        observeOnUserLoggedInLive()
    }

    override fun onSupportNavigateUp() =
          findNavController(R.id.main_nav_host_fragment).navigateUp()

    /**
     * Replaces the current screen with the passed in [screen]
     */
    public fun goToScreen(screen: Fragment, shouldAddToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, screen)
        if (shouldAddToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
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
     * Setup BottomNavigationView's onItemSelectedListener to handle navigating
     * to different screens using [goToScreen].
     */
    private fun prepareBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView?.let { bottomNav ->
            bottomNav.apply {
                // setup with nav controller
                val navController = findNavController(R.id.main_nav_host_fragment)
                setupWithNavController(navController)

                // handle onClick
                setOnNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.item_home_screen -> {
                            navController.navigate(R.id.home_screen)
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.item_toke_log_screen -> {
                            navController.navigate(R.id.toke_log_screen)
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.item_settings_screen -> {
                            navController.navigate(R.id.settings_screen)
                            return@setOnNavigationItemSelectedListener true
                        }
                    }
                    false
                }

                // set default selection
                selectedItemId = R.id.item_home_screen

                // prevents ability to reselect tab
                setOnNavigationItemReselectedListener {
                    return@setOnNavigationItemReselectedListener
                }
            }
        }
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
