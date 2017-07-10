package com.ognev.kotlin.agendacalendarview.agenda

import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.EventAdapter
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for the agenda, implements StickyListHeadersAdapter.
 * Days as sections and CalendarEvents as list items.
 */
class AgendaAdapter: BaseAdapter(), StickyListHeadersAdapter {

    override fun getCount(): Int {
        return CalendarManager.instance!!.events.size
    }

    private val mRenderers = ArrayList<EventAdapter<CalendarEvent>>()

    fun updateEvents() {
        notifyDataSetChanged()
    }

    override
    fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup): View {
        var agendaHeaderView = convertView
        var eventAdapter: EventAdapter<CalendarEvent> ? = mRenderers[0]

        if (agendaHeaderView == null) {
            agendaHeaderView = LayoutInflater.from(parent.context).
                    inflate(eventAdapter!!.getHeaderLayout(), parent, false)
        }

        if (!CalendarManager.instance!!.events.isEmpty()) {
            eventAdapter!!.getHeaderItemView(agendaHeaderView!!, getItem(position).instanceDay)
        }

        return agendaHeaderView!!
    }



    override
    fun getHeaderId(position: Int): Long {
        return (if (CalendarManager.instance!!.events.isEmpty()) 0
        else CalendarManager.instance!!.events[position].instanceDay.timeInMillis).toLong()
    }

    override
    fun getItem(position: Int): CalendarEvent {
        return CalendarManager.instance!!.events[position]
    }

    override
    fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var eventAdapter: EventAdapter<CalendarEvent> = mRenderers[0]
        val event = getItem(position)

        // Search for the correct event renderer
        for (renderer in mRenderers) {
            if (event.javaClass.isAssignableFrom(renderer.renderType)) {
                eventAdapter = renderer
                break
            }
        }

        convertView = LayoutInflater.from(parent.context)
                .inflate(eventAdapter.getEventLayout(CalendarManager.
                        instance!!.events[position].hasEvent()), parent, false)

        eventAdapter.getEventItemView(convertView, event, position)

        return convertView
    }

    fun addEventRenderer(@NonNull adapter: EventAdapter<CalendarEvent>) {
        mRenderers.add(adapter)
    }

}
