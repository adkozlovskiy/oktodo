package com.weinstudio.memoria.ui.main

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.weinstudio.memoria.R
import com.weinstudio.memoria.ui.create.CreateActivity
import com.weinstudio.memoria.ui.main.view.BottomSheetFragment
import com.weinstudio.memoria.ui.main.view.ProblemsFragment
import com.weinstudio.memoria.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), FragmentController {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val tasksFragment = ProblemsFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, tasksFragment)
            transaction.commit()
        }

        val fabCreate: FloatingActionButton = findViewById(R.id.fab_create)
        fabCreate.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        val bottomAppBar: BottomAppBar = findViewById(R.id.bottom_app_bar)
        setSupportActionBar(bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            val fragment = BottomSheetFragment.newInstance()
            fragment.show(supportFragmentManager, "bottom sheet")
        }
    }

    private var eyeItem: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
        eyeItem = menu?.findItem(R.id.show_hide)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onProblemDone() {
        val anim = ValueAnimator()
        anim.setIntValues(
            getColor(R.color.on_appbar),
            getColor(R.color.secondary)
        )

        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator ->
            eyeItem?.icon?.setTint(
                (valueAnimator.animatedValue as Int)
            )
        }

        anim.duration = 300

        anim.repeatCount = 2
        anim.start()
    }
}