package com.natassm.calendarpickerlib.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NSCalendarEntity (
    var time: Long = 0
) : Parcelable
