package com.natassm.calendarpickerlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.natassm.calendarpickerlib.databinding.ViewCalendarDayBinding
import com.natassm.calendarpickerlib.entity.NSCalendarEntity

class NSCalendarDayView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCalendarDayBinding by lazy {
        ViewCalendarDayBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_calendar_day, this, true)
    }

    fun setUpData(entity: NSCalendarEntity) {
        binding.dayTextView

        NSUIHelper.convertLongToPatternTime("dd", entity.time)
    }
}