package com.base.hamoud.chronictrack.ui.addtoke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.ui.main.MainActivity
import com.base.hamoud.chronictrack.ui.tokelog.TokeLogScreen
import java.util.*

class AddTokeScreen : Fragment() {

    private lateinit var viewModel: AddTokeViewModel

    lateinit var date: String
    lateinit var time: String
    lateinit var typeSelection: String
    lateinit var methodSelection: String
    lateinit var strainSelection: String

    private var saveButton: Button? = null
    private var strainEditText: EditText? = null
    private var timeInputTextView: TextView? = null
    private var dateInputTextView: TextView? = null

    companion object {
        fun newInstance(): AddTokeScreen = AddTokeScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_add_toke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddTokeViewModel::class.java)

        // prepare ui
        prepareFormView(view)
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
        val now: Calendar = Calendar.getInstance()
        dateInputTextView = view?.findViewById(R.id.add_toke_date_field)
        date = "${now.get(Calendar.DATE)} / ${now.get(Calendar.MONTH)} / ${now.get(Calendar.YEAR)}"
        dateInputTextView?.text = date
    }

    private fun prepareTimeField() {
        val now: Calendar = Calendar.getInstance()
        timeInputTextView = view?.findViewById(R.id.add_toke_time_field)
        val hours: String = now.get(Calendar.HOUR_OF_DAY).toString()
        val minuteCalendar = now.get(Calendar.MINUTE)
        val minutes: String = if (minuteCalendar < 10) {
            "0$minuteCalendar"
        } else {
            minuteCalendar.toString()
        }
        val secondsCalendar = now.get(Calendar.SECOND)
        val seconds: String = if (secondsCalendar < 10) {
            "0$secondsCalendar"
        } else {
            secondsCalendar.toString()
        }
        time = "$hours:$minutes:$seconds"
        timeInputTextView?.text = time
    }

    private fun prepareStrainField() {
        strainEditText = view?.findViewById(R.id.add_toke_strain_name_field)
    }

    private fun prepareSaveBtn() {
        // prepare save button
        saveButton = view?.findViewById(R.id.add_toke_save_button)
        saveButton?.setOnClickListener {
            strainSelection = strainEditText?.text.toString()
            val hit = Hit(
                  userId = "Chron",
                  hitTime = time,
                  hitDate = date,
                  hitType = typeSelection,
                  strain = strainSelection,
                  toolUsed = methodSelection
            )
            viewModel.insertHit(hit)
            (activity as MainActivity).goToScreen(
                  screen = TokeLogScreen.newInstance(),
                  shouldAddToBackStack = false)
        }
    }

    private fun prepareMethodSpinner(view: View) {
        val methodSpinner: Spinner = view.findViewById(R.id.add_toke_tool_used_dropdown)
        ArrayAdapter.createFromResource(
              context!!,
              R.array.ingestion_method,
              android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            methodSpinner.adapter = it
        }
        methodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                methodSelection = parent?.getItemAtPosition(0).toString()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                methodSelection = parent?.getItemAtPosition(pos).toString()
            }
        }
    }

    private fun prepareTypeSpinner(view: View) {
        val typeSpinner: Spinner = view.findViewById(R.id.add_toke_type_dropdown)
        ArrayAdapter.createFromResource(
              context!!,
              R.array.chronic_type,
              android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinner.adapter = it
        }
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                typeSelection = parent?.getItemAtPosition(0).toString()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                typeSelection = parent?.getItemAtPosition(pos).toString()
            }
        }
    }
}