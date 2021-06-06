package com.weinstudio.affari.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weinstudio.affari.R

class CreateBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = CreateBottomSheetFragment().apply {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivClose: ImageView = view.findViewById(R.id.iv_close)
        ivClose.setOnClickListener { dismiss() }

        val btnCreateTask: TextView = view.findViewById(R.id.tv_create_a_task)
        btnCreateTask.setOnClickListener { }

        val btnCreateNote: TextView = view.findViewById(R.id.tv_create_a_note)
        btnCreateNote.setOnClickListener { }
    }
}