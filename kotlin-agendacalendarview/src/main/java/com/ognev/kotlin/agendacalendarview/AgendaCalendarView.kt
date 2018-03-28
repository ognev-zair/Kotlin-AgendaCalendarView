package com.ognev.kotlin.agendacalendarview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.agenda.AgendaView
import com.ognev.kotlin.agendacalendarview.calendar.CalendarView
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.render.DefaultEventAdapter
import com.ognev.kotlin.agendacalendarview.render.EventAdapter
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.Events
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView : FrameLayout, StickyListHeadersListView.OnStickyHeaderChangedListener {

    private var mCalendarView: CalendarView? = null
    lateinit var agendaView: AgendaView
        private set
    //  private FloatingActionButton mFloatingActionButton;

    private var mCalendarHeaderColor: Int = 0
    private var monthCalendarColor: Int = 0
    private var selectedDayTextColor: Int = 0
    private var mCalendarPastDayTextColor: Int = 0
    private var circleBackgroundColor: Drawable? = null
    private var cellPastBackgroundColor: Int = 0
    private var cellNowadaysDayColor: Int = 0
    private var mCalendarCurrentDayColor: Int = 0
    private var mFabColor: Int = 0
    private var calendarController: CalendarController? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorOptionsView, 0, 0)
        mCalendarHeaderColor = a.getColor(R.styleable.ColorOptionsView_calendarHeaderColor, ContextCompat.getColor(context, R.color.theme_primary))
        monthCalendarColor = a.getColor(R.styleable.ColorOptionsView_calendarMonthTextColor, ContextCompat.getColor(context, R.color.theme_text_icons))
        selectedDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarSelectedDayTextColor, ContextCompat.getColor(context, R.color.theme_text_icons))
        mCalendarCurrentDayColor = a.getColor(R.styleable.ColorOptionsView_calendarCurrentDayTextColor, ContextCompat.getColor(context, R.color.calendar_text_current_day))
        mCalendarPastDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarPastDayTextColor, ContextCompat.getColor(context, R.color.theme_light_primary))
        circleBackgroundColor = a.getDrawable(R.styleable.ColorOptionsView_circleBackgroundColor)
        cellNowadaysDayColor = a.getColor(R.styleable.ColorOptionsView_cellNowadaysDayColor, ContextCompat.getColor(context, R.color.white))
        cellPastBackgroundColor = a.getColor(R.styleable.ColorOptionsView_cellPastBackgroundColor, ContextCompat.getColor(context, R.color.calendar_past_days_bg))
        mFabColor = a.getColor(R.styleable.ColorOptionsView_fabColor, ContextCompat.getColor(context, R.color.theme_accent))

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_agendacalendar, this, true)

        setAlpha(0f)
    }


    override
    protected fun onFinishInflate() {
        super.onFinishInflate()
        mCalendarView = findViewById(R.id.calendar_view) as CalendarView
        agendaView = findViewById(R.id.agenda_view) as AgendaView
        mCalendarView!!.findViewById(R.id.cal_day_names).setBackgroundColor(mCalendarHeaderColor)

//        agendaView.agendaListView.setOnItemClickListener({ parent: AdapterView<*>, view: View, position: Int,
//                                                           id: Long ->
//            calendarController!!.onEventSelected(CalendarManager.instance!!.events[position]) })

        BusProvider.instance.toObserverable()
                .subscribe({ event ->
                    if (event is Events.DayClickedEvent) {
                        calendarController!!.onDaySelected((event).day)
                    } else if (event is Events.EventsFetched) {
                        val alphaAnimation = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 1f).setDuration(500)
                        alphaAnimation.addListener(object : Animator.AnimatorListener {
                            override
                            fun onAnimationStart(animation: Animator) {

                            }

                            override
                            fun onAnimationEnd(animation: Animator) {

                            }

                            override
                            fun onAnimationCancel(animation: Animator) {

                            }

                            override
                            fun onAnimationRepeat(animation: Animator) {

                            }
                        })
                        alphaAnimation.start()
                    }
                })
    }


    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) {

        if (CalendarManager.instance!!.events.size > 0) {
            val event = CalendarManager.instance!!.events[position]
            if (event != null) {
                mCalendarView!!.scrollToDate(event)
                calendarController!!.onScrollToDate(event.instanceDay)
            }
        }
    }


    fun setCallbacks(calendarController: CalendarController) {
        this.calendarController = calendarController
    }

    fun init(lWeeks: MutableList<IWeekItem>, lDays: MutableList<IDayItem>, lEvents: MutableList<CalendarEvent>,
             sampleAgendaAdapter: DefaultEventAdapter) {

        CalendarManager.getInstance(context).loadCal(lWeeks, lDays, lEvents)

        // Feed our views with weeks MutableList and events
        mCalendarView!!.init(CalendarManager.getInstance(context), monthCalendarColor, selectedDayTextColor, mCalendarCurrentDayColor, mCalendarPastDayTextColor,
                circleBackgroundColor, cellPastBackgroundColor, cellNowadaysDayColor)

        // Load agenda events and scroll to current day
        val agendaAdapter = AgendaAdapter()
        agendaView.agendaListView.adapter = agendaAdapter
        agendaView.agendaListView.setOnStickyHeaderChangedListener(this)

        // notify that actually everything is loaded
        BusProvider.instance.send(Events.EventsFetched())
        //    Log.d(LOG_TAG, "CalendarEventTask finished");

        // add default event renderer
        addEventRenderer(sampleAgendaAdapter)
    }

    fun addEventRenderer(@NonNull eventAdapter: EventAdapter<*>) {
        val agendaAdapter = agendaView.agendaListView.adapter as AgendaAdapter
        agendaAdapter.addEventRenderer(eventAdapter as EventAdapter<CalendarEvent>)
    }

    fun showProgress() {
        (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isRefreshing = true
    }

    fun hideProgress() {
        (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isRefreshing = false
    }

    fun isCalendarLoading(): Boolean {
        return (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isRefreshing
    }

}
