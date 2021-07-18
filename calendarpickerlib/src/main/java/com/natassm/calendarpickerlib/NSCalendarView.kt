package com.natassm.calendarpickerlib

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.natassm.calendarpickerlib.entity.NSDayEntity
import com.natassm.calendarpickerlib.entity.NSWeekEntity
import java.util.*

class NSCalendarView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    private val list: MutableList<NSWeekEntity> = mutableListOf()
    var eventList: MutableList<Long> = mutableListOf()
        set(eventList) {
            field = eventList
            mapEvent()
        }
    var selectedDate: Long = Date().time
    var listener: NSOnCalendarClicked? = null
    private val itemListener: NSOnCalendarClicked = object : NSOnCalendarClicked {
        override fun itemClicked(data: NSDayEntity) {
            if (data.isActiveMonth) {
                selectedDate = data.time
                list.forEach {
                    it.weekList.forEach {
                        it.isActive = it.time == data.time
                    }
                }

                drawBaseView()
                listener?.itemClicked(data)
            }
        }
    }

    init {
        orientation = VERTICAL
        generateCalendarEvent()
        drawBaseView()
    }

    private fun drawBaseView() {
        removeAllViews()
        generateDateView()
        for (entity in list) {
            val baseHorizontalLinearLayout = NSCalendarWeekView(context)
            baseHorizontalLinearLayout.listener = itemListener
            baseHorizontalLinearLayout.drawView(entity.weekList)
            baseHorizontalLinearLayout.gravity = Gravity.CENTER_VERTICAL
            baseHorizontalLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            addView(baseHorizontalLinearLayout)
        }
    }

    private fun mapEvent() {
        list.forEach {
            it.weekList.forEach { entity ->
                entity.hasEvent = eventList.firstOrNull { data -> entity.time == data } != null
            }
        }

        drawBaseView()
    }

    private fun generateDateView() {
        val horizontalDayLinearLayout = LinearLayout(context)
        horizontalDayLinearLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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

    private fun generateCalendarEvent() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val savedMonth = calendar.get(Calendar.MONTH)
        var firstDateInterval = 0

        calendar.set(Calendar.DATE, 1)

        val groupEntity = NSWeekEntity()
        list.add(groupEntity)

        for (a in 0 until calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            val calendarEntity = NSDayEntity(time = calendar.timeInMillis, isHoliday = NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Sabtu", ignoreCase = true) || NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Minggu", ignoreCase = true), isActive = Calendar.getInstance().get(
                Calendar.DATE) == a + 1, isActiveMonth = true)

            if (NSUIHelper.convertLongToPatternTime(DATE_DAY_ONLY, calendar.timeInMillis).equals("Minggu", ignoreCase = true)) {
                val nextGroupEntity = NSWeekEntity()
                nextGroupEntity.weekList.add(calendarEntity)
                list.add(nextGroupEntity)
            } else {
                list.last().weekList.add(calendarEntity)
            }

            if (list.first().weekList.size == 1) {
                firstDateInterval = firstDateInterval(list.first().weekList.first().time)
            }

            calendar.add(Calendar.DATE, 1)
        }

        calendar.set(Calendar.DATE, 1)
        for (a in 0 until firstDateInterval) {
            if (a == 0) {
                calendar.add(Calendar.MONTH, -1)
            }

            calendar.add(Calendar.DATE, -1)
            list.first().weekList.add(0, NSDayEntity(time = calendar.timeInMillis, isHoliday = NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Sabtu", ignoreCase = true) or NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Minggu", ignoreCase = true), isActive = false)
            )
        }

        calendar.set(Calendar.MONTH, savedMonth + 1)
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        for (a in 0 until lastDateInterval(list.last().weekList.last().time)) {
            calendar.add(Calendar.DATE, 1)
            list.last().weekList.add(
                NSDayEntity(time = calendar.timeInMillis, isHoliday = NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Sabtu", ignoreCase = true) or NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Minggu", ignoreCase = true), isActive = false)
            )
        }
    }

    private fun countExceedCalendar(calendar: Calendar, dateInterval: Int, isReverse: Boolean) {
        for (a in 0 until dateInterval) {
            if (isReverse) {
                if (a == 0) {
                    calendar.add(Calendar.MONTH, -1)
                }

                calendar.add(Calendar.DATE, -1)
            } else {
                calendar.add(Calendar.DATE, 1)
            }

            val calendarEntity = NSDayEntity(time = calendar.timeInMillis, isHoliday = NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Sabtu", ignoreCase = true) or NSUIHelper.convertLongToPatternTime(
                DATE_DAY_ONLY, calendar.timeInMillis).equals("Minggu", ignoreCase = true), isActive = false, isActiveMonth = false)

            if (isReverse) {
                list.last().weekList.add(0, calendarEntity)
            } else {
                list.last().weekList.add(calendarEntity)
            }
        }
    }

    private fun modulo(a: Int, firstDateInterval: Int): Int = if (a <= (7 - firstDateInterval)) 7 - firstDateInterval else 7

    private fun firstDateInterval(time: Long): Int =
        when (NSUIHelper.convertLongToPatternTime(DATE_DAY_ONLY, time)) {
            "Minggu" -> 0
            "Senin" -> 1
            "Selasa" -> 2
            "Rabu" -> 3
            "Kamis" -> 4
            "Jumat" -> 5
            else -> 6
        }

    private fun lastDateInterval(time: Long): Int = when (NSUIHelper.convertLongToPatternTime(DATE_DAY_ONLY, time)) {
        "Minggu" -> 6
        "Senin" -> 5
        "Selasa" -> 4
        "Rabu" -> 3
        "Kamis" -> 2
        "Jumat" -> 1
        else -> 0
    }
}