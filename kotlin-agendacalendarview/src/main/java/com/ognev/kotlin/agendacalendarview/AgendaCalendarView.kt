package com.ognev.kotlin.agendacalendarview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.annotation.NonNull
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.agenda.AgendaView
import com.ognev.kotlin.agendacalendarview.calendar.CalendarView
import com.ognev.kotlin.agendacalendarview.models.BaseCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.render.DefaultEventRenderer
import com.ognev.kotlin.agendacalendarview.render.EventRenderer
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.Events
import com.ognev.kotlin.agendacalendarview.utils.ListViewScrollTracker
import com.ognev.kotlin.agendacalendarview.R
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

import java.util.Calendar
import java.util.Locale

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView : FrameLayout, StickyListHeadersListView.OnStickyHeaderChangedListener {

    private var mCalendarView: CalendarView? = null
    lateinit var agendaView: AgendaView
        private set
    //  private FloatingActionButton mFloatingActionButton;

    private var mAgendaCurrentDayTextColor: Int = 0
    private var mCalendarHeaderColor: Int = 0
    private var mCalendarBackgroundColor: Int = 0
    private var mCalendarDayTextColor: Int = 0
    private var mCalendarPastDayTextColor: Int = 0
    private var mCalendarCurrentDayColor: Int = 0
    private var mFabColor: Int = 0
    private var mCalendarPickerController: CalendarPickerController? = null

    private val mAgendaListViewScrollTracker: ListViewScrollTracker? = null
    private val mAgendaScrollListener = object : AbsListView.OnScrollListener {
        internal var mCurrentAngle: Int = 0
        internal var mMaxAngle = 85

        override
        fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

        }

        override
        fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
            val scrollY = mAgendaListViewScrollTracker!!.calculateScrollY(firstVisibleItem, visibleItemCount)
            //      if (scrollY != 0) {
            //        mFloatingActionButton.show();
            //      }
            //      Log.d(LOG_TAG, String.format("Agenda listView scrollY: %d", scrollY));
            var toAngle = scrollY / 100
            if (toAngle > mMaxAngle) {
                toAngle = mMaxAngle
            } else if (toAngle < -mMaxAngle) {
                toAngle = -mMaxAngle
            }
            //      RotateAnimation rotate = new RotateAnimation(mCurrentAngle, toAngle, mFloatingActionButton.getWidth() / 2, mFloatingActionButton.getHeight() / 2);
            //      rotate.setFillAfter(true);
            mCurrentAngle = toAngle

            //      mFloatingActionButton.startAnimation(rotate);
        }
    }
    private var visitClickListener: OnClickListener? = null

    // region Constructors

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorOptionsView, 0, 0)
        mAgendaCurrentDayTextColor = a.getColor(R.styleable.ColorOptionsView_agendaCurrentDayTextColor, getResources().getColor(R.color.theme_primary))
        mCalendarHeaderColor = a.getColor(R.styleable.ColorOptionsView_calendarHeaderColor, getResources().getColor(R.color.theme_primary))
        mCalendarBackgroundColor = a.getColor(R.styleable.ColorOptionsView_calendarColor, getResources().getColor(R.color.theme_primary))
        mCalendarDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarDayTextColor, getResources().getColor(R.color.theme_text_icons))
        mCalendarCurrentDayColor = a.getColor(R.styleable.ColorOptionsView_calendarCurrentDayTextColor, getResources().getColor(R.color.calendar_text_current_day))
        mCalendarPastDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarPastDayTextColor, getResources().getColor(R.color.theme_light_primary))
        mFabColor = a.getColor(R.styleable.ColorOptionsView_fabColor, getResources().getColor(R.color.theme_accent))

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_agendacalendar, this, true)

        setAlpha(0f)
    }

    // endregion

    // region Class - View

    override
    protected fun onFinishInflate() {
        super.onFinishInflate()
        mCalendarView = findViewById(R.id.calendar_view) as CalendarView
        agendaView = findViewById(R.id.agenda_view) as AgendaView
        //    mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        //    ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{mFabColor});
        //    mFloatingActionButton.setBackgroundTintList(csl);

        mCalendarView!!.findViewById(R.id.cal_day_names).setBackgroundColor(mCalendarHeaderColor)
        mCalendarView!!.findViewById(R.id.list_week).setBackgroundColor(mCalendarBackgroundColor)

        agendaView!!.agendaListView!!.setOnItemClickListener({ parent: AdapterView<*>, view: View, position: Int, id: Long -> mCalendarPickerController!!.onEventSelected(CalendarManager.instance!!.events[position]) })

        BusProvider.instance.toObserverable()
                .subscribe({ event ->
                    if (event is Events.DayClickedEvent) {
                        mCalendarPickerController!!.onDaySelected((event as Events.DayClickedEvent).day)
                    } else if (event is Events.EventsFetched) {
                        val alphaAnimation = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 1f).setDuration(500)
                        alphaAnimation.addListener(object : Animator.AnimatorListener {
                            override
                            fun onAnimationStart(animation: Animator) {

                            }

                            override
                            fun onAnimationEnd(animation: Animator) {
                                //                long fabAnimationDelay = 500;
                                //                // Just after setting the alpha from this view to 1, we hide the fab.
                                //                // It will reappear as soon as the user is scrolling the Agenda view.
                                //                new Handler().postDelayed(() -> {
                                ////                  mFloatingActionButton.hide();
                                //                  mAgendaListViewScrollTracker = new ListViewScrollTracker(mAgendaView.getAgendaListView());
                                //                  mAgendaView.getAgendaListView().setOnScrollListener(mAgendaScrollListener);
                                ////                  mFloatingActionButton.setOnClickListener((v) -> {
                                //                    mAgendaView.translateList(0);
                                //                    mAgendaView.getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());
                                ////                    new Handler().postDelayed(() -> mFloatingActionButton.hide(), fabAnimationDelay);
                                ////                  });
                                //                }, fabAnimationDelay);
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

    // endregion

    // region Interface - StickyListHeadersListView.OnStickyHeaderChangedListener

    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) {
        //    Log.d(LOG_TAG, String.format("onStickyHeaderChanged, position = %d, headerId = %d", position, headerId));

        if (CalendarManager.instance!!.events.size > 0) {
            val event = CalendarManager.instance!!.events[position]
            if (event != null) {
                mCalendarView!!.scrollToDate(event)
                mCalendarPickerController!!.onScrollToDate(event!!.instanceDay)
            }
        }
    }

    // endregion

    // region Public methods

    fun init(eventList: MutableList<CalendarEvent>, minDate: Calendar, maxDate: Calendar, locale: Locale, calendarPickerController: CalendarPickerController) {
        mCalendarPickerController = calendarPickerController

        CalendarManager.getInstance(getContext()).buildCal(minDate, maxDate)

        // Feed our views with weeks list and events
        mCalendarView!!.init(CalendarManager.getInstance(getContext()), mCalendarDayTextColor, mCalendarCurrentDayColor, mCalendarPastDayTextColor)

        // Load agenda events and scroll to current day
        val agendaAdapter = AgendaAdapter(mAgendaCurrentDayTextColor)
        agendaView!!.agendaListView!!.setAdapter(agendaAdapter)
        agendaView!!.agendaListView!!.setOnStickyHeaderChangedListener(this)

        CalendarManager.instance!!.loadEvents(eventList, BaseCalendarEvent())
        BusProvider.instance.send(Events.EventsFetched())
        //    Log.d(LOG_TAG, "CalendarEventTask finished");

        // add default event renderer
        addEventRenderer(DefaultEventRenderer())
    }

    fun init(lWeeks: MutableList<IWeekItem>, lDays: MutableList<IDayItem>, lEvents: MutableList<CalendarEvent>, calendarPickerController: CalendarPickerController) {
        mCalendarPickerController = calendarPickerController

        CalendarManager.getInstance(getContext()).loadCal(lWeeks, lDays, lEvents)

        // Feed our views with weeks MutableList and events
        mCalendarView!!.init(CalendarManager.getInstance(getContext()), mCalendarDayTextColor, mCalendarCurrentDayColor, mCalendarPastDayTextColor)

        // Load agenda events and scroll to current day
        val agendaAdapter = AgendaAdapter(mAgendaCurrentDayTextColor)
        agendaView.agendaListView.adapter = agendaAdapter
        agendaView.agendaListView.setOnStickyHeaderChangedListener(this)

        // notify that actually everything is loaded
        BusProvider.instance.send(Events.EventsFetched())
        //    Log.d(LOG_TAG, "CalendarEventTask finished");

        // add default event renderer
        addEventRenderer(DefaultEventRenderer())
    }

    fun addEventRenderer(@NonNull renderer: EventRenderer<*>) {
        val adapter = agendaView!!.agendaListView!!.getAdapter() as AgendaAdapter
        adapter.addEventRenderer(renderer as EventRenderer<CalendarEvent>)
    }

    fun setOnVisitClickListener(visitClickListener: OnClickListener) {
        this.visitClickListener = visitClickListener
    }

    companion object {

        private val LOG_TAG = AgendaCalendarView::class.java!!.getSimpleName()
    }

    // endregion
}
