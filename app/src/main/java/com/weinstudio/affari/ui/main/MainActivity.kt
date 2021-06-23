package com.weinstudio.affari.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.create.CreateActivity
import com.weinstudio.affari.ui.main.view.BottomSheetFragment
import com.weinstudio.affari.ui.main.view.ProblemsFragment
import com.weinstudio.affari.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.all_tasks)

        val bottomAppBar: BottomAppBar = findViewById(R.id.bottom_app_bar)
        setSupportActionBar(bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            val fragment = BottomSheetFragment.newInstance()
            fragment.show(supportFragmentManager, "bottom sheet")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
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
}