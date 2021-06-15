package com.weinstudio.affari.ui.create.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.create.viewmodel.CreateViewModel

class CreateFragment : Fragment() {

    companion object {
        fun newInstance() = CreateFragment()
    }

    private val viewModel: CreateViewModel by lazy {
        ViewModelProvider(this).get(CreateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}