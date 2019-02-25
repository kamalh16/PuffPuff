package com.base.hamoud.chronictrack.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class HitFormBottomDrawerFragment : BottomSheetDialogFragment() {

    lateinit var date: String
    lateinit var time: String
    lateinit var typeSelection: String
    lateinit var methodSelection: String
    lateinit var strainSelection: String

    lateinit var saveButton: Button
    lateinit var strainEditText: EditText
    lateinit var timeInputTextView: TextView
    lateinit var dateInputTextView: TextView

    var saveHit: MutableLiveData<Hit> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hit_form_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFormView(view)
    }

    fun prepareFormView(view: View) {
        // prepare date and time
        // todays date and time always set
        val now: Calendar = Calendar.getInstance()
        dateInputTextView = view.findViewById(R.id.date_textview)
        date = "${now.get(Calendar.DATE)} / ${now.get(Calendar.MONTH)} / ${now.get(Calendar.YEAR)}"
        dateInputTextView.text = date

        timeInputTextView = view.findViewById(R.id.time_textview)
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
        timeInputTextView.text = time

        // prepare type spinner
        // indica/hybrid/sativas
        prepareTypeSpinner(view)
        // prepare method spinner
        prepareMethodSpinner(view)
        // prepare strain edittext
        strainEditText = view.findViewById(R.id.strain_text)
        // prepare save button
        saveButton = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            strainSelection = strainEditText.text.toString()
            val hit = Hit(
                userId = "Chron",
                hitTime = "$time",
                hitDate = "$date",
                hitType = typeSelection,
                strain = strainSelection,
                toolUsed = methodSelection
            )
            saveHit.postValue(hit)
        }
    }

    private fun prepareMethodSpinner(view: View) {
        val methodSpinner: Spinner = view.findViewById(R.id.ingestion_method_spinner)
        ArrayAdapter.createFromResource(
            activity?.applicationContext,
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
        val typeSpinner: Spinner = view.findViewById(R.id.type_spinner)
        ArrayAdapter.createFromResource(
            activity?.applicationContext,
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