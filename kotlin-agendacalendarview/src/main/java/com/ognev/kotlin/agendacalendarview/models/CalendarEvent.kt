package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

interface CalendarEvent {

    var event: Any?

    var startTime: Calendar

    var endTime: Calendar

    val instanceDay: Calendar

    fun setEventInstanceDay(instanceDay: Calendar) : CalendarEvent

    var dayReference: IDayItem

    var weekReference: IWeekItem

    fun copy(): CalendarEvent

    fun hasEvent(): Boolean

}
