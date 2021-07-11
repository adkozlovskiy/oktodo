package com.weinstudio.oktodo.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.weinstudio.oktodo.MemoriaApp
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.entity.Problem
import com.weinstudio.oktodo.databinding.FragmentProblemsBinding
import com.weinstudio.oktodo.ui.edit.EditActivity
import com.weinstudio.oktodo.ui.main.EyeButtonListener
import com.weinstudio.oktodo.ui.main.MainActivity
import com.weinstudio.oktodo.ui.main.adapter.FingerprintAdapter
import com.weinstudio.oktodo.ui.main.adapter.fingerprint.ProblemFingerprint
import com.weinstudio.oktodo.ui.main.adapter.util.ItemSwipeCallback
import com.weinstudio.oktodo.ui.main.viewmodel.ProblemsViewModel
import com.weinstudio.oktodo.ui.main.viewmodel.ProblemsViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ProblemsFragment : Fragment(), EyeButtonListener {

    private var _binding: FragmentProblemsBinding? = null

    private val binding get() = _binding!!

    private val fingerprintAdapter by lazy {
        FingerprintAdapter(getFingerprints())
    }

    private val viewModel: ProblemsViewModel by viewModels {
        ProblemsViewModelFactory((context?.applicationContext as MemoriaApp).repository)
    }

    private val subtitleTemplate by lazy {
        context?.getString(R.string.done)
    }

    companion object {
        fun newInstance() = ProblemsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProblemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshButton.setOnClickListener {
            viewModel.refreshProblems()
        }

        with(binding.recycler) {
            adapter = fingerprintAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val itemTouchHelperCallback = ItemSwipeCallback(
            onItemDelete = { pos ->
                val problem = fingerprintAdapter.currentList[pos] as Problem
                viewModel.deleteProblem(problem)

            },

            onItemDone = { pos ->
                val problem = fingerprintAdapter.currentList[pos] as Problem
                viewModel.changeDoneFlag(problem, problem.done.not())

                // Because of more smooth anim.
                if (isEyeEnabled()) {
                    fingerprintAdapter.notifyItemRemoved(pos)
                    fingerprintAdapter.notifyItemInserted(pos)
                }
            })

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        viewModel.doneCount
            .observe(viewLifecycleOwner, { value ->
                value?.let {
                    setToolbarSubtitle(it)
                }
            })

        viewModel.allProblems
            .observe(viewLifecycleOwner, { value ->
                value?.let { it ->
                    fingerprintAdapter.submitList(it)
                }
            })
    }

    private fun onProblemClick(problem: Problem) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtra(Problem.PROBLEM_EXTRA_TAG, Gson().toJson(problem))
        context?.startActivity(intent)
    }

    private fun isEyeEnabled(): Boolean =
        (activity as MainActivity).viewModel.isEyeEnabled.value ?: false

    private fun setToolbarSubtitle(count: Int) {
        val subtitle = "$subtitleTemplate — $count"
        binding.toolbarSubtitle.text = subtitle
    }

    @ExperimentalCoroutinesApi
    override fun onEyeButtonPressed(enabled: Boolean) {
        viewModel.setFilterFlag(enabled.not())
    }

    private fun getFingerprints() = listOf(ProblemFingerprint(requireContext(), ::onProblemClick))

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}