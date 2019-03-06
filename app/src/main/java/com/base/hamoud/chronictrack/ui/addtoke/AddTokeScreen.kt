package com.base.hamoud.chronictrack.ui.addtoke

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Toke
import java.time.OffsetDateTime

class AddTokeScreen : Fragment() {

    private lateinit var viewModel: AddTokeViewModel

    private var saveButton: Button? = null
    private var strainEditText: EditText? = null
    private var timeInputTextView: TextView? = null
    private var dateInputTextView: TextView? = null
    private var typeSpinner: Spinner? = null
    private var methodSpinner: Spinner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_add_toke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AddTokeViewModel::class.java)

        // prepare ui
        prepareFormView(view)

        // observe
        observeDateTimeLiveData(view)
        observeOnLastAddedTokeLive()

        // trigger
        viewModel.getLastAddedToke()
    }

    private fun observeDateTimeLiveData(view: View) {
        viewModel.dateTimeLiveData.observe(this, Observer<OffsetDateTime> {
            dateInputTextView = view.findViewById(R.id.add_toke_date_field)
            dateInputTextView?.text = viewModel.dateCreator()
            timeInputTextView = view.findViewById(R.id.add_toke_time_field)
            timeInputTextView?.text = viewModel.timeCreator()
        })
    }

    private fun observeOnLastAddedTokeLive() {
        viewModel.lastAddedTokeLive.observe(this, Observer { toke ->
            toke?.let {
                strainEditText?.setText(it.strain)
                when(it.tokeType) {
                    getString(R.string.indica) -> {
                    // todo auto fill spinner and remove drop down
//                        methodSpinner?. = 1
                    }
                }
            }
        })
    }

    private fun prepareFormView(view: View) {
        prepareDateField()
        prepareTimeField()
        prepareTypeSpinner(view)
        prepareMethodSpinner(view)
        prepareStrainField()
        prepareSaveBtn()
    }

    private fun prepareDateField() {
        dateInputTextView = view?.findViewById(R.id.add_toke_date_field)
        dateInputTextView?.text = viewModel.dateCreator()

        dateInputTextView?.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                  activity!!.themedContext)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                viewModel.updateDate(year = year, month = month, dayOfMonth = dayOfMonth)
            }
            datePickerDialog.updateDate(viewModel.now.year, viewModel.now.monthValue, viewModel.now.dayOfMonth)
            datePickerDialog.show()
        }
    }

    private fun prepareTimeField() {
        timeInputTextView = view?.findViewById(R.id.add_toke_time_field)
        timeInputTextView?.text = viewModel.timeCreator()

        timeInputTextView?.setOnClickListener {
            val timePickerDialog = TimePickerDialog(activity!!.themedContext,
                  TimePickerDialog.OnTimeSetListener(function = { view, hourOfDay, minute ->
                      viewModel.updateTime(hour = hourOfDay, minute = minute)
                  }), viewModel.now.hour, viewModel.now.minute,
                  android.text.format.DateFormat.is24HourFormat(activity))

            timePickerDialog.show()
        }
    }

    private fun prepareStrainField() {
        strainEditText = view?.findViewById(R.id.add_toke_strain_name_field)
    }

    private fun prepareSaveBtn() {
        // prepare save button
        saveButton = view?.findViewById(R.id.add_toke_save_button)
        saveButton?.setOnClickListener {
            viewModel.strainSelection = strainEditText?.text.toString()
            val hit = Toke(
                  tokeType = viewModel.typeSelection,
                  strain = viewModel.strainSelection,
                  tokeDateTime = viewModel.now,
                  toolUsed = viewModel.methodSelection
            )
            viewModel.insertToke(hit)
            findNavController().navigate(R.id.toke_log_screen)
        }
    }

    private fun prepareMethodSpinner(view: View) {
        methodSpinner= view.findViewById(R.id.add_toke_tool_used_dropdown)
        ArrayAdapter.createFromResource(
              context!!,
              R.array.ingestion_method,
              android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            methodSpinner?.adapter = it
        }
        methodSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.methodSelection = parent?.getItemAtPosition(0).toString()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                viewModel.methodSelection = parent?.getItemAtPosition(pos).toString()
            }
        }
    }

    private fun prepareTypeSpinner(view: View) {
        typeSpinner = view.findViewById(R.id.add_toke_type_dropdown)
        ArrayAdapter.createFromResource(
              context!!,
              R.array.chronic_type,
              android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinner?.adapter = it
        }
        typeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.typeSelection = parent?.getItemAtPosition(0).toString()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                viewModel.typeSelection = parent?.getItemAtPosition(pos).toString()
            }
        }
    }
}