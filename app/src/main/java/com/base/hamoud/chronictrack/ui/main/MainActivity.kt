package com.base.hamoud.chronictrack.ui.main

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.TokesDatabase
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import com.base.hamoud.chronictrack.ui.drawer.BottomAppDrawerFragment
import com.base.hamoud.chronictrack.ui.drawer.HitFormAppDrawerFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    private lateinit var db: TokesDatabase
    private lateinit var adapter: HitListAdapter
    private lateinit var userRepo: UserRepo
    private var user: User? = null
    private lateinit var hitRepo: HitRepo
    private lateinit var hits: List<Hit>
    private lateinit var bottomNavDrawerFragment: BottomAppDrawerFragment
    private lateinit var hitFormAppDrawerFragment: HitFormAppDrawerFragment
    private var hitCount = 0
    private lateinit var hitTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private lateinit var viewModel: ChronicTrackerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.db = TokesDatabase.getDatabase(this)
        userRepo = UserRepo(db.userDao())
        hitRepo = HitRepo(db.hitDao())

        viewModel = ViewModelProviders.of(this).get(ChronicTrackerViewModel::class.java)

        recyclerView = findViewById<RecyclerView>(R.id.hits_recyclerview)
        adapter = HitListAdapter(this)
        adapter.hitsLiveData.observe(this, androidx.lifecycle.Observer { hitsRefreshed: Boolean ->
            if (hitsRefreshed) {
                resetRecyclerView()
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this, (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )

//        val userId = UUID.randomUUID().toString()
        scope.launch {
            userRepo.insert(User(UUID.randomUUID().toString(), "kamal"))
            user = userRepo.getUserByUsername("kamal")
            user?.let {
                hits = hitRepo.getTodaysHits(it.id).asReversed()
                hitCount = hits.size
                adapter.setHits(hits)
            }
        }

        hitTextView = findViewById<TextView>(R.id.todays_hit_count)
        scope.launch {
            hitCount = hitRepo.getAllHits().count()
            hitTextView.text = hitCount.toString()
        }

        val hitBtn = findViewById<FloatingActionButton>(R.id.hit_btn)
        hitBtn.setOnClickListener {
            hitFormAppDrawerFragment.show(supportFragmentManager, hitFormAppDrawerFragment.tag)
        }

        prepareBottomAppSheet()
        prepareHitFormBottomAppSheet()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun resetRecyclerView() {
        scope.launch {
            hits = hitRepo.getAllHits().asReversed()
        }
        hits.let {
            hitCount = hits.size
            hitTextView.text = hitCount.toString()
            (recyclerView.adapter as HitListAdapter).setHits(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun prepareBottomAppSheet() {
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
        bottomAppBar.setNavigationOnClickListener {
            bottomNavDrawerFragment = BottomAppDrawerFragment()
        }
    }

    private fun prepareHitFormBottomAppSheet() {
        hitFormAppDrawerFragment = HitFormAppDrawerFragment()
        hitFormAppDrawerFragment.saveHit.observe(this, androidx.lifecycle.Observer { hit ->
            scope.launch {
                hit.userId = user!!.id
                hitRepo.insert(hit)
                hitFormAppDrawerFragment.dismiss()
            }
            Toast.makeText(this, "Hit Saved!", Toast.LENGTH_LONG).show()
        })
    }

}
