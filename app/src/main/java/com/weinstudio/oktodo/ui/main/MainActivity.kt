package com.weinstudio.oktodo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.databinding.ActivityMainBinding
import com.weinstudio.oktodo.ui.edit.EditActivity
import com.weinstudio.oktodo.ui.main.view.BottomSheetFragment
import com.weinstudio.oktodo.ui.main.view.ProblemsFragment
import com.weinstudio.oktodo.ui.settings.SettingsActivity
import com.weinstudio.oktodo.util.getDrawableCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val eyeOpenedResourceDrawable by lazy {
        getDrawableCompat(R.drawable.ic_eye_open)
    }

    private val eyeClosedResourceDrawable by lazy {
        getDrawableCompat(R.drawable.ic_eye_close)
    }

    private val eyeOpenedResourceString by lazy {
        getString(R.string.eye_enabled)
    }

    private val eyeClosedResourceString by lazy {
        getString(R.string.eye_disabled)
    }

    val viewModel: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    lateinit var eyeButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabCreate.setOnClickListener { launchEditActivity() }

        setSupportActionBar(binding.bottomAppBar)
        binding.bottomAppBar.setNavigationOnClickListener {
            showBottomSheetFragment()
        }

        if (savedInstanceState == null) {
            setFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_app_bar, menu)
        eyeButton = menu.findItem(R.id.action_eye)

        setEyeButtonDrawable(viewModel.isEyeButtonEnabled())
        return true
    }

    @ExperimentalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                launchSettingsActivity()
                return true
            }

            R.id.action_eye -> {
                viewModel.changeEyeButtonEnabled()
                showEyeButtonSnack(viewModel.isEyeButtonEnabled())
                setEyeButtonDrawable(viewModel.isEyeButtonEnabled())
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun launchEditActivity() {
        val intent = Intent(this, EditActivity::class.java)
        startActivity(intent)
    }

    private fun launchSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun showBottomSheetFragment() {
        val fragment = BottomSheetFragment.newInstance()
        fragment.show(supportFragmentManager, BottomSheetFragment.TAG)
    }

    private fun setFragment() {
        val fragment = ProblemsFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment, ProblemsFragment.TAG)
        transaction.commit()
    }

    private fun setEyeButtonDrawable(enabled: Boolean) {
        eyeButton.icon = if (enabled) {
            eyeOpenedResourceDrawable

        } else eyeClosedResourceDrawable
    }

    private fun showEyeButtonSnack(enabled: Boolean) {
        val title = if (enabled) {
            eyeOpenedResourceString

        } else eyeClosedResourceString

        val snack = Snackbar.make(binding.root, title, Snackbar.LENGTH_LONG)
        snack.anchorView = binding.fabCreate
        snack.show()
    }
}