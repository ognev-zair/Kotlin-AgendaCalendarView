package com.ognev.kotlin.agendacalendarview.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ognev.kotlin.agendacalendarview.AgendaCalendarView
import com.ognev.kotlin.agendacalendarview.CalendarItemLayout
import com.ognev.kotlin.agendacalendarview.CalendarPickerController
import com.ognev.kotlin.agendacalendarview.models.BaseCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity  : AppCompatActivity(), CalendarPickerController, CalendarItemLayout {
    override fun getEmptyEventLayout(): Int {
        return R.layout.view_agenda_empty_event
    }

    override fun getEventLayout(): Int {
        return R.layout.view_agenda_event
    }

    override fun onDaySelected(dayItem: IDayItem) {
    }

    override fun onEventSelected(event: CalendarEvent<Any>) {
    }

    override fun onScrollToDate(calendar: Calendar) {
    }

    private var oldDate: Calendar? = null
    var eventList: MutableList<CalendarEvent<Any>>? = null
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        oldDate = Calendar.getInstance()

        minDate = Calendar.getInstance()
        maxDate = Calendar.getInstance()

        minDate.add(Calendar.MONTH, -10)
        minDate.add(Calendar.YEAR, -1)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)

        eventList = ArrayList()

        eventList!!.clear()


        var contentHelper: CalendarContentHelper = CalendarContentHelper(this, agenda_calendar_view)
        contentHelper.locale = Locale.ENGLISH;
        contentHelper.setDateRange(minDate, maxDate)


        for (i in 1..30) {
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = System.currentTimeMillis();
            day.set(Calendar.DAY_OF_MONTH, i)
//
//            hasEvent = false
//
//            if (visitListResponseData.data.visits != null)
//                for (j in 0..visitListResponseData.data.visits.size() - 1) {
//                    val visitDay = Calendar.getInstance(locale)
//                    visitDay.timeInMillis = visitListResponseData.data.visits.get(j).schedule.visitDate
//                    if (day.get(Calendar.DAY_OF_MONTH) === visitDay.get(Calendar.DAY_OF_MONTH)) {
//                        val startTime = Calendar.getInstance(locale)
//                        startTime.timeInMillis = visitListResponseData.data.visits.get(j).schedule.visitDate
//
//                        var color = ""
//                        when (visitListResponseData.data.visits.get(j).status) {
//                            Visit.NOT_CONFIRMED -> color = "#e3913b"
//
//                            Visit.IN_WAIT, Visit.IN_PROGRESS -> color = "#2a69a3"
//
//                            Visit.FINISHED -> color = "#1ea065"
//                            Visit.CANCELED, Visit.CLOSE -> {
//                            }
//                        }
//                        eventList.add(BaseCalendarEvent(visitListResponseData.data.visits.get(j).patientName, visitListResponseData.data.visits.get(j).comment,
//                                visitListResponseData.data.visits.get(j).schedule.formTime,
//                                color,
//                                visitListResponseData.data.visits.get(j).status,
//                                visitListResponseData.data.visits.get(j).id,
//                                startTime, startTime, true,
//                                DayItem.buildDayItemFromCal(startTime)).setInstanceDay(startTime))
//                        hasEvent = true
//
//                    }
//                }
//
//            if (!hasEvent) {
            eventList!!.add(MyCalendarEvent("", "", "",
                    "#f58c27",
                    day, day, true,
                    DayItem.buildDayItemFromCal(day), false, SampleEvent()).setInstanceDay(day))
//            }

        }

        contentHelper.loadCalendar(eventList!!, SampleAgendaAdapter(), this, this)

//        if (isFirst) {
//            isFirst = false
//            calendarManager.buildCal(minDate, maxDate)
//            calendarManager.loadEvents(eventList, BaseCalendarEvent())
//            ////////
//
//            val readyEvents = calendarManager.getEvents()
//            val readyDays = calendarManager.getDays()
//            val readyWeeks = calendarManager.getWeeks()
//            mAgendaCalendarView.init(readyWeeks, readyDays, readyEvents, this@CalendarViewModel)
//
//            (mAgendaCalendarView.getAgendaView().getAgendaListView().getAdapter() as AgendaAdapter).setOnVisitClickListener({ v ->
//
//                val position = Integer.parseInt(v.getTag().toString())
//
//                if (calendarManager.getEvents().get(position).hasEvent() && calendarManager.getEvents().get(position).getStatus() !== Visit.CLOSE) {
//                    eventPosition = position
//                    oldDate = calendarManager.getEvents().get(position).getEndTime()
//                    context.startActivity
// (Intent(context, AboutVisitActivity::class.java).putExtra(C.VISIT_ID, calendarManager.getEvents().get(position).getId()))
//                }
//            })
//
//        } else {
//            if (fromStart) {
//                calendarManager.addFromStartEvents(eventList, BaseCalendarEvent())
//                (mAgendaCalendarView.getAgendaView().getAgendaListView().getAdapter() as AgendaAdapter).updateEvents()
//                mAgendaCalendarView.getAgendaView().getAgendaListView().setSelection(eventList.size() - 1)
//                //                mAgendaCalendarView.getAgendaView().getAgendaListView().scrollToCurrentDate(oldDate);
//            } else {


        ////////



//                (agenda_calendar_view.agendaView.agendaListView.adapter as AgendaAdapter).updateEvents()
//                if (isSelectedDay) {
//                    mAgendaCalendarView.getAgendaView().getAgendaListView().scrollToCurrentDate(oldDate)
//                    //                  mAgendaCalendarView.getAgendaView().getAgendaListView().setSelection(day);
//                    Handler().postDelayed({ isDaySelection = false }, 5000)
////                }
////            }
//        }

    }
}
