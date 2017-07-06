package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

interface CalendarEvent<T> {

    var event: T

    var startTime: Calendar

    var endTime: Calendar

    val instanceDay: Calendar

    fun setEventInstanceDay(instanceDay: Calendar) : CalendarEvent<Any>

    var dayReference: IDayItem

    var weekReference: IWeekItem

    fun copy(): CalendarEvent<Any>

    fun hasEvent(): Boolean

}
