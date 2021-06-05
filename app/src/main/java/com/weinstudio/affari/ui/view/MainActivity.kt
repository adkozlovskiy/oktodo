package com.weinstudio.affari.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.presenter.MainPresenter
import moxy.presenter.InjectPresenter

class MainActivity : AppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}