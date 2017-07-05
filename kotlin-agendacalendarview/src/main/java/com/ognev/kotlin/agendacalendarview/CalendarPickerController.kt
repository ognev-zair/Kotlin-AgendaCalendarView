package com.ognev.kotlin.agendacalendarview

import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem

import java.util.Calendar

interface CalendarPickerController {
    fun onDaySelected(dayItem: IDayItem)

    fun onEventSelected(event: CalendarEvent)

    fun onScrollToDate(calendar: Calendar)
}
