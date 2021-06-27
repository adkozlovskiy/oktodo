package com.weinstudio.memoria.ui.main.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.weinstudio.memoria.R
import com.weinstudio.memoria.ui.main.EyeButtonCallback
import com.weinstudio.memoria.ui.main.MainActivity
import com.weinstudio.memoria.ui.main.adapter.ProblemsAdapter
import com.weinstudio.memoria.ui.main.viewmodel.ProblemsViewModel

class ProblemsFragment : Fragment(), EyeButtonCallback {

    private val adapter by lazy {
        ProblemsAdapter(requireContext())
    }

    companion object {
        fun newInstance() = ProblemsFragment()
    }

    private val viewModel: ProblemsViewModel by lazy {
        ViewModelProvider(this).get(ProblemsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_problems, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDataFromModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler: RecyclerView = view.findViewById(R.id.recycler)

        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_swipe_delete)!!
        val doneIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done_28dp)!!
        val intrinsicWidth = deleteIcon.intrinsicWidth
        val intrinsicHeight = deleteIcon.intrinsicHeight
        val background = ColorDrawable()

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition

                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            val problem = adapter.actualProblems[position]
                            viewModel.removeProblem(problem)
                            val title: String = problem.title

                            if (!problem.isDone) {

                                val snack = Snackbar.make(
                                    requireActivity().findViewById(R.id.root_layout),
                                    title + " — " + getString(R.string.deleted_from_yr_tasks),
                                    Snackbar.LENGTH_LONG
                                )
                                snack.setAction(getString(R.string.undo)) {
                                    viewModel.insertProblem(position, problem)
                                    adapter.notifyItemRemoved(position)
                                    adapter.notifyItemInserted(position)
                                    if (position == 0) {
                                        recycler.scrollToPosition(0)
                                    }
                                }
                                snack.setActionTextColor(requireContext().getColor(R.color.secondary))
                                snack.anchorView = requireActivity().findViewById(R.id.fab_create)
                                snack.show()
                            }
                        }

                        ItemTouchHelper.RIGHT -> {
                            val problem = adapter.actualProblems[position]

                            viewModel.setProblemDoneFlag(problem, !problem.isDone)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemInserted(position)
                        }
                    }
                }

                override fun onChildDraw(
                    c: Canvas, rv: RecyclerView, holder: RecyclerView.ViewHolder,
                    dX: Float, dY: Float, aState: Int, isActive: Boolean
                ) {

                    val itemView = holder.itemView
                    val itemHeight = itemView.bottom - itemView.top

                    val iconMargin = (itemHeight - intrinsicHeight) / 2
                    val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val iconBottom = iconTop + intrinsicHeight
                    val (iconLeft, iconRight) = getIconPosHorizontal(
                        itemView,
                        iconMargin,
                        dX,
                        intrinsicWidth
                    )

                    if (dX > 0) {
                        background.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.left + dX.toInt(),
                            itemView.bottom
                        )
                        background.color = Color.parseColor("#559858")
                        background.draw(c)

                        doneIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        doneIcon.draw(c)

                    } else if (dX < 0) {
                        background.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        background.color = Color.parseColor("#C54540")
                        background.draw(c)

                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        deleteIcon.draw(c)
                    }

                    super.onChildDraw(c, rv, holder, dX, dY, aState, isActive)
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        viewModel.problemsLiveData
            .observe(viewLifecycleOwner, {
                it?.let {
                    val isEyeEnabled =
                        (activity as MainActivity).viewModel.isEyeEnabled.value ?: false

                    var items = it

                    if (!isEyeEnabled) {
                        items = items.filter {
                            !it.isDone
                        }.toMutableList()
                    }

                    adapter.setItems(items)
                }
            })
    }

    private fun getIconPosHorizontal(iv: View, im: Int, dX: Float, iw: Int): Pair<Int, Int> {
        val iconLeft: Int
        val iconRight: Int

        if (dX > 0) {
            iconLeft = iv.left + im
            iconRight = iv.left + im + iw
        } else {
            iconLeft = iv.right - im - iw
            iconRight = iv.right - im
        }

        return Pair(iconLeft, iconRight)
    }

    override fun onEyeButtonPressed(isEnabled: Boolean) {
        if (isEnabled) {
            viewModel.getDataFromModel()

        } else {
            viewModel.filterProblems()
        }
    }
}