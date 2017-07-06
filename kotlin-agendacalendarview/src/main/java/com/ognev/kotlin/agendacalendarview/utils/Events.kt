package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.models.IDayItem

import java.util.Calendar

/**
 * Events emitted by the bus provider.
 */
class Events {

    open class DayClickedEvent(var day: IDayItem) {

        var calendar: Calendar

        init {
            this.calendar = Calendar.getInstance()
            this.calendar.time = day.date
        }
    }

    open class CalendarScrolledEvent {
        companion object {
            var variableName = object : CalendarScrolledEvent() {
            }
        }
    }

    open class AgendaListViewTouchedEvent

    open class EventsFetched

    open class ForecastFetched
}
