package com.base.hamoud.chronictrack

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.hamoud.chronictrack.data.TokesDatabase
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import com.base.hamoud.chronictrack.ui.drawer.BottomAppDrawerFragment
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
    private lateinit var userRepo: UserRepo
    private lateinit var hitRepo: HitRepo
    private lateinit var hits: List<Hit>
    private var hitCount = 0

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.db = TokesDatabase.getDatabase(this)
        userRepo = UserRepo(db.userDao())
        hitRepo = HitRepo(db.hitDao())

        val recyclerView = findViewById<RecyclerView>(R.id.hits_recyclerview)
        val adapter = HitListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(
                this, (recyclerView.layoutManager as LinearLayoutManager).orientation))

        val userId = UUID.randomUUID().toString()
        scope.launch {
            userRepo.insert(User(userId, "kamal"))
            hits = hitRepo.getAllHits()
            hitCount = hits.size
            adapter.setHits(hits)
        }

        val hitTextView = findViewById<TextView>(R.id.hit_count)
        scope.launch {
            hitCount = hitRepo.getAllHits().count()
            hitTextView.text = hitCount.toString()
        }

        val hitBtn = findViewById<FloatingActionButton>(R.id.hit_btn)
        hitBtn.setOnClickListener {
            val hit = Hit(userId = userId)
            scope.launch {
                hitRepo.insert(hit)
                hits = hitRepo.getAllHits().reversed()
                hitCount = hits.size
            }
            adapter.setHits(hits)

            hitTextView.text = (++hitCount).toString()
        }

        prepareBottomAppBar()
    }

    private fun prepareBottomAppBar() {
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_app_bar)
        bottomAppBar.setNavigationOnClickListener {
            val bottomNavDrawerFragment = BottomAppDrawerFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }
    }

}
