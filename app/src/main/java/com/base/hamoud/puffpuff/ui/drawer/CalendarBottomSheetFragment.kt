package com.base.hamoud.puffpuff.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.lifecycle.Observer
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.ui.journal.JournalViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.joda.time.DateTime


class CalendarBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {

        fun getInstance(): CalendarBottomSheetFragment {
            return CalendarBottomSheetFragment()
        }

    }

    private lateinit var viewModel: JournalViewModel

    private var calendarView: CalendarView? = null
    private var calendarOkBtn: Button? = null

    var calendarDateTime: DateTime = DateTime.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // prepare ui
        prepareView()

        // observe
        observeOnCalendarMinDateTime()

        // trigger
        viewModel.getFirstEverSavedToke()
    }

    fun setViewModel(journalViewModel: JournalViewModel) {
        viewModel = journalViewModel
        calendarDateTime = viewModel.journalDate
    }

    private fun observeOnCalendarMinDateTime() {
        viewModel.calendarMinDateTimeLive.observe(viewLifecycleOwner, Observer { minimumDateTime ->
            minimumDateTime?.let {
                // set the minimum selectable date for the calendar
                calendarView?.minDate = it
            }
        })
    }

    private fun prepareView() {
        prepareCalendarView()
        prepareOkBtn()
    }

    private fun prepareCalendarView() {
        calendarView = view?.findViewById(R.id.bottom_sheet_calendar_view)
        calendarView?.maxDate = DateTime().millis // set max selectable date, which is today
        calendarView?.date = calendarDateTime.millis // set the current journal date
        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendarDateTime = DateTime().withDate(year, month + 1, dayOfMonth)
        }
    }

    private fun prepareOkBtn() {
        calendarOkBtn = view?.findViewById(R.id.bottom_sheet_calendar_view_ok_btn)
        calendarOkBtn?.setOnClickListener {
            // update journalDate and dismiss the bottom sheet
            viewModel.updateJournalDate(calendarDateTime)
            dialog?.dismiss()
        }
    }


}