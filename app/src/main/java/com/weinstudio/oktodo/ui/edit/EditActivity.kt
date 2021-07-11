package com.weinstudio.oktodo.ui.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.entity.Problem
import com.weinstudio.oktodo.ui.edit.view.EditFragment

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.title = getString(R.string.problem)
        toolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_close, theme)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val problemStr: String? = intent.getStringExtra(Problem.PROBLEM_EXTRA_TAG)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EditFragment.newInstance(problemStr))
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_create) {
            val listener = supportFragmentManager.findFragmentById(R.id.container)
                    as OkButtonListener
            listener.onButtonPressed()
            return true
        }
        return false
    }
}