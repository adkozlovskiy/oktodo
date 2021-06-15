package com.weinstudio.affari.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weinstudio.affari.R

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = BottomSheetFragment().apply {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sheet_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivClose: ImageView = view.findViewById(R.id.iv_close)
        ivClose.setOnClickListener { dismiss() }
    }
}