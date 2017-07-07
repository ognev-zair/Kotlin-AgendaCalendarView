package com.ognev.kotlin.agendacalendarview.sample

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    override fun onEventSelected(event: CalendarEvent) {
    }

    fun isSameDays(oldDate: Calendar, newDate: Calendar): Boolean {
        return oldDate.get(Calendar.DAY_OF_MONTH) == newDate.get(Calendar.DAY_OF_MONTH)
    }


    override fun onScrollToDate(calendar: Calendar) {
        var lastPosition = agenda_calendar_view.agendaView.agendaListView.lastVisiblePosition + 1
        if (!isSameDays(oldDate as Calendar, calendar)) {
            if (lastPosition == CalendarManager.getInstance(this).events.size) {
                //          Calendar startCal = Calendar.getInstance(locale);
                //          startCal.setTimeInMillis(calendar.getTimeInMillis());
                //          startCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH));

                val endCal = Calendar.getInstance(Locale.CANADA)
                endCal.setTimeInMillis(calendar.timeInMillis)
                //          endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 1);
                endCal.add(Calendar.MONTH, 1)
                endCal.getTime()
//                Log.d("calendar: ", endCal.get(Calendar.DAY_OF_MONTH)  " " + endCal.get(Calendar.MONTH) + " " + endCal.get(Calendar.YEAR))

                //          long time[] = new long[2];
                //          time[0] = DateUtil.getStartDatesInMoth(startCal);
                //          time[1] = DateUtil.getEndDatesInMoth(endCal);

                loadItemsAsync(false)

            }
        }

        if (agenda_calendar_view.agendaView.agendaListView.firstVisiblePosition === 0) {
            val minCal = Calendar.getInstance()
            minCal.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            //      if (mAgendaCalendarView.getAgendaView().getAgendaListView().getFirstVisiblePosition() == 0) {
            if (calendar.get(Calendar.DAY_OF_MONTH) == minCal.get(Calendar.DAY_OF_MONTH)) {
                //        Calendar netCal = Calendar.getInstance(locale);
                //        netCal.setTimeInMillis(calendar.getTimeInMillis());
                //        Log.d("visits", "newMoth:" + netCal.get(Calendar.MONTH));
                //        netCal.set(Calendar.MONTH, netCal.get(Calendar.MONTH));
                //        oldDate = calendar;

                //        Calendar startCal = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1)
                calendar.time
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                calendar.time
                Log.d("calendar: ", calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.YEAR))

                //        startCal.setTimeInMillis(calendar.getTimeInMillis());
                //        startCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH) - 1);

                //        Calendar endCal = Calendar.getInstance(locale);
                //        endCal.setTimeInMillis(calendar.getTimeInMillis());
                //        endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH));
                //
                //        long time[] = new long[2];
                //        time[0] = DateUtil.getStartDatesInMoth(startCal);
                //        time[1] = DateUtil.getEndDatesInMoth(endCal);

                //        getVisits(DateUtil.getStartEndDatesInMoth(netCal), true, false, 0);
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


        contentManager = CalendarContentManager(this, agenda_calendar_view, SampleAgendaAdapter())

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


    fun loadItemsAsync(addFromStart: Boolean) {
        object : AsyncTask<Unit, Unit, Unit>() {


            private var startMonthCal: Calendar = Calendar.getInstance()
            private var endMonthCal: Calendar = Calendar.getInstance()
            var startMonth: Int =  Calendar.getInstance().get(Calendar.MONTH)
            var endMonth: Int = startMonth

            override fun onPreExecute() {
                super.onPreExecute()
                eventList!!.clear()
            }

            override fun doInBackground(vararg params: Unit) {
                if(addFromStart) {
                    startMonth = if(startMonth == 0) 11 else startMonth.dec()

                    endMonthCal.set(Calendar.MONTH, endMonth)
                    if(endMonth == 11)
                        endMonthCal.set(Calendar.YEAR, endMonthCal.get(Calendar.YEAR).dec())


                    for(i in 1..startMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        val day = Calendar.getInstance(Locale.ENGLISH)
                        day.timeInMillis = System.currentTimeMillis();
                        day.set(Calendar.MONTH, startMonth)
                        day.set(Calendar.DAY_OF_MONTH, i)
                        if(endMonth == 11)
                            day.set(Calendar.YEAR, day.get(Calendar.YEAR) - 1)
                        eventList!!.add(MyCalendarEvent(day, day,
                                DayItem.buildDayItemFromCal(day), SampleEvent()).setEventInstanceDay(day))
                    }
                } else {
                    endMonth = if(endMonth >= 11) 0 else endMonth.inc()

                    endMonthCal.set(Calendar.MONTH, endMonth)
                    if(endMonth == 0)
                        endMonthCal.set(Calendar.YEAR, endMonthCal.get(Calendar.YEAR).inc())

                    for(i in 1..endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        val day = Calendar.getInstance(Locale.ENGLISH)
                        day.timeInMillis = System.currentTimeMillis();
                        day.set(Calendar.MONTH, endMonth)
                        day.set(Calendar.DAY_OF_MONTH, i)
                        if(endMonth == 0)
                        day.set(Calendar.YEAR, day.get(Calendar.YEAR) + 1)

                        eventList!!.add(MyCalendarEvent(day, day,
                                DayItem.buildDayItemFromCal(day), SampleEvent()).setEventInstanceDay(day))

                    }
                }
              Thread.sleep(5000)

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
