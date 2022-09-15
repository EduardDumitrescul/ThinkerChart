package com.example.thinkerchart

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class ChartDatePicker(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet){
    init {
        initDefaults()
    }

    private fun initDefaults() {
        View.inflate(context, R.layout.chart_date_picker, this)
    }
}