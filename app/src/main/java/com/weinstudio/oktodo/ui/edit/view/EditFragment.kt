package com.weinstudio.oktodo.ui.edit.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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
import com.weinstudio.oktodo.ui.edit.OkButtonListener
import com.weinstudio.oktodo.ui.edit.viewmodel.EditFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditFragment : Fragment(), OkButtonListener {

    @Inject
    lateinit var gson: Gson

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditFragViewModel by viewModels()

    private val importanceStrings by lazy {
        mutableListOf<String>().apply {
            add(getString(R.string.low_priority))
            add(getString(R.string.default_priority))
            add(getString(R.string.high_priority))
        }
    }

    private val chooseImportanceString by lazy {
        getString(R.string.choose_importance)
    }

    private val chooseDeadlineString by lazy {
        getString(R.string.choose_deadline)
    }

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())

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

        val receivedProblemJson = arguments?.getString(Problem.PROBLEM_EXTRA_TAG)
        val receivedProblem = gson.fromJson(receivedProblemJson, Problem::class.java)

        if (viewModel.problemProperties.value == null) {
            viewModel.setProblemValue(receivedProblem ?: Problem())
        }

        val problem = viewModel.getProblemValue()
        binding.etTitle.setText(problem.text)
        viewModel.initDeadlineCalendar(problem.deadline)

        registerErrorStatusChangedListener()

        binding.cvDeadline.setOnClickListener {
            chooseDeadline()
        }

        binding.cvPriority.setOnClickListener {
            chooseImportance()
        }

        binding.switchDeadline.setOnCheckedChangeListener { _, checked ->
            animateDeadlineField(checked)

            if (!checked) {
                viewModel.setProblemDeadline(null)
            }
        }

        observePropertiesChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observePropertiesChanges() {
        viewModel.problemProperties.observe(viewLifecycleOwner) { problem ->
            val hasDeadline = problem.hasDeadline()
            checkDeadlineSwitch(hasDeadline)
            setDeadlineText(problem.deadline)

            setImportanceText(problem.importance.value)
        }
    }

    private fun registerErrorStatusChangedListener() {
        binding.etTitle.addTextChangedListener(
            afterTextChanged = {
                if (binding.tiTitle.isErrorEnabled && binding.etTitle.text.isNotBlank()) {
                    binding.tiTitle.error = null
                }
            })
    }

    private fun setDeadlineText(deadlineMillis: Long?) {
        if (deadlineMillis == null) {
            binding.tvDeadlineProp.text = chooseDeadlineString

        } else {
            val deadlineDate = Date(deadlineMillis * 1000)
            binding.tvDeadlineProp.text = dateFormat.format(deadlineDate)
        }
    }

    private fun setImportanceText(importanceValue: Int) {
        binding.tvPriorityProp.text = importanceStrings[importanceValue]
    }

    private fun checkDeadlineSwitch(checked: Boolean) {
        binding.switchDeadline.isChecked = checked
    }

    private fun animateDeadlineField(visible: Boolean) {
        if (visible) {
            binding.cvDeadline.visibility = View.VISIBLE
            binding.cvDeadline.alpha = 0f
            binding.cvDeadline.animate().alpha(1f)

        } else {
            binding.cvDeadline.visibility = View.GONE
        }
    }

    private fun chooseDeadline() {
        showDatePickerDialog(onSelected = {
            showTimePickerDialog(onSelected = { millis ->
                viewModel.setProblemDeadline(millis)
            })
        })
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

    private fun showTimePickerDialog(onSelected: (millis: Long) -> Unit) {
        val calendar = viewModel.deadlineCalendar
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                with(calendar) {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }

                onSelected.invoke(calendar.timeInMillis)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        )
            .show()
    }

    private fun chooseImportance() {
        val items: Array<String> = importanceStrings.toTypedArray()

        var selectedItemOrdinal = viewModel.getProblemValue().importance.value

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(chooseImportanceString)
            .setSingleChoiceItems(items, selectedItemOrdinal) { _, id ->
                selectedItemOrdinal = id
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.setProblemImportance(selectedItemOrdinal)
            }
            .show()
    }

    override fun onButtonPressed() {
        if (binding.etTitle.text.toString().isBlank()) {
            binding.tiTitle.error = getString(R.string.invalid_title)
            return
        }

        val problem = viewModel.getProblemValue().copy(
            text = binding.etTitle.text.toString()
        )

        if (problem.hasCreated()) {
            viewModel.updateProblem(problem)

        } else viewModel.insertProblem(problem)

        activity?.finish()
    }
}