package com.natassm.calendarpickerlib.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NSDayEntity (
    var time: Long = 0,
    var event: String = "",
    var isHoliday: Boolean = false,
    var isActive: Boolean = false,
    var isActiveMonth: Boolean = false,
    var hasEvent: Boolean = false,
    var isSelected: Boolean = false
) : Parcelable
