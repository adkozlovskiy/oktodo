package com.weinstudio.memoria.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.weinstudio.memoria.R
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.ui.main.EyeButtonListener
import com.weinstudio.memoria.ui.main.MainActivity
import com.weinstudio.memoria.ui.main.adapter.FingerprintAdapter
import com.weinstudio.memoria.ui.main.adapter.fingerprint.ProblemFingerprint
import com.weinstudio.memoria.ui.main.adapter.util.ItemSwipeCallback
import com.weinstudio.memoria.ui.main.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment(), EyeButtonListener {

    private val adapter by lazy {
        FingerprintAdapter(getFingerprints())
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSubtitle: TextView = view.findViewById(R.id.toolbar_subtitle)
        viewModel.countLiveDate.observe(viewLifecycleOwner, {
            val subtitle = getString(R.string.done) + " — $it"
            tvSubtitle.text = subtitle

            CoroutineScope(Dispatchers.IO).launch {
                val local = getDefaultSharedPreferences(context)
                val editor = local.edit()

                editor.putInt("last_saved_done_count", it)
                editor.apply()

            }
        })

        val recycler: RecyclerView = view.findViewById(R.id.recycler)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        val itemTouchHelperCallback = ItemSwipeCallback(onItemDelete = { pos ->
            val problem = adapter.currentList[pos] as Problem
            viewModel.removeProblem(problem)

            if (!problem.isDone) {
                val title: String = problem.title + " — "
                val snack = Snackbar.make(
                    requireActivity().findViewById(R.id.root_layout),
                    title + getString(R.string.deleted_from_yr_tasks),
                    Snackbar.LENGTH_LONG
                )

                snack.setAction(getString(R.string.undo)) {
                    viewModel.insertProblem(pos, problem)

                    // Scrolling when we insert item on top of recycler.
                    if (pos == 0) {
                        recycler.scrollToPosition(0)
                    }
                }

                snack.setActionTextColor(requireContext().getColor(R.color.secondary))
                snack.anchorView = requireActivity().findViewById(R.id.fab_create)
                snack.show()
            }
        }, onItemDone = { pos ->
            val problem = adapter.currentList[pos] as Problem

            viewModel.setProblemDoneFlag(problem, !problem.isDone)

            // Instead of notifyItemChanged because of smooth anim.
            adapter.notifyItemRemoved(pos)
            adapter.notifyItemInserted(pos)
        })

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        viewModel.problemsLiveData
            .observe(viewLifecycleOwner, { value ->
                value?.let { list ->
                    var items = list

                    if (!isEyeEnabled()) {
                        items = list.filter { !it.isDone }.toMutableList()
                    }

                    adapter.submitList(items.toList())
                }
            })
    }

    private fun isEyeEnabled(): Boolean =
        (activity as MainActivity).viewModel.isEyeEnabled.value ?: false

    override fun onEyeButtonPressed(isEnabled: Boolean) {
        if (isEnabled) {
            viewModel.updateData()

        } else {
            viewModel.filterProblems()
        }
    }

    private fun getFingerprints() = listOf(ProblemFingerprint(requireContext()))
}