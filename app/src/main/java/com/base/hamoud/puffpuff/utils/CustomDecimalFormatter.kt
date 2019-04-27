package com.base.hamoud.puffpuff.utils

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.DecimalFormat

open class CustomDecimalFormatter : IndexAxisValueFormatter() {

    private val decimalFormat: DecimalFormat = DecimalFormat("#")

    override fun getFormattedValue(value: Float): String {
        if (value == 0f) {
            return ""
        }

        return decimalFormat.format(value)
    }
}