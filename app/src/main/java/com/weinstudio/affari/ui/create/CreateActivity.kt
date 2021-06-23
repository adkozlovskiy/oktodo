package com.weinstudio.affari.ui.create

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.weinstudio.affari.R
import com.weinstudio.affari.databinding.CreateActivityBinding
import com.weinstudio.affari.ui.create.view.CreateFragment
import com.weinstudio.affari.ui.main.MainActivity

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: CreateActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CreateActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbar.title = getString(R.string.create)
        binding.toolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_return, theme)
        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateFragment.newInstance())
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
        if (item.itemId == R.id.done) {
            val intent = Intent(this, MainActivity::class.java)
        }
        return true
    }
}