package com.weinstudio.oktodo.ui.edit.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.databinding.ActivityEditBinding
import com.weinstudio.oktodo.ui.edit.OkButtonListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (savedInstanceState == null) {
            val receivedProblemJson = intent.getStringExtra(Problem.PROBLEM_EXTRA_TAG)
            showEditFragment(receivedProblemJson)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_create) {
            val listener = getOkButtonListener()
            listener.onButtonPressed()
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showEditFragment(problem: String?) {
        val editFragment = EditFragment.newInstance(problem)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, editFragment)
            .commitNow()
    }

    private fun getOkButtonListener(): OkButtonListener {
        return supportFragmentManager.findFragmentById(R.id.container) as OkButtonListener
    }
}