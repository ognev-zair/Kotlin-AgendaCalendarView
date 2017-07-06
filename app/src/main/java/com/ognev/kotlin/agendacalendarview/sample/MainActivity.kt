package com.ognev.kotlin.agendacalendarview.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity  : AppCompatActivity(), CalendarController {

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


        var contentManager: CalendarContentManager =
                CalendarContentManager(this, agenda_calendar_view, SampleAgendaAdapter())

        contentManager.locale = Locale.ENGLISH
        contentManager.setDateRange(minDate, maxDate)


        val maxLength = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxLength) {
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = System.currentTimeMillis();
            day.set(Calendar.DAY_OF_MONTH, i)

            eventList!!.add(MyCalendarEvent(day, day,
                    DayItem.buildDayItemFromCal(day), SampleEvent()).setEventInstanceDay(day))

        }


        contentManager.loadItemsFromStart(eventList!!)

    }
}
