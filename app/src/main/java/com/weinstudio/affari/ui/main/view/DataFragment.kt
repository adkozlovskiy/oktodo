package com.weinstudio.affari.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.main.RecyclerItemTouchHelper
import com.weinstudio.affari.ui.main.adapter.ProblemsAdapter
import com.weinstudio.affari.ui.main.viewmodel.DataViewModel

class DataFragment : Fragment() {

    private val adapter by lazy {
        ProblemsAdapter(requireContext())
    }

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

        recycler.setHasFixedSize(true)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) { holder, _, pos ->
                if (holder is ProblemsAdapter.TaskViewHolder) {
                    val removedProblem = adapter.removeProblem(pos)
                    val title: String = removedProblem.title!!

                    val snack = Snackbar.make(
                        requireActivity().findViewById(R.id.root_layout),
                        title + " — " + getString(R.string.deleted_from_yr_tasks),
                        Snackbar.LENGTH_LONG
                    )
                    snack.setAction(getString(R.string.undo)) {
                        adapter.restoreProblem(removedProblem, pos)
                    }
                    snack.setActionTextColor(requireContext().getColor(R.color.secondary))
                    snack.anchorView = requireActivity().findViewById(R.id.fab_create)
                    snack.show()
                }
            }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler)

        viewModel.getData()
            .observe(viewLifecycleOwner, {
                it?.let {
                    adapter.setItems(it.toMutableList())

                    if (refresher.isRefreshing) {
                        refresher.isRefreshing = false
                    }
                }
            })
    }
}