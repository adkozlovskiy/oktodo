package com.weinstudio.oktodo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.ui.edit.EditActivity
import com.weinstudio.oktodo.ui.main.view.BottomSheetFragment
import com.weinstudio.oktodo.ui.main.view.ProblemsFragment
import com.weinstudio.oktodo.ui.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val callback by lazy {
        supportFragmentManager.findFragmentByTag(ProblemsFragment.TAG)
                as EyeButtonListener
    }

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val tasksFragment = ProblemsFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, tasksFragment, ProblemsFragment.TAG)
            transaction.commit()
        }

        val fabCreate: FloatingActionButton = findViewById(R.id.fab_create)
        fabCreate.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        val bottomAppBar: BottomAppBar = findViewById(R.id.bottom_app_bar)
        setSupportActionBar(bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            val fragment = BottomSheetFragment.newInstance()
            fragment.show(supportFragmentManager, BottomSheetFragment.TAG)
        }

        viewModel.isEyeEnabled.observe(this, {
            callback.onEyeButtonPressed(it)

            eyeItem?.icon = if (it) AppCompatResources.getDrawable(
                this,
                R.drawable.ic_eye_open

            ) else AppCompatResources.getDrawable(this, R.drawable.ic_eye_close)
        })
    }

    private var eyeItem: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
        eyeItem = menu?.findItem(R.id.action_eye)

        val isEnabled = viewModel.isEyeEnabled.value

        if (isEnabled != null) {
            eyeItem?.icon = if (isEnabled) AppCompatResources.getDrawable(
                this,
                R.drawable.ic_eye_open

            ) else AppCompatResources.getDrawable(this, R.drawable.ic_eye_close)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_eye -> {
                val isEnabled = viewModel.isEyeEnabled.value
                if (isEnabled != null) {
                    viewModel.isEyeEnabled.value = !isEnabled

                    val snackTitle = if (isEnabled) {
                        getString(R.string.eye_disabled)

                    } else getString(R.string.eye_enabled)

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