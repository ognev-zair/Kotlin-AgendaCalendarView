package com.ognev.kotlin.agendacalendarview.sample

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity  : AppCompatActivity(), CalendarController {

    private var oldDate: Calendar? = null
    var eventList: MutableList<CalendarEvent>? = null
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private lateinit var contentManager: CalendarContentManager
    override fun getEmptyEventLayout(): Int {
        return R.layout.view_agenda_empty_event
    }

    override fun getEventLayout(): Int {
        return R.layout.view_agenda_event
    }

    override fun onDaySelected(dayItem: IDayItem) {
    }

//    override fun onEventSelected(event: CalendarEvent) {
//    }

    fun isSameDays(oldDate: Calendar, newDate: Calendar): Boolean {
        return oldDate.get(Calendar.DAY_OF_MONTH) == newDate.get(Calendar.DAY_OF_MONTH)
    }


    override fun onScrollToDate(calendar: Calendar) {
        var lastPosition = agenda_calendar_view.agendaView.agendaListView.lastVisiblePosition + 1
        if (!isSameDays(oldDate as Calendar, calendar)) {
            if (lastPosition == CalendarManager.getInstance(this).events.size) {
                if(!agenda_calendar_view.isCalendarLoading()) // condition to prevent asynchronous requests
                loadItemsAsync(false)
            }
        }

        if (agenda_calendar_view.agendaView.agendaListView.firstVisiblePosition === 0) {
            val minCal = Calendar.getInstance()
            minCal.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            if (calendar.get(Calendar.DAY_OF_MONTH) == minCal.get(Calendar.DAY_OF_MONTH)) {
                if(!agenda_calendar_view.isCalendarLoading())  // condition to prevent asynchronous requests
                loadItemsAsync(true)
            }
        }

        oldDate = calendar


    }



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

        contentManager = CalendarContentManager(this, agenda_calendar_view, SampleEventAgendaAdapter())

        contentManager.locale = Locale.ENGLISH
        contentManager.setDateRange(minDate, maxDate)


        val maxLength = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxLength) {
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = System.currentTimeMillis();
            day.set(Calendar.DAY_OF_MONTH, i)

            eventList!!.add(MyCalendarEvent(day, day,
                    DayItem.buildDayItemFromCal(day), null).setEventInstanceDay(day))

        }


        contentManager.loadItemsFromStart(eventList!!)
        agenda_calendar_view.agendaView.agendaListView.setOnItemClickListener({ parent: AdapterView<*>, view: View, position: Int,
                                                                                id: Long ->

            Toast.makeText(view.context, "item: ".plus(position), Toast.LENGTH_SHORT ).show()
            /*calendarController!!.onEventSelected(CalendarManager.instance!!.events[position]) */})

    }

    var startMonth: Int =  Calendar.getInstance().get(Calendar.MONTH)
    var endMonth: Int =  Calendar.getInstance().get(Calendar.MONTH)

    fun loadItemsAsync(addFromStart: Boolean) {
        object : AsyncTask<Unit, Unit, Unit>() {


            private var startMonthCal: Calendar = Calendar.getInstance()
            private var endMonthCal: Calendar = Calendar.getInstance()


            override fun onPreExecute() {
                super.onPreExecute()
                agenda_calendar_view.showProgress()
                eventList!!.clear()
            }

            override fun doInBackground(vararg params: Unit) {
                Thread.sleep(2000) // simulating requesting json via rest api

                if(addFromStart) {
                     if(startMonth == 0)
                        startMonth = 11
                     else startMonth--

                    startMonthCal.set(Calendar.MONTH, startMonth)
                    if(startMonth == 11) {
                        var year = startMonthCal.get(Calendar.YEAR);
                        startMonthCal.set(Calendar.YEAR, ++year)
                    }


                    for(i in 1..startMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        val day = Calendar.getInstance(Locale.ENGLISH)
                        day.timeInMillis = System.currentTimeMillis();
                        day.set(Calendar.MONTH, startMonth)
                        day.set(Calendar.DAY_OF_MONTH, i)
                        if(endMonth == 11)
                            day.set(Calendar.YEAR, day.get(Calendar.YEAR) - 1)
                        eventList!!.add(MyCalendarEvent(day, day,
                                DayItem.buildDayItemFromCal(day), SampleEvent("Awesome ".plus(i), "Event ".plus(i))).setEventInstanceDay(day))
                    }
                } else {
                    if(endMonth >= 11)
                        endMonth = 0
                    else endMonth++

                    endMonthCal.set(Calendar.MONTH, endMonth)
                    if(endMonth == 0) {
                        var year = endMonthCal.get(Calendar.YEAR);
                        endMonthCal.set(Calendar.YEAR, ++year)
                    }

                    for(i in 1..endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        val day = Calendar.getInstance(Locale.ENGLISH)
                        day.timeInMillis = System.currentTimeMillis();
                        day.set(Calendar.MONTH, endMonth)
                        day.set(Calendar.DAY_OF_MONTH, i)
                        if(endMonth == 0)
                        day.set(Calendar.YEAR, day.get(Calendar.YEAR) + 1)

                        eventList!!.add(MyCalendarEvent(day, day,
                                DayItem.buildDayItemFromCal(day), SampleEvent("Awesome ".plus(i), "Event ".plus(i))).setEventInstanceDay(day))

                    }
                }

            }

            override fun onPostExecute(user: Unit) {
                if(addFromStart)
                contentManager.loadItemsFromStart(eventList!!)
                else
                    contentManager.loadFromEndCalendar(eventList!!)


            }
        }.execute()
    }


}
