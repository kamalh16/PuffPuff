package com.base.hamoud.chronictrack.ui.edittoke

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.base.hamoud.chronictrack.R
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.model.ChronicTypes
import com.base.hamoud.chronictrack.data.model.Tools
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.joda.time.DateTime

class EditTokeScreen : Fragment() {

    companion object {

        private const val ARG_TOKE_ID = "tokeId"

        fun bundleArgs(tokeId: String): Bundle {
            return Bundle().apply {
                this.putString(ARG_TOKE_ID, tokeId)
            }
        }
    }

    private var tokeId: String? = null
    private lateinit var viewModel: EditTokeViewModel

    private var screenTitleLabel: TextView? = null
    private var saveButton: FloatingActionButton? = null
    private var deleteButton: FloatingActionButton? = null
    private var strainEditText: EditText? = null
    private var timeInputTextView: TextView? = null
    private var dateInputTextView: TextView? = null
    private var tokeTypeChipGroup: ChipGroup? = null
    private var tokeToolChipGroup: ChipGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // retrieve tokeId from bundle args
        tokeId = arguments?.getString(ARG_TOKE_ID)

        return inflater.inflate(R.layout.screen_add_toke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EditTokeViewModel::class.java)

        // prepare ui
        prepareView()

        // observe
        observeDateTimeLiveData()
        observeOnLastAddedTokeLive()

        // trigger
        tokeId?.let {
            viewModel.getTokeForEdit(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanupReferences()
    }

    private fun observeDateTimeLiveData() {
        viewModel.dateTimeLiveData.observe(this, Observer<DateTime> {
            dateInputTextView?.text = viewModel.getFormattedTokeDate()
            timeInputTextView?.text = viewModel.getFormattedTokeTime()
        })
    }

    private fun observeOnLastAddedTokeLive() {
        viewModel.tokeForEditLive.observe(this, Observer { toke ->
            toke?.let {
                strainEditText?.setText(it.strain)
                setTypeChipGroupSelection(it.tokeType)
                setToolChipGroupSelection(it.toolUsed)
            }
        })
    }

    private fun prepareView() {
        prepareScreenTitleLabel()
        prepareDateField()
        prepareTimeField()
        prepareTypeChipGroup()
        prepareToolChipGroup()
        prepareStrainField()
        prepareSaveBtn()
        prepareDeleteBtn()
    }

    private fun cleanupReferences() {
        saveButton = null
        deleteButton = null
        strainEditText = null
        timeInputTextView = null
        dateInputTextView = null
        tokeTypeChipGroup = null
        tokeToolChipGroup = null
    }

    private fun prepareScreenTitleLabel() {
        screenTitleLabel = view?.findViewById(R.id.add_toke_screen_label)
        screenTitleLabel?.setText(R.string.label_edit_toke)
    }

    private fun prepareDateField() {
        dateInputTextView = view?.findViewById(R.id.add_toke_date_field)
        dateInputTextView?.text = viewModel.getFormattedTokeDate()

        dateInputTextView?.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    viewModel.updateDate(
                        year = year,
                        month = month + 1,
                        dayOfMonth = dayOfMonth
                    )

                },
                viewModel.tokeDateTime.year,
                (viewModel.tokeDateTime.monthOfYear - 1),
                viewModel.tokeDateTime.dayOfMonth
            )
            datePickerDialog.show()
        }
    }

    private fun prepareTimeField() {
        timeInputTextView = view?.findViewById(R.id.add_toke_time_field)
        timeInputTextView?.text = viewModel.getFormattedTokeTime()

        timeInputTextView?.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                activity,
                TimePickerDialog.OnTimeSetListener(function = { view, hourOfDay, minute ->
                    viewModel.updateTime(
                        hour = hourOfDay,
                        minute = minute
                    )
                }),
                viewModel.tokeDateTime.hourOfDay,
                viewModel.tokeDateTime.minuteOfHour,
                android.text.format.DateFormat.is24HourFormat(activity)
            )
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
            tokeId?.let {
                val toke = Toke(
                    id = it,
                    tokeType = viewModel.typeSelection,
                    strain = viewModel.strainSelection,
                    tokeDateTime = viewModel.tokeDateTime.millis,
                    toolUsed = viewModel.toolSelection
                )
                viewModel.updateToke(toke)
            }
            findNavController().navigate(R.id.toke_log_screen)
        }
    }

    private fun prepareDeleteBtn() {
        deleteButton = view?.findViewById(R.id.add_toke_delete_button)
        deleteButton?.visibility = View.VISIBLE
        deleteButton?.setOnClickListener {
            tokeId?.let {
                showDeleteTokeConfirmationDialog(it)
            }
        }
    }

    private fun prepareTypeChipGroup() {
        tokeTypeChipGroup = view?.findViewById(R.id.add_toke_strain_type_chip_group)
        tokeTypeChipGroup?.setOnCheckedChangeListener { group, checkedId ->
            // force the ChipGroup to act like a RadioGroup
            // as in at least one chip must always be selected in the group
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i)
                chip.isClickable = chip.id != group.checkedChipId
            }

            when (checkedId) {
                R.id.add_toke_cbd_chip -> {
                    viewModel.typeSelection = ChronicTypes.CBD.name
                }
                R.id.add_toke_hybrid_chip -> {
                    viewModel.typeSelection = ChronicTypes.Hybrid.name
                }
                R.id.add_toke_indica_chip -> {
                    viewModel.typeSelection = ChronicTypes.Indica.name
                }
                R.id.add_toke_sativa_chip -> {
                    viewModel.typeSelection = ChronicTypes.Sativa.name
                }
            }
        }
    }

    private fun prepareToolChipGroup() {
        tokeToolChipGroup = view?.findViewById(R.id.add_toke_tool_chip_group)
        // add tool chips
        for (tool in Tools.values()) {
            val chip = Chip(tokeToolChipGroup?.context)
                .apply {
                    id = tool.ordinal
                    text = tool.name
                    setTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryText))
                    setTextAppearanceResource(R.style.TextAppearance_MaterialComponents_Chip)
                    isCheckable = true
                    isClickable = true
                    isFocusable = true
                    checkedIcon =
                        ContextCompat.getDrawable(activity!!, R.drawable.ic_check_mark_24dp)
                    isCheckedIconVisible = true
                    setChipBackgroundColorResource(R.color.colorPrimary)
                    setEnsureMinTouchTargetSize(true)
                    setChipStrokeColorResource(R.color.colorPrimaryText)
                    setChipStrokeWidthResource(R.dimen.chip_stroke_width)
                }

            tokeToolChipGroup?.addView(chip)
        }

        // handle onClick
        tokeToolChipGroup?.setOnCheckedChangeListener { group, checkedId ->
            // force the ChipGroup to act like a RadioGroup
            // as in at least one chip must always be selected in the group
            for (i in 0 until group.childCount) {
                val chip = group.getChildAt(i)
                chip.isClickable = chip.id != group.checkedChipId
            }

            // set viewModel.toolSelection
            when (checkedId) {
                Tools.Joint.ordinal ->
                    viewModel.toolSelection = Tools.Joint.name
                Tools.Vape.ordinal ->
                    viewModel.toolSelection = Tools.Vape.name
                Tools.Bong.ordinal ->
                    viewModel.toolSelection = Tools.Bong.name
                Tools.Pipe.ordinal ->
                    viewModel.toolSelection = Tools.Pipe.name
                Tools.Edible.ordinal ->
                    viewModel.toolSelection = Tools.Edible.name
                Tools.Dab.ordinal ->
                    viewModel.toolSelection = Tools.Dab.name
            }
        }

        // set default checked chip
        tokeToolChipGroup?.check(0)
    }

    private fun setTypeChipGroupSelection(tokeType: String) {
        when (tokeType) {
            ChronicTypes.Indica.name ->
                tokeTypeChipGroup?.check(R.id.add_toke_indica_chip)
            ChronicTypes.Hybrid.name ->
                tokeTypeChipGroup?.check(R.id.add_toke_hybrid_chip)
            ChronicTypes.Sativa.name ->
                tokeTypeChipGroup?.check(R.id.add_toke_sativa_chip)
            ChronicTypes.CBD.name ->
                tokeTypeChipGroup?.check(R.id.add_toke_cbd_chip)
            else ->
                tokeTypeChipGroup?.check(R.id.add_toke_sativa_chip)
        }
    }

    private fun setToolChipGroupSelection(tokeTool: String) {
        when (tokeTool) {
            Tools.Joint.name ->
                tokeToolChipGroup?.check(Tools.Joint.ordinal)
            Tools.Vape.name ->
                tokeToolChipGroup?.check(Tools.Vape.ordinal)
            Tools.Bong.name ->
                tokeToolChipGroup?.check(Tools.Bong.ordinal)
            Tools.Pipe.name ->
                tokeToolChipGroup?.check(Tools.Pipe.ordinal)
            Tools.Edible.name ->
                tokeToolChipGroup?.check(Tools.Edible.ordinal)
            Tools.Dab.name ->
                tokeToolChipGroup?.check(Tools.Dab.ordinal)
            else ->
                tokeToolChipGroup?.check(0)
        }
    }

    /**
     * Prepare and show this confirmation dialog when [deleteButton] is clicked
     *
     * @param tokeId the id of the toke to delete
     */
    private fun showDeleteTokeConfirmationDialog(tokeId: String) {
        val dialog = AlertDialog.Builder(activity!!)
        dialog
            .setTitle("Delete Toke?")
            .setMessage("This will permanently delete the Toke.")
            .setPositiveButton("Delete") { dialogBox, _ ->
                viewModel.deleteToke(tokeId)
                dialogBox.dismiss()
                findNavController().navigate(R.id.toke_log_screen)
            }
            .setNegativeButton("Cancel") { dialogBox, _ ->
                dialogBox.dismiss()
            }
            .show()
    }


}