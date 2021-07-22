package com.weinstudio.oktodo.ui.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.model.Problem
import com.weinstudio.oktodo.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EditFragment : Fragment(), OkButtonListener {

    private val viewModel: EditViewModel by viewModels()

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val importanceResourceConstants by lazy {
        ArrayList<String>().apply {
            add(getString(R.string.low_priority))
            add(getString(R.string.default_priority))
            add(getString(R.string.high_priority))
        }
    }

    @Inject
    lateinit var gson: Gson

    companion object {
        @JvmStatic
        fun newInstance(problem: String?) = EditFragment().apply {
            arguments = Bundle().apply {
                putString(Problem.PROBLEM_EXTRA_TAG, problem)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val problemExtra = arguments?.getString(Problem.PROBLEM_EXTRA_TAG)

        if (!problemExtra.isNullOrEmpty()) {
            val problem = gson.fromJson(problemExtra, Problem::class.java)

            // Setting text by default
            binding.etTitle.setText(problem.text)

            viewModel.problemData.value = problem
        }

        observeProblemChanges()

        binding.cvDeadline.setOnClickListener { chooseDateTime() }
        binding.cvPriority.setOnClickListener { chooseImportance() }

        observeSwitchChecking()

        binding.switchDeadline.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDeadlineSwitchChecked(isChecked)
        }

        binding.etTitle.addTextChangedListener(afterTextChanged = {
            if (binding.tiTitle.isErrorEnabled && binding.etTitle.text.isNotBlank()) {
                binding.tiTitle.error = null
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeProblemChanges() {
        viewModel.problemData.observe(viewLifecycleOwner) { problem ->

            // Observing deadline
            val deadline = problem.deadline

            if (deadline != null) {
                binding.tvDeadlineProp.text = DateUtils.formatDateTime(
                    context,
                    deadline * 1000,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
                            or DateUtils.FORMAT_SHOW_TIME
                )
            }

            // Observing importance
            binding.tvPriorityProp.text = importanceResourceConstants[problem.importance.value]
        }
    }

    private fun observeSwitchChecking() {
        viewModel.deadlineSwitchChecked.observe(viewLifecycleOwner) { isChecked ->
            binding.switchDeadline.isChecked = isChecked

            if (isChecked) {
                binding.cvDeadline.visibility = View.VISIBLE
                binding.cvDeadline.alpha = 0f
                binding.cvDeadline.animate().alpha(1f)

            } else {
                binding.cvDeadline.visibility = View.GONE
            }
        }
    }

    private fun chooseDateTime() {
        showDatePickerDialog { // onSelected()
            showTimePickerDialog { // onSelected()
                viewModel.updateDeadlineProp()
            }
        }
    }

    private fun showDatePickerDialog(onSelected: () -> Unit) {
        val calendar = viewModel.deadlineCalendar
        val picker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                with(calendar) {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                onSelected.invoke()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.datePicker.minDate = Date().time
        picker.show()
    }

    private fun showTimePickerDialog(onSelected: () -> Unit) {
        val calendar = viewModel.deadlineCalendar
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                with(calendar) {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }

                onSelected.invoke()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        )
            .show()
    }

    private fun chooseImportance() {
        val items: Array<String> = importanceResourceConstants.toTypedArray()

        var selectedItemOrdinal = viewModel.problemData.value?.importance!!.value

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.choose_priority))
            .setSingleChoiceItems(items, selectedItemOrdinal) { _, id ->
                selectedItemOrdinal = id
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.setImportanceProp(selectedItemOrdinal)
            }
            .show()
    }

    override fun onButtonPressed() {
        if (binding.etTitle.text.toString().isBlank()) {
            binding.tiTitle.error = getString(R.string.invalid_title)
            return
        }

        val problem = viewModel.problemData.value!!.copy()

        if (!binding.switchDeadline.isChecked) {
            problem.deadline = null
        }

        problem.text = binding.etTitle.text.toString()

        if (problem.created == -1L) {
            viewModel.insertProblem(problem)

        } else {
            viewModel.updateProblem(problem)
        }

        activity?.finish()
    }
}