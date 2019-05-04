package com.base.hamoud.puffpuff.ui.addtoke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.timePicker
import com.base.hamoud.puffpuff.R
import com.base.hamoud.puffpuff.data.model.ChronicTypes
import com.base.hamoud.puffpuff.data.model.Tools
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.joda.time.DateTime
import java.util.*


class AddTokeScreen : Fragment() {

    private lateinit var viewModel: AddTokeViewModel

    private var saveButton: FloatingActionButton? = null
    private var strainEditText: EditText? = null
    private var timeInputView: MaterialButton? = null
    private var dateInputView: MaterialButton? = null
    private var tokeTypeChipGroup: ChipGroup? = null
    private var tokeToolChipGroup: ChipGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_add_toke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AddTokeViewModel::class.java)

        // prepare ui
        prepareView()

        // observe
        observeDateTimeLiveData()
        observeOnLastAddedTokeLive()

        // trigger
        viewModel.getLastAddedToke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanupReferences()
    }

    private fun observeDateTimeLiveData() {
        viewModel.dateTimeLiveData.observe(viewLifecycleOwner, Observer<DateTime> {
            dateInputView?.text = viewModel.getFormattedTokeDate()
            timeInputView?.text = viewModel.getFormattedTokeTime()
        })
    }

    private fun observeOnLastAddedTokeLive() {
        viewModel.lastAddedTokeLive.observe(viewLifecycleOwner, Observer { toke ->
            toke?.let {
                strainEditText?.setText(it.strain)
                setTypeChipGroupSelection(it.tokeType)
                setToolChipGroupSelection(it.toolUsed)
            }
        })
    }

    private fun prepareView() {
        prepareDateField()
        prepareTimeField()
        prepareTypeChipGroup()
        prepareToolChipGroup()
        prepareStrainField()
        prepareSaveBtn()
    }

    private fun cleanupReferences() {
        saveButton = null
        strainEditText = null
        timeInputView = null
        dateInputView = null
        tokeTypeChipGroup = null
        tokeToolChipGroup = null
    }

    private fun prepareDateField() {
        dateInputView = view?.findViewById(R.id.add_toke_date_field)
        dateInputView?.text = viewModel.getFormattedTokeDate()
        // handle onClick
        dateInputView?.setOnClickListener {
            // show datePicker dialog
            MaterialDialog(activity!!).show {
                val startDate = Calendar.getInstance()
                datePicker(null, startDate) { dialog, date ->
                    // Use date (Calendar)
                    viewModel.updateDate(
                        year = date.get(Calendar.YEAR),
                        month = date.get(Calendar.MONTH) + 1,
                        dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
                    )
                }
            }
        }
    }

    private fun prepareTimeField() {
        timeInputView = view?.findViewById(R.id.add_toke_time_field)
        timeInputView?.text = viewModel.getFormattedTokeTime()
        // handle onClick
        timeInputView?.setOnClickListener {
            // show timePicker dialog
            MaterialDialog(activity!!).show {
                val startTime = Calendar.getInstance()
                val shouldShow24Hr = android.text.format.DateFormat.is24HourFormat(activity)
                timePicker(startTime, false, shouldShow24Hr) { dialog, date ->
                    // Use date (Calendar)
                    viewModel.updateTime(
                        hour = date.get(Calendar.HOUR_OF_DAY),
                        minute = date.get(Calendar.MINUTE)
                    )
                }
            }
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
            viewModel.saveToke()
            findNavController().navigate(R.id.journal_screen)
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

            // set viewModel.typeSelection
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

        // set default checked chip
        tokeTypeChipGroup?.check(R.id.add_toke_sativa_chip)
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

}