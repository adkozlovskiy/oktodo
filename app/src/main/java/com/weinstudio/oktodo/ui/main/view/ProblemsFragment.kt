package com.weinstudio.oktodo.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.model.Hike.Companion.completedWithError
import com.weinstudio.oktodo.data.model.Hike.Companion.loading
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.databinding.FragmentProblemsBinding
import com.weinstudio.oktodo.ui.edit.view.EditActivity
import com.weinstudio.oktodo.ui.main.adapter.FingerprintAdapter
import com.weinstudio.oktodo.ui.main.adapter.fingerprint.ProblemFingerprint
import com.weinstudio.oktodo.ui.main.adapter.util.ItemSwipeCallback
import com.weinstudio.oktodo.ui.main.viewmodel.ProblemsViewModel
import com.weinstudio.oktodo.util.hasInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ProblemsFragment : Fragment() {

    private var _binding: FragmentProblemsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProblemsViewModel by viewModels()
    private lateinit var fingerprintAdapter: FingerprintAdapter

    private val subtitlePrefixString by lazy {
        getString(R.string.done)
    }

    private val networkUnavailableString by lazy {
        getString(R.string.network_unavailable)
    }

    private val buttonOkString by lazy {
        getString(android.R.string.ok)
    }

    private val loadingErrorString by lazy {
        getString(R.string.loading_error)
    }

    companion object {
        const val TAG = "problems_fragment"
        fun newInstance() = ProblemsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.subscribeNetworkStateChanges()
        _binding = FragmentProblemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerWithAdapter()
        observeNetworkStateChanges()
        observeEyeButtonEnabled()
        observeHikeState()
        refreshProblems()

        binding.refreshButton.setOnClickListener {
            if (viewModel.isNetworkConnectionGranted()) {
                refreshProblems()
            } else showNetworkUnavailableDialog()
        }

        val itemTouchHelperCallback = getItemTouchHelperCallback()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        observeDoneProblemsCount()
        observeProblems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unsubscribeNetworkStateChanges()
        _binding = null
    }

    private fun initRecyclerWithAdapter() {
        val fingerprints = getFingerprints()
        fingerprintAdapter = FingerprintAdapter(fingerprints)

        with(binding.recycler) {
            adapter = fingerprintAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun getFingerprints() = listOf(
        ProblemFingerprint(requireContext(), ::onProblemClick)
    )

    private fun observeNetworkStateChanges() {
        viewModel.networkState.observe(viewLifecycleOwner, { state ->
            setRefreshButtonAlpha(state.hasInternet())
        })
    }

    @ExperimentalCoroutinesApi
    private fun observeEyeButtonEnabled() {
        getMainActivity().viewModel.eyeButtonEnabled
            .observe(viewLifecycleOwner) { enabled ->
                viewModel.setFilterFlag(!enabled)
            }
    }

    private fun observeDoneProblemsCount() {
        viewModel.doneCount
            .observe(viewLifecycleOwner, { value ->
                value?.let {
                    setToolbarSubtitle(it)
                }
            })
    }

    private fun observeHikeState() {
        viewModel.hikeState.observe(viewLifecycleOwner) { hike ->
            stopRefreshButtonAnim()
            if (hike.loading()) {
                startRefreshButtonAnimation()

            } else if (hike.completedWithError()) {
                showLoadingErrorSnack()
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun observeProblems() {
        viewModel.allProblems.observe(viewLifecycleOwner) { value ->
            value?.let { it ->
                fingerprintAdapter.submitList(it)
            }
        }
    }

    private fun onProblemClick(problem: Problem) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtra(Problem.PROBLEM_EXTRA_TAG, Gson().toJson(problem))
        context?.startActivity(intent)
    }

    private fun refreshProblems() {
        val hikeLoading = viewModel.hikeState.value?.loading() ?: false
        if (!hikeLoading && viewModel.isNetworkConnectionGranted()) {
            viewModel.enqueueRefreshProblems()
        }
    }

    private fun startRefreshButtonAnimation() {
        binding.refreshButton.post {
            val pivotX = binding.refreshButton.width / 2
            val pivotY = binding.refreshButton.height / 2

            val animation =
                RotateAnimation(0f, 720f, pivotX.toFloat(), pivotY.toFloat())

            animation.duration = 600L
            animation.interpolator = LinearInterpolator()
            animation.repeatCount = Animation.INFINITE
            animation.fillAfter = true
            animation.fillBefore = true

            binding.refreshButton.startAnimation(animation)
        }
    }

    private fun stopRefreshButtonAnim() {
        binding.refreshButton.clearAnimation()
    }


    private fun setRefreshButtonAlpha(enabled: Boolean) {
        binding.refreshButton.imageAlpha = if (enabled) 255 else 85
    }

    private fun showNetworkUnavailableDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(networkUnavailableString)
            .setPositiveButton(buttonOkString) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showLoadingErrorSnack() {
        val snack =
            Snackbar.make(binding.root, loadingErrorString, Snackbar.LENGTH_LONG)
        snack.anchorView = getMainActivity().binding.fabCreate
        snack.setAction(R.string.retry) {
            refreshProblems()
        }

        snack.show()
    }

    private fun getItemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return ItemSwipeCallback(
            onItemDelete = { pos ->
                val problem = fingerprintAdapter.currentList[pos] as Problem
                viewModel.deleteProblem(problem)

            },

            onItemDone = { pos ->
                val problem = fingerprintAdapter.currentList[pos] as Problem
                viewModel.changeDoneFlag(problem, problem.done.not())

                // Because of more smooth anim.
                if (getMainActivity().viewModel.eyeButtonEnabled.value == true) {
                    fingerprintAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun setToolbarSubtitle(count: Int) {
        val subtitle = "$subtitlePrefixString — $count"
        binding.toolbarSubtitle.text = subtitle
    }

    private fun getMainActivity(): MainActivity {
        return activity as MainActivity
    }
}