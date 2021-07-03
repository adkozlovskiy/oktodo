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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.weinstudio.memoria.App
import com.weinstudio.memoria.R
import com.weinstudio.memoria.data.entity.Problem
import com.weinstudio.memoria.data.entity.enums.Priority
import com.weinstudio.memoria.ui.create.CreateButtonListener
import com.weinstudio.memoria.ui.create.viewmodel.CreateViewModel
import kotlinx.coroutines.launch
import java.util.*

class CreateFragment : Fragment(), CreateButtonListener {

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

        val cvDeadline = view.findViewById<CardView>(R.id.cv_deadline)
        val tvDeadline = view.findViewById<TextView>(R.id.tv_deadline_prop)

        cvDeadline.setOnClickListener { chooseDateTime() }
        viewModel.deadlineText.observe(viewLifecycleOwner, {
            tvDeadline.text = it
        })

        cvPriority = view.findViewById(R.id.cv_priority)
        tvPriority = view.findViewById(R.id.tv_priority_prop)

        cvPriority.setOnClickListener { choosePriority() }
        viewModel.priorityProp.observe(viewLifecycleOwner, {
            tvPriority.text = when (it) {
                Priority.HIGH -> getString(R.string.high_priority)
                Priority.DEFAULT -> getString(R.string.default_priority)
                Priority.LOW -> getString(R.string.low_priority)
                else -> throw java.lang.IllegalStateException()
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
                viewModel.deadlineText.value = DateUtils.formatDateTime(
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
            getString(R.string.low_priority),
            getString(R.string.default_priority),
            getString(R.string.high_priority)
        )

        var checked = when (viewModel.priorityProp.value) {
            Priority.LOW -> 0
            Priority.DEFAULT -> 1
            Priority.HIGH -> 2
            else -> throw IllegalStateException()
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.choose_priority))
            .setSingleChoiceItems(items, checked) { _, id ->
                checked = id
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.priorityProp.value = when (checked) {
                    0 -> Priority.LOW
                    1 -> Priority.DEFAULT
                    else -> Priority.HIGH
                }
            }
            .show()
    }

    override fun onButtonPressed() {
        if (etTitle.text.toString().isBlank()) {
            tiTitle.error = getString(R.string.invalid_title)
            return
        }

        val problem = Problem(
            id = null,
            text = etTitle.text.toString(),
            priority = viewModel.priorityProp.value ?: Priority.DEFAULT,
            done = false,
            created = Date().time,
            updated = Date().time,
            deadline = if (switchDeadline.isChecked && viewModel.deadlineText.value != null) {
                viewModel.calendar.timeInMillis
            } else null
        )

        lifecycleScope.launch {
            (context?.applicationContext as App).repository.insertProblem(problem)
        }

        activity?.finish()
    }
}