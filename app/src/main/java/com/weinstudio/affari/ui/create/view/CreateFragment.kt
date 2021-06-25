package com.weinstudio.affari.ui.create.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.weinstudio.affari.R
import com.weinstudio.affari.data._enum.Priority
import com.weinstudio.affari.ui.create.FragmentController
import com.weinstudio.affari.ui.create.viewmodel.CreateViewModel
import java.util.*

class CreateFragment : Fragment(), FragmentController {

    private lateinit var cvDeadline: CardView
    private lateinit var tvDeadline: TextView
    private lateinit var switchDeadline: SwitchMaterial

    private lateinit var cvPriority: CardView
    private lateinit var tvPriority: TextView

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
        DatePickerDialog(
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
            .show()
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

    override fun onCreateButtonPressed() {
        Log.d("FFFF", "onCreateButtonPressed: +++")
    }
}