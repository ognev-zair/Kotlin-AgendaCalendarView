package com.ognev.kotlin.agendacalendarview.builder

import android.content.Context
import com.ognev.kotlin.agendacalendarview.AgendaCalendarView
import com.ognev.kotlin.agendacalendarview.CalendarItemLayout
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.CalendarPickerController
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.models.BaseCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.DefaultEventRenderer
import java.util.*

/**
 * Created by ognev on 7/5/17.
 */
class CalendarContentHelper(val context: Context, val agendaCalendarView: AgendaCalendarView) {

    private var minDate: Calendar? = null
    private var maxDate: Calendar? = null
    var locale: Locale? = null
    private lateinit var calendarManager: CalendarManager

    init {
        locale = Locale.ENGLISH
    }



    fun setDateRange(minDate: Calendar, maxDate: Calendar) {
        this.minDate = minDate
        this.maxDate = maxDate
        calendarManager = CalendarManager.getInstance(context)
        calendarManager.locale = locale
        calendarManager.buildCal(minDate, maxDate)
    }


    fun loadCalendar(eventList: MutableList<CalendarEvent<Any>>, sampleAgendaAdapter: DefaultEventRenderer, calendarPickerController: CalendarPickerController, calendarItemLayout: CalendarItemLayout) {
        calendarManager.loadEvents(eventList, BaseCalendarEvent())
        val readyEvents = calendarManager.events
        val readyDays = calendarManager.days
        val readyWeeks = calendarManager.weeks
        agendaCalendarView.init(readyWeeks, readyDays, readyEvents, sampleAgendaAdapter, calendarPickerController, calendarItemLayout)
    }




}