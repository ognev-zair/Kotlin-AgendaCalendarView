package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.models.*
import java.util.*

/**
 * Created by ognev on 7/6/17.
 */

class MyCalendarEvent: BaseCalendarEvent {


    override lateinit var startTime: Calendar
    override lateinit var endTime: Calendar
    override lateinit var event: Any

    override lateinit var instanceDay: Calendar


    override lateinit var dayReference: IDayItem

    override lateinit var weekReference: IWeekItem

    private var hasEvent: Boolean = false


    override fun setEventInstanceDay(instanceDay: Calendar): MyCalendarEvent {
        this.instanceDay = instanceDay
        this.instanceDay!!.set(Calendar.HOUR, 0)
        this.instanceDay!!.set(Calendar.MINUTE, 0)
        this.instanceDay!!.set(Calendar.SECOND, 0)
        this.instanceDay!!.set(Calendar.MILLISECOND, 0)
        this.instanceDay!!.set(Calendar.AM_PM, 0)
        return this
    }

    constructor(calendarEvent: CalendarEvent<Any>) {
        this.hasEvent = calendarEvent.hasEvent()
    }


    constructor(startTime: Calendar, endTime: Calendar,
                dayItem: DayItem,
                hasEvent: Boolean, event: SampleEvent)  {
        this.startTime = startTime
        this.endTime = endTime
        this.hasEvent = hasEvent
        this.dayReference = dayItem
        this.event = event
    }


    override
    fun copy(): MyCalendarEvent {
        return MyCalendarEvent(this)
    }

    override fun hasEvent(): Boolean {
        return event != null
    }

    // endregion

    override
    fun toString(): String {
        return super.toString();
    }


}