package com.ognev.kotlin.agendacalendarview.builder

import android.app.Application
import com.ognev.kotlin.agendacalendarview.AgendaCalendarView
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.models.BaseCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.DefaultEventAdapter
import java.util.*

/**
 * Created by ognev on 7/5/17.
 */
class CalendarContentManager(
        val calendarController: CalendarController,
        val agendaCalendarView: AgendaCalendarView,
        val sampleAgendaAdapter: DefaultEventAdapter) {

    private var minDate: Calendar? = null
    private var maxDate: Calendar? = null
    private var isInitialised: Boolean = false;
    var locale: Locale? = null
    private lateinit var calendarManager: CalendarManager

    /*default Locale */
    init {
        locale = Locale.ENGLISH
    }

    fun setDateRange(minDate: Calendar, maxDate: Calendar) {
        this.minDate = minDate
        this.maxDate = maxDate
        initManager()
    }

    private fun initManager() {
        calendarManager = CalendarManager.getInstance(agendaCalendarView.context)
        calendarManager.locale = locale
        calendarManager.buildCal(minDate, maxDate)
    }


    fun loadItemsFromStart(eventList: MutableList<CalendarEvent>) {
       if(!isInitialised)
           initialiseCalendar(eventList)
        else {
           calendarManager.addFromStartEvents(eventList, BaseCalendarEvent())
           (agendaCalendarView.agendaView.agendaListView.adapter as AgendaAdapter).updateEvents()
           agendaCalendarView.agendaView.agendaListView.scrollToCurrentDate(calendarManager.currentSelectedDay)
       }
    }

    fun loadFromEndCalendar(eventList: MutableList<CalendarEvent>) {
        if(!isInitialised)
        initialiseCalendar(eventList)
        else {
            calendarManager.addEvents(eventList, BaseCalendarEvent())
            (agendaCalendarView.agendaView.agendaListView.adapter as AgendaAdapter).updateEvents()
//            if (isSelectedDay) {
                agendaCalendarView.agendaView.agendaListView.scrollToCurrentDate(calendarManager.currentSelectedDay)
            //                  mAgendaCalendarView.getAgendaView().getAgendaListView().setSelection(day);
//                Handler().postDelayed({ isDaySelection = false }, 5000)
//            }
        }
    }

    private fun initialiseCalendar(eventList: MutableList<CalendarEvent>) {
        isInitialised = true
        calendarManager.loadInitialEvents(eventList)
        val readyEvents = calendarManager.events
        val readyDays = calendarManager.days
        val readyWeeks = calendarManager.weeks
        agendaCalendarView.init(readyWeeks, readyDays, readyEvents, sampleAgendaAdapter)
        agendaCalendarView.setCallbacks(calendarController)
    }


}
