package com.base.hamoud.chronictrack.ui.main

import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    // set logged in user
    private var user: User = User(UUID.randomUUID().toString(), "Chron")

    private lateinit var viewModel: MainViewModel

    private lateinit var hitListView: RecyclerView
    private lateinit var adapter: HitListAdapter
    private lateinit var hitCountTextView: TextView

    private lateinit var bottomNavDrawerFragment: BottomAppDrawerFragment
    private lateinit var hitFormBottomDrawerFragment: HitFormBottomDrawerFragment

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.insertUser(user)

        // prepare ui
        prepareBottomAppSheet()
        prepareHitFormBottomAppSheet()
        prepareTodaysHitCountView()
        prepareHitsRecyclerView()
        prepareHitBtn()


        // observe
        observeOnUserHitsLive()

        viewModel.refreshHitsList()

//        val userId = UUID.randomUUID().toString()
//        scope.launch {
//            userRepo.insert(User(UUID.randomUUID().toString(), "kamal"))
//            user = userRepo.getUserByUsername("kamal")
//            scope.launch(Dispatchers.Main) {
//                user?.let {
//                    hits = hitRepo.getTodaysHits(it.id).asReversed()
//                    hitCount = hits.size
//                    adapter.setHits(hits)
//                }
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun observeOnUserHitsLive() {
        viewModel.userHitsListLive.observe(this, Observer {
            if (it != null) {
                hitCountTextView.text = it.count().toString()
                adapter.setHits(it)
            }
        })
    }

    private fun prepareTodaysHitCountView() {
        hitCountTextView = findViewById<TextView>(R.id.todays_hit_count)
//        scope.launch {
//            hitCount = hitRepo.getAllHits().count()
//            hitCountTextView.text = hitCount.toString()
//        }
    }

    private fun prepareHitBtn() {
        val hitBtn = findViewById<FloatingActionButton>(R.id.hit_btn)
        hitBtn.setOnClickListener {
            hitFormBottomDrawerFragment.show(supportFragmentManager, "HitFormBottomDrawer")
        }
    }

    private fun prepareHitsRecyclerView() {
        hitListView = findViewById<RecyclerView>(R.id.hits_recyclerview)
        adapter = HitListAdapter(this)
//        adapter.hitsLiveData.observe(this, androidx.lifecycle.Observer { hitsRefreshed: Boolean ->
//            if (hitsRefreshed) {
//                resetRecyclerView()
//            }
//        })
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
                hit.userId = user.id
                viewModel.insertHit(hit)
                hitFormBottomDrawerFragment.dismiss()
                Toast.makeText(this, "Hit Saved!", Toast.LENGTH_LONG).show()
            }
        })
    }

}
