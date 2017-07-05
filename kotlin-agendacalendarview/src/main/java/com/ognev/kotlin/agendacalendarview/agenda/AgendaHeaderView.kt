package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.utils.DateHelper

import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Header view for the StickyHeaderListView of the agenda view
 */
class AgendaHeaderView : LinearLayout {

    private var format: SimpleDateFormat? = null

    // region Constructors

    constructor(context: Context) : super(context) {
        format = SimpleDateFormat(context.getString(R.string.header_date), CalendarManager.instance!!.locale)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        format = SimpleDateFormat(context.getString(R.string.header_date), CalendarManager.instance!!.locale)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        format = SimpleDateFormat(context.getString(R.string.header_date), CalendarManager.instance!!.locale)
    }

    // endregion

    // region Public methods

    fun setDay(day: Calendar, currentDayTextColor: Int) {
        val txtDayOfMonth = findViewById(R.id.view_agenda_day_of_month) as TextView
        //    TextView txtDayOfWeek = (TextView) findViewById(com.ognev.kotlin.agendacalendarview.R.id.view_agenda_day_of_week);
        //    View circleView = findViewById(com.ognev.kotlin.agendacalendarview.R.id.view_day_circle_selected);

        val today = CalendarManager.instance!!.today

        //    SimpleDateFormat dayWeekFormatter = new SimpleDateFormat(getContext().getString(R.string.header_date), CalendarManager.getInstance().getLocale());

        //    txtDayOfMonth.setTextColor(getResources().getColor(com.ognev.kotlin.agendacalendarview.R.color.calendar_text_default));
        //    txtDayOfWeek.setTextColor(getResources().getColor(com.ognev.kotlin.agendacalendarview.R.color.calendar_text_default));

        if (DateHelper.sameDate(day, today)) {
            txtDayOfMonth.setTextColor(currentDayTextColor)
            //            circleView.setVisibility(VISIBLE);// todo draw background
            //      GradientDrawable drawable = (GradientDrawable) circleView.getBackground();
            //      drawable.setStroke((int) (2 * Resources.getSystem().getDisplayMetrics().density), currentDayTextColor);
        }

        txtDayOfMonth.setText(format!!.format(day.getTime()))
        //    txtDayOfWeek.setText(dayWeekFormatter.format(day.getTime()));
    }

    companion object {

        fun inflate(parent: ViewGroup): AgendaHeaderView {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_agenda_header, parent, false) as AgendaHeaderView
        }
    }

    // endregion
}
