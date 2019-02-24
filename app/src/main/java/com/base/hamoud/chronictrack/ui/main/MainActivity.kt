package com.base.hamoud.chronictrack.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.ui.drawer.BottomAppDrawerFragment
import com.base.hamoud.chronictrack.ui.drawer.HitFormBottomDrawerFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var loggedInUser: User

    private lateinit var hitListView: RecyclerView
    private lateinit var adapter: HitListAdapter
    private lateinit var hitCountTextView: TextView

    private lateinit var bottomNavDrawerFragment: BottomAppDrawerFragment
    private lateinit var hitFormBottomDrawerFragment: HitFormBottomDrawerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColorToInvertedIcons()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // prepare ui
        prepareBottomAppSheet()
        prepareHitFormBottomAppSheet()
        prepareTodaysHitCountView()
        prepareHitsRecyclerView()
        prepareHitBtn()

        // observe
        observeOnUserLoggedIn()
        observeOnGetUserHitsLive()

        viewModel.refreshHitsList()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun observeOnUserLoggedIn() {
        viewModel.loggedInUserLive.observe(this, Observer {
            if (it != null) {
                // set user, set profile, etc.
                loggedInUser = it
            }
        })
    }

    private fun observeOnGetUserHitsLive() {
        viewModel.userHitsListLive.observe(this, Observer {
            if (it != null) {
                hitCountTextView.text = it.count().toString()
                adapter.setHits(it)
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

    private fun prepareTodaysHitCountView() {
        hitCountTextView = findViewById(R.id.todays_hit_count)
    }

    private fun prepareHitBtn() {
        val hitBtn = findViewById<FloatingActionButton>(R.id.hit_btn)
        hitBtn.setOnClickListener {
            hitFormBottomDrawerFragment.show(supportFragmentManager, HitFormBottomDrawerFragment::javaClass.name)
        }
    }

    private fun prepareHitsRecyclerView() {
        hitListView = findViewById<RecyclerView>(R.id.hits_recyclerview)
        adapter = HitListAdapter(this)
        hitListView.adapter = adapter
        hitListView.layoutManager = LinearLayoutManager(this)
    }

    private fun prepareBottomAppSheet() {
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
        bottomAppBar.setNavigationOnClickListener {
            bottomNavDrawerFragment = BottomAppDrawerFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, BottomAppDrawerFragment::javaClass.name)
        }
    }

    private fun prepareHitFormBottomAppSheet() {
        hitFormBottomDrawerFragment = HitFormBottomDrawerFragment()
        hitFormBottomDrawerFragment.saveHit.observe(this, androidx.lifecycle.Observer { hit ->
            if (hit != null) {
                hit.userId = loggedInUser.id
                hitListView.scrollToPosition(0)
                viewModel.insertHit(hit)
                hitFormBottomDrawerFragment.dismiss()
                Toast.makeText(this, "Hit Saved!", Toast.LENGTH_LONG).show()
            }
        })
    }

}
