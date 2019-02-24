package com.base.hamoud.chronictrack.ui.main

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
import com.base.hamoud.chronictrack.ui.drawer.HitFormBottomDrawerFragment
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
    private var user: User = User(UUID.randomUUID().toString(), "Chron")
    private lateinit var hitRepo: HitRepo
    private lateinit var hits: List<Hit>
    private lateinit var bottomNavDrawerFragment: BottomAppDrawerFragment
    private lateinit var hitFormBottomDrawerFragment: HitFormBottomDrawerFragment
    private var hitCount = 0
    private lateinit var hitCountTextView: TextView
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
        viewModel.insertUser(user)

        // prepare ui
        prepareBottomAppSheet()
        prepareHitFormBottomAppSheet()
        prepareTodaysHitCountView()
        prepareHitsRecyclerView()
        prepareHitBtn()


        // observe
        observeOnUserHitsLive()

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
        scope.launch {
            hitCount = hitRepo.getAllHits().count()
            hitCountTextView.text = hitCount.toString()
        }
    }

    private fun prepareHitBtn() {
        val hitBtn = findViewById<FloatingActionButton>(R.id.hit_btn)
        hitBtn.setOnClickListener {
            hitFormBottomDrawerFragment.show(supportFragmentManager, "HitFormBottomDrawer")
        }
    }

    private fun prepareHitsRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.hits_recyclerview)
        adapter = HitListAdapter(this)
//        adapter.hitsLiveData.observe(this, androidx.lifecycle.Observer { hitsRefreshed: Boolean ->
//            if (hitsRefreshed) {
//                resetRecyclerView()
//            }
//        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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
            scope.launch {
                hit.userId = user.id
                hitRepo.insert(hit)
                viewModel.refreshHitsList()
                hitFormBottomDrawerFragment.dismiss()
            }
            Toast.makeText(this, "Hit Saved!", Toast.LENGTH_LONG).show()
        })
    }

}
