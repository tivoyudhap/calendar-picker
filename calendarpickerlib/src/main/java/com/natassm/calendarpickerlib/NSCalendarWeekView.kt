package com.natassm.calendarpickerlib

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.natassm.calendarpickerlib.entity.NSCalendarEntity

class NSCalendarWeekView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL
    }

    fun drawView(entity: List<NSCalendarEntity>) {

        val layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f
        removeAllViews()

        entity.forEach {
            val calendarDayView = NSCalendarDayView(context)
            calendarDayView.layoutParams = layoutParams
            calendarDayView.setUpData(it)
            addView(calendarDayView)
        }
    }
}