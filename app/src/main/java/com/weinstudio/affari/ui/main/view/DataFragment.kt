package com.weinstudio.affari.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.main.adapter.DataAdapter
import com.weinstudio.affari.ui.main.viewmodel.DataViewModel

class DataFragment : Fragment() {

    companion object {
        fun newInstance() = DataFragment()
    }

    private val viewModel: DataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler: RecyclerView = view.findViewById(R.id.recycler)
        val refresher: SwipeRefreshLayout = view.findViewById(R.id.refresher)
        refresher.setOnRefreshListener { viewModel.updateData() }

        val adapter = DataAdapter(requireContext())
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        viewModel.getData()
            .observe(viewLifecycleOwner, {
                it?.let {
                    adapter.updateData(it)

                    if (refresher.isRefreshing) {
                        refresher.isRefreshing = false
                    }
                }
            })
    }
}