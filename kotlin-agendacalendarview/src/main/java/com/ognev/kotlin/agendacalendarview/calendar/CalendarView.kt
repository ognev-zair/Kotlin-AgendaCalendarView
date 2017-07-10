package com.ognev.kotlin.agendacalendarview.calendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.calendar.weekslist.WeekListView
import com.ognev.kotlin.agendacalendarview.calendar.weekslist.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import com.ognev.kotlin.agendacalendarview.utils.Events
import com.ognev.kotlin.agendacalendarview.R

import java.text.SimpleDateFormat
import java.util.*

/**
 * The calendar view is a freely scrolling view that allows the user to browse between days of the
 * year.
 */
open class CalendarView : LinearLayout {

    /**
     * Top of the calendar view layout, the week days list
     */
    private var mDayNamesHeader: LinearLayout? = null
    /**
     * Part of the calendar view layout always visible, the weeks list
     */
    var listViewWeeks: WeekListView? = null
        private set
    /**
     * The adapter for the weeks list
     */
    private var mWeeksAdapter: WeeksAdapter? = null
    /**
     * The current highlighted day in blue
     */
    // endregion

    var selectedDay: IDayItem? = null
    /**
     * The current row displayed at top of the list
     */
    private var mCurrentListPosition: Int = 0

    // region Constructors

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_calendar, this, true)

        setOrientation(VERTICAL)
    }

    // region Class - View

    override fun onFinishInflate() {
        super.onFinishInflate()

        mDayNamesHeader = findViewById(R.id.cal_day_names) as LinearLayout
        listViewWeeks = findViewById(R.id.list_week) as WeekListView
        listViewWeeks!!.layoutManager = LinearLayoutManager(getContext())
        listViewWeeks!!.setHasFixedSize(true)
        listViewWeeks!!.itemAnimator = null
        listViewWeeks!!.setSnapEnabled(true)

        // display only two visible rows on the calendar view
        viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override
                    fun onGlobalLayout() {
                        if (getWidth() !== 0 && getHeight() !== 0) {
                            collapseCalendarView()
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                    }
                }
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        BusProvider.instance.toObserverable()
                .subscribe({ event ->
                    if (event is Events.CalendarScrolledEvent) {
                        expandCalendarView()
                    } else if (event is Events.AgendaListViewTouchedEvent) {
                        collapseCalendarView()
                    } else if (event is Events.DayClickedEvent) {
                        val clickedEvent = event
                        updateSelectedDay(clickedEvent.calendar, clickedEvent.day)
                    }
                })
    }

    // endregion

    // region Public methods

    fun init(calendarManager: CalendarManager, monthColor: Int, selectedDayTextColor: Int, currentDayTextColor: Int, pastDayTextColor: Int, circleColor: Drawable?
             , cellPastBackgroundColor: Int, cellNowadaysDayColor: Int) {
        val today = calendarManager.today
        val locale = calendarManager.locale
        val weekDayFormatter = calendarManager.weekdayFormatter
        val weeks = calendarManager.weeks

        setUpHeader(today, weekDayFormatter!!, locale!!)
        setUpAdapter(today, weeks, monthColor, selectedDayTextColor, currentDayTextColor, pastDayTextColor, circleColor
        , cellPastBackgroundColor, cellNowadaysDayColor)
        scrollToDate(today, weeks)
    }

    /**
     * Fired when the Agenda list view changes section.

     * @param calendarEvent The event for the selected position in the agenda listview.
     */
    fun scrollToDate(calendarEvent: CalendarEvent) {
        listViewWeeks!!.post({ scrollToPosition(updateSelectedDay(calendarEvent.instanceDay, calendarEvent.dayReference)) })
    }

    fun scrollToDate(today: Calendar, weeks: List<IWeekItem>) {
        var currentWeekIndex: Integer? = null

        for (c in 0..weeks.size - 1) {
            if (DateHelper.sameWeek(today, weeks[c])) {
                currentWeekIndex = c as Integer
                break
            }
        }

        if (currentWeekIndex != null) {
            val finalCurrentWeekIndex = currentWeekIndex
            listViewWeeks!!.post({ scrollToPosition(finalCurrentWeekIndex!!.toInt()) })
        }
    }

    override fun setBackgroundColor(color: Int) {
        listViewWeeks!!.setBackgroundColor(color)
    }

    // endregion

    // region Private methods

    private fun scrollToPosition(targetPosition: Int) {
        val layoutManager = listViewWeeks!!.getLayoutManager() as LinearLayoutManager
        layoutManager.scrollToPosition(targetPosition)
    }

    private fun updateItemAtPosition(position: Int) {
        val weeksAdapter = listViewWeeks!!.getAdapter() as WeeksAdapter
        weeksAdapter.notifyItemChanged(position)
    }

    /**
     * Creates a new adapter if necessary and sets up its parameters.
     */
    private fun setUpAdapter(today: Calendar, weeks: List<IWeekItem>, monthColor: Int, selectedDayTextColor: Int, currentDayTextColor: Int, pastDayTextColor: Int,
                             circleBackgroundColor: Drawable?, cellPastBackgroundColor: Int, cellNowadaysDayColor: Int) {
        if (mWeeksAdapter == null) {
            Log.d(LOG_TAG, "Setting adapter with today's calendar: " + today.toString())
            mWeeksAdapter = WeeksAdapter(context, today, monthColor, selectedDayTextColor, currentDayTextColor, pastDayTextColor,
                    circleBackgroundColor, cellPastBackgroundColor, cellNowadaysDayColor )
            listViewWeeks!!.setAdapter(mWeeksAdapter)
        }
        mWeeksAdapter!!.updateWeeksItems(weeks)
    }

    private fun setUpHeader(today: Calendar, weekDayFormatter: SimpleDateFormat, locale: Locale) {
        val daysPerWeek = 7
        val dayLabels = arrayOfNulls<String>(daysPerWeek)
        val cal = Calendar.getInstance(CalendarManager.getInstance(context).locale)
        cal.time = today.time
        val firstDayOfWeek = cal.firstDayOfWeek
        for (count in 0..6) {
            cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + count)
            //      if (locale.getLanguage().equals("en")) {
            dayLabels[count] = weekDayFormatter.format(cal.time).toUpperCase(locale)
            //      } else {
            //        dayLabels[count] = weekDayFormatter.format(cal.getTime());
            //      }
        }

        for (i in 0..mDayNamesHeader!!.childCount - 1) {
            val txtDay = mDayNamesHeader!!.getChildAt(i) as TextView
            txtDay.text = dayLabels[i]
        }
    }

    private fun expandCalendarView() {
        val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = (getResources().getDimension(R.dimen.calendar_header_height) + 5 * getResources().getDimension(R.dimen.day_cell_height)).toInt()
        setLayoutParams(layoutParams)
    }

    private fun collapseCalendarView() {
        val layoutParams = getLayoutParams() as ViewGroup.MarginLayoutParams
        layoutParams.height = (getResources().getDimension(R.dimen.calendar_header_height) + 2 * getResources().getDimension(R.dimen.day_cell_height)).toInt()
        setLayoutParams(layoutParams)
    }

    /**
     * Update a selected cell day item.

     * @param calendar The Calendar instance of the day selected.
     * *
     * @param dayItem  The DayItem information held by the cell item.
     * *
     * @return The selected row of the weeks list, to be updated.
     */
    fun updateSelectedDay(calendar: Calendar, dayItem: IDayItem): Int {
        var currentWeekIndex: Integer? = null

        // update highlighted/selected day
        if (!dayItem.equals(selectedDay)) {
            dayItem.isSelected = true
            if (selectedDay != null) {
                selectedDay!!.isSelected = false
            }
            selectedDay = dayItem
        }

        for (c in 0..CalendarManager.instance!!.weeks.size - 1) {
            if (DateHelper.sameWeek(calendar, CalendarManager.instance!!.weeks[c])) {
                currentWeekIndex = c as Integer
                break
            }
        }

        if (currentWeekIndex != null) {
            // highlighted day has changed, update the rows concerned
            if (!currentWeekIndex.equals( mCurrentListPosition)) {
                updateItemAtPosition(mCurrentListPosition)
            }
            mCurrentListPosition = currentWeekIndex!!.toInt()
            updateItemAtPosition(currentWeekIndex!!.toInt())
        }

        CalendarManager.instance!!.currentSelectedDay = calendar
        CalendarManager.instance!!.currentListPosition = mCurrentListPosition

        return mCurrentListPosition
    }

    companion object {

        private val LOG_TAG = CalendarView::class.java!!.getSimpleName()
    }

    // endregion
}
