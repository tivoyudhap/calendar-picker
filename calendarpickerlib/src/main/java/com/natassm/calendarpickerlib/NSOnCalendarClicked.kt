package com.natassm.calendarpickerlib

import com.natassm.calendarpickerlib.entity.NSDayEntity

interface NSOnCalendarClicked {
    fun itemClicked(data: NSDayEntity)
}