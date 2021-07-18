package com.natassm.calendarpickerlib

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.natassm.calendarpickerlib.entity.NSCalendarEntity
import com.natassm.calendarpickerlib.entity.NSGroupEntity
import java.util.*

class NSCalendarView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    var list: MutableList<NSGroupEntity> = mutableListOf()

    init {
        orientation = VERTICAL
        generateCalendarEvent()
        drawBaseView()
    }

    fun drawBaseView() {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        removeAllViews()
        generateDateView()
        list.forEach {
            val calendarWeekView = NSCalendarWeekView(context)
            calendarWeekView.layoutParams = layoutParams
            calendarWeekView.gravity = Gravity.CENTER_VERTICAL
            calendarWeekView.drawView(it.calendarList)
            addView(calendarWeekView)
        }
    }

    private fun generateDateView() {
        val horizontalDayLinearLayout = LinearLayout(context)
        horizontalDayLinearLayout.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        horizontalDayLinearLayout.orientation = HORIZONTAL

        val layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f

        for (a in 0 until 7) {
            val textView = TextView(context)
            textView.text = when (a) {
                0 -> "SUN"
                1 -> "MON"
                2 -> "TUE"
                3 -> "WED"
                4 -> "THU"
                5 -> "FRI"
                else -> "SAT"
            }
            textView.gravity = Gravity.CENTER
            textView.layoutParams = layoutParams
            textView.setTypeface(null, Typeface.BOLD)
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))

            horizontalDayLinearLayout.addView(textView)
        }

        addView(horizontalDayLinearLayout)
    }

    fun generateCalendarEvent() {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.DATE, 1)

        val groupEntity = NSGroupEntity()
        list.add(groupEntity)

        for (a in 0 until calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){

            val calendarEntity = NSCalendarEntity(calendar.timeInMillis)

            if (NSUIHelper.convertLongToPatternTime("EEEE", calendar.timeInMillis).equals("Minggu", ignoreCase = true)) {
                val tempGroupEntity = NSGroupEntity()
                tempGroupEntity.calendarList.add(calendarEntity)
                list.add(tempGroupEntity)
            } else {
                list.last().calendarList.add(calendarEntity)
            }

            calendar.add(Calendar.DATE, 1)
        }
    }
}