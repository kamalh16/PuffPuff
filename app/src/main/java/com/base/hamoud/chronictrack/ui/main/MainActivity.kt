package com.base.hamoud.chronictrack.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.ui.drawer.HitFormBottomDrawerFragment
import com.base.hamoud.chronictrack.ui.drawer.NavDrawerBottomSheetFragment
import com.base.hamoud.chronictrack.ui.home.HomeFragment
import com.base.hamoud.chronictrack.ui.log.LogFragment
import com.base.hamoud.chronictrack.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var loggedInUser: User

    private lateinit var hitListRecyclerView: RecyclerView
    private lateinit var adapter: HitListAdapter
    private lateinit var hitCountTextView: TextView

    private lateinit var navDrawerBottomSheetFragment: NavDrawerBottomSheetFragment
    private lateinit var hitFormBottomDrawerFragment: HitFormBottomDrawerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColorToInvertedIcons()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareBottomNavigation()
        prepareNavDrawerBottomSheet()
        prepareHitFormBottomSheet()
        prepareTodaysHitCountView()
        prepareHitsRecyclerView()
        prepareHitBtn()

        // observe
        observeOnUserLoggedIn()
        observeOnGetUserHitsCountLive()
        observeOnGetUserHitsListLive()

        // trigger
        viewModel.refreshHitsTotalCount()
        viewModel.refreshHitsList()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun goToScreen(screen: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, screen)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun observeOnUserLoggedIn() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                // set user, set profile, etc.
                loggedInUser = it
            }
        })
    }

    private fun observeOnGetUserHitsCountLive() {
        viewModel.userHitsCount.observe(this, Observer {
            if (it != null) {
                hitCountTextView.text = it.toString()
            }
        })
    }

    private fun observeOnGetUserHitsListLive() {
        viewModel.userHitsListLive.observe(this, Observer {
            if (it != null) {
                adapter.setHits(it)
            }
        })
    }

    private fun prepareTodaysHitCountView() {
        hitCountTextView = findViewById(R.id.todays_hit_count)
    }

    private fun prepareHitBtn() {
//        val hitBtn = findViewById<FloatingActionButton>(R.id.hit_btn)
//        hitBtn.setOnClickListener {
//            hitFormBottomDrawerFragment.show(supportFragmentManager, HitFormBottomDrawerFragment::javaClass.name)
//        }
    }

    private fun prepareHitsRecyclerView() {
        hitListRecyclerView = findViewById(R.id.hits_recyclerview)
        adapter = HitListAdapter(this)

        // setup RecyclerView
        hitListRecyclerView.adapter = adapter
        hitListRecyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        // add swipe-to-delete support
//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
//        itemTouchHelper.attachToRecyclerView(hitListRecyclerView)
    }

    private fun prepareNavDrawerBottomSheet() {
//        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
//        bottomAppBar.setNavigationOnClickListener {
//            navDrawerBottomSheetFragment = NavDrawerBottomSheetFragment()
//            navDrawerBottomSheetFragment.show(supportFragmentManager, NavDrawerBottomSheetFragment::javaClass.name)
//        }
    }

    private fun prepareBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigate_home_screen -> {
                    val songsFragment = HomeFragment.newInstance()
                    goToScreen(songsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigate_log_screen -> {
                    val songsFragment = LogFragment.newInstance()
                    goToScreen(songsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigate_settings_screen -> {
                    val songsFragment = SettingsFragment.newInstance()
                    goToScreen(songsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        // Set default selection
        bottomNavigationView.selectedItemId = R.id.navigate_home_screen
    }

    private fun prepareHitFormBottomSheet() {
        hitFormBottomDrawerFragment = HitFormBottomDrawerFragment()
        hitFormBottomDrawerFragment.saveHit.observe(this, androidx.lifecycle.Observer { hit ->
            if (hit != null) {
                hit.userId = loggedInUser.id
                viewModel.insertHit(hit)
                viewModel.refreshHitsList()
                hitListRecyclerView.scrollToPosition(0)
                hitFormBottomDrawerFragment.dismiss()
                Toast.makeText(this, "Hit Saved!", Toast.LENGTH_LONG).show()
            }
        })
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
