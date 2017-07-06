package com.ognev.kotlin.agendacalendarview.agenda

import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ognev.kotlin.agendacalendarview.CalendarItemLayout
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.DefaultEventRenderer
import com.ognev.kotlin.agendacalendarview.render.EventRenderer
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

import java.util.ArrayList

/**
 * Adapter for the agenda, implements StickyListHeadersAdapter.
 * Days as sections and CalendarEvents as list items.
 */
class AgendaAdapter
// region Constructor

(private val mCurrentDayColor: Int) : BaseAdapter(), StickyListHeadersAdapter {
    override fun getCount(): Int {
        return CalendarManager.instance!!.events.size
    }

    private val mRenderers = ArrayList<EventRenderer<CalendarEvent<Any>>>()
    private var visitClickListener: View.OnClickListener? = null
    private lateinit var calendarItemLayout: CalendarItemLayout

    // endregion

    // region Public methods

    fun updateEvents() {
        //    CalendarManager.getInstance().getEvents().clear();

        notifyDataSetChanged()
    }

    //
    //  public void updateExistedEvents(List<CalendarEvent> events) {
    ////    CalendarManager.getInstance().getEvents().addAll(events);
    //    CalendarManager.getInstance().getEvents().addAll(events);
    //    notifyDataSetChanged();
    //
    //  }

    // endregion

    // region Interface - StickyListHeadersAdapter

    override
    fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup): View {
        var agendaHeaderView: AgendaHeaderView? = convertView as AgendaHeaderView?
        if (agendaHeaderView == null) {
            agendaHeaderView = AgendaHeaderView.inflate(parent)
        }
        if (!CalendarManager.instance!!.events.isEmpty())
            agendaHeaderView.setDay(getItem(position).instanceDay, mCurrentDayColor)
        return agendaHeaderView
    }

    override
    fun getHeaderId(position: Int): Long {

        return (if (CalendarManager.instance!!.events.isEmpty()) 0 else CalendarManager.instance!!.events.get(position).instanceDay.timeInMillis).toLong()
    }

    override
    fun getItem(position: Int): CalendarEvent<Any> {
        return CalendarManager.instance!!.events[position]
    }

    override
    fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var eventRenderer: EventRenderer<CalendarEvent<Any>> = mRenderers[0]
        val event = getItem(position)

        // Search for the correct event renderer
        for (renderer in mRenderers) {
            if (event.javaClass.isAssignableFrom(renderer.renderType)) {
                eventRenderer = renderer
                break
            }
        }
        convertView = LayoutInflater.from(parent.context)
                .inflate(eventRenderer.getEventLayout(CalendarManager.instance!!.events[position].hasEvent()), parent, false)


        eventRenderer.render(convertView, event)

        convertView.tag = position
        convertView.setOnClickListener(visitClickListener)



        return convertView
    }

    fun setCalendarLayouts(calendarItemLayout: CalendarItemLayout) {
        this.calendarItemLayout = calendarItemLayout;
    }

    fun setOnVisitClickListener(visitClickListener: View.OnClickListener) {
        this.visitClickListener = visitClickListener
    }

    fun addEventRenderer(@NonNull renderer: EventRenderer<CalendarEvent<Any>>) {
        mRenderers.add(renderer)
    }

    // endregion
}
