package com.weinstudio.memoria.ui.create.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.weinstudio.memoria.R
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.util.enums.Priority
import com.weinstudio.memoria.data.repository.ProblemsRepository
import com.weinstudio.memoria.ui.create.FragmentController
import com.weinstudio.memoria.ui.create.viewmodel.CreateViewModel
import java.util.*

class CreateFragment : Fragment(), FragmentController {

    private lateinit var cvDeadline: CardView
    private lateinit var tvDeadline: TextView
    private lateinit var switchDeadline: SwitchCompat

    private lateinit var cvPriority: CardView
    private lateinit var tvPriority: TextView

    private lateinit var tiTitle: TextInputLayout
    private lateinit var etTitle: EditText

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
        cvDeadline = view.findViewById(R.id.cv_deadline)
        tvDeadline = view.findViewById(R.id.tv_deadline_prop)

        cvDeadline.setOnClickListener { chooseDateTime() }
        viewModel.datetimeData.observe(viewLifecycleOwner, {
            tvDeadline.text = it
        })

        cvPriority = view.findViewById(R.id.cv_priority)
        tvPriority = view.findViewById(R.id.tv_priority_prop)

        cvPriority.setOnClickListener { choosePriority() }
        viewModel.priority.observe(viewLifecycleOwner, {
            tvPriority.text = when (it) {
                Priority.HIGH_PRIORITY -> getString(R.string.high_priority)
                Priority.LOW_PRIORITY -> getString(R.string.low_priority)
                null -> getString(R.string.no_priority)
            }
        })

        switchDeadline = view.findViewById(R.id.switch_deadline)

        switchDeadline.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cvDeadline.visibility = View.VISIBLE
                cvDeadline.alpha = 0f
                cvDeadline.animate().alpha(1f)

            } else {
                cvDeadline.visibility = View.GONE
            }
        }

        tiTitle = view.findViewById(R.id.ti_title)
        etTitle = view.findViewById(R.id.et_title)

        etTitle.addTextChangedListener {
            if (tiTitle.isErrorEnabled && etTitle.text.isNotBlank()) {
                tiTitle.error = null
            }
        }
    }

    private fun chooseDateTime() {
        showDatePickerDialog { // onSelected
            showTimePickerDialog { // onSelected
                viewModel.datetimeData.value = DateUtils.formatDateTime(
                    context,
                    viewModel.calendar.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
                            or DateUtils.FORMAT_SHOW_TIME
                )
            }
        }
    }

    private fun showDatePickerDialog(onSelected: () -> Unit) {
        val calendar = viewModel.calendar
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
        val calendar = viewModel.calendar
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

    private fun choosePriority() {
        val items = arrayOf(
            getString(R.string.no_priority),
            getString(R.string.low_priority),
            getString(R.string.high_priority)
        )

        var checked = when (viewModel.priority.value) {
            null -> 0
            Priority.LOW_PRIORITY -> 1
            Priority.HIGH_PRIORITY -> 2
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.choose_priority))
            .setSingleChoiceItems(items, checked) { _, id ->
                checked = id
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.priority.value = when (checked) {
                    1 -> Priority.LOW_PRIORITY
                    2 -> Priority.HIGH_PRIORITY
                    else -> null
                }
            }
            .show()
    }

    private var i = 6

    override fun onCreateButtonPressed() {
        if (etTitle.text.toString().isBlank()) {
            tiTitle.error = getString(R.string.invalid_title)
            return
        }

        val problem = Problem(
            id = i,
            title = etTitle.text.toString(),
            priority = viewModel.priority.value,
            isDone = false,
            notifyDate = null,
            deadline = if (switchDeadline.isChecked && viewModel.datetimeData.value != null) {
                viewModel.calendar.timeInMillis
            } else null
        )

        ProblemsRepository.insertProblem(0, problem)
        i++
        activity?.finish()
    }
}