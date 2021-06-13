package com.weinstudio.affari.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.adapter.TasksAdapter
import com.weinstudio.affari.ui.viewmodel.TasksViewModel

class TasksFragment : Fragment() {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private val viewModel: TasksViewModel by lazy {
        ViewModelProvider(this).get(TasksViewModel::class.java)
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
        refresher.setOnRefreshListener { viewModel.updateTasks() }

        val adapter = TasksAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        val dividerDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)

        dividerDecoration.setDrawable(
            getDrawable(
                resources,
                R.drawable.divider,
                requireContext().theme
            )!!
        )
        recycler.addItemDecoration(dividerDecoration)

        viewModel.getTasks()
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