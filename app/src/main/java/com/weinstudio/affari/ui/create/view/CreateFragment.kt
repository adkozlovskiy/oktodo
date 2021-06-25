package com.weinstudio.affari.ui.create.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.create.viewmodel.CreateViewModel
import java.util.*

class CreateFragment : Fragment() {

    private lateinit var cvDeadline: CardView
    private lateinit var tvDeadline: TextView

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
        val dialog = DatePickerDialog(
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
        dialog.show()
    }

    private fun showTimePickerDialog(onSelected: () -> Unit) {
        val calendar = viewModel.calendar
        val dialog = TimePickerDialog(
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
        dialog.show()
    }
}