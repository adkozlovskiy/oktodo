package com.weinstudio.affari.ui.view

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
        return inflater.inflate(R.layout.tasks_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvTasks: RecyclerView = view.findViewById(R.id.recycler)
        val refresher: SwipeRefreshLayout = view.findViewById(R.id.refresher)

        val adapter = TasksAdapter()
        rvTasks.adapter = adapter
        rvTasks.layoutManager = LinearLayoutManager(context)

        viewModel.getTasks()
            .observe(viewLifecycleOwner, {
                it?.let {
                    adapter.updateData(it)

                    if (refresher.isRefreshing) {
                        refresher.isRefreshing = false
                    }
                }
            })

        refresher.setOnRefreshListener { viewModel.updateTasks() }
    }
}