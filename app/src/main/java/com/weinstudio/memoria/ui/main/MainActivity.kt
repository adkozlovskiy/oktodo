package com.weinstudio.memoria.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.weinstudio.memoria.R
import com.weinstudio.memoria.ui.create.CreateActivity
import com.weinstudio.memoria.ui.main.view.BottomSheetFragment
import com.weinstudio.memoria.ui.main.view.ProblemsFragment
import com.weinstudio.memoria.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    val cb by lazy {
        supportFragmentManager.findFragmentByTag("problems fragment")
                as EyeButtonCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val tasksFragment = ProblemsFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, tasksFragment, "problems fragment")
            transaction.commit()
        }

        viewModel.isEyeEnabled.observe(this, {
            cb.onEyeButtonPressed(it)

            if (it) {
                eyeItem?.icon = AppCompatResources.getDrawable(this, R.drawable.ic_eye_close)

            } else {
                eyeItem?.icon = AppCompatResources.getDrawable(this, R.drawable.ic_eye_open)
            }
        })

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

            R.id.show_hide -> {
                val isEnabled = viewModel.isEyeEnabled.value
                if (isEnabled != null) {
                    viewModel.isEyeEnabled.value = !isEnabled

                    val snackTitle =
                        if (isEnabled) getString(R.string.eye_disabled) else getString(R.string.eye_enabled)

                    val snack = Snackbar.make(
                        findViewById(R.id.root_layout),
                        snackTitle,
                        Snackbar.LENGTH_LONG
                    )

                    snack.anchorView = findViewById(R.id.fab_create)
                    snack.show()
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}