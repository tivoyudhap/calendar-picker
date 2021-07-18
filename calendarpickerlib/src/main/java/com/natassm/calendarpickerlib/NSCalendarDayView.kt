package com.natassm.calendarpickerlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.natassm.calendarpickerlib.databinding.ViewCalendarDayBinding
import com.natassm.calendarpickerlib.entity.NSDayEntity

const val DATE_DAY_ONLY = "EEEE"
const val DATE_ONLY = "dd"

class NSCalendarDayView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private lateinit var entity: NSDayEntity
    lateinit var listener: NSOnCalendarClicked

    private val binding: ViewCalendarDayBinding by lazy {
        ViewCalendarDayBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        orientation = HORIZONTAL
        binding.calendarItemBaseConstraint.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.calendarItemBaseConstraint -> {
                listener.itemClicked(entity)
            }
        }
    }

    fun setUpData(entity: NSDayEntity) {
        this.entity = entity
        binding.calendarItemDateTextView.setTextColor(ContextCompat.getColor(context, if (entity.isActive) R.color.black else R.color.dark_grey))
        binding.calendarItemBaseConstraint.isActivated = entity.isSelected

        binding.calendarItemDateTextView.alpha = if (entity.isActiveMonth) 1f else 0.5f
        binding.calendarItemDateTextView.text = NSUIHelper.convertLongToPatternTime(DATE_ONLY, entity.time).toInt().toString()
    }
}