package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

/**
 * Event model class containing the information to be displayed on the agenda view.
 */
open class BaseCalendarEvent : CalendarEvent<Any> {
    override var isPlaceholder: Boolean = false

    override val time: String = ""

    override fun setLocation(mLocation: String) {

    }

    override lateinit var startTime: Calendar
    override lateinit var endTime: Calendar

    override fun setTitle(mTitle: String) {
    }

    override val status: Int = 0

    override lateinit var event: Any

    override val instanceDay: Calendar
        get() = mInstanceDay!!

    /**
     * Id of the event.
     */
    override var id: Long = 0
    /**
     * Color to be displayed in the agenda view.
     */
    // endregion

    // region Getters/Setters

    /**
     * Title of the event.
     */
    override lateinit var name: String
    /**
     * Description of the event.
     */
    /**
     * Where the event takes place.
     */
    /**
     * Calendar instance helping sorting the events per section in the agenda view.
     */
    private var mInstanceDay: Calendar? = null
    /**
     * Start time of the event.
     */
    /**
     * End time of the event.
     */
    /**
     * Indicates if the event lasts all day.
     */
    /**
     * Tells if this BaseCalendarEvent instance is used as a placeholder in the agenda view, if there's
     * no event for that day.
     */
    /**
     * Tells if this BaseCalendarEvent instance is used as a forecast information holder in the agenda
     * view.
     */
    /**
     * Duration of the event.
     */
    /**
     * References to a DayItem instance for that event, used to link interaction between the
     * calendar view and the agenda view.
     */
    override lateinit var dayReference: IDayItem
    /**
     * References to a WeekItem instance for that event, used to link interaction between the
     * calendar view and the agenda view.
     */
    override lateinit var weekReference: IWeekItem
    /**
     * Weather icon string returned by the Dark Sky API.
     */
    /**
     * Temperature value returned by the Dark Sky API.
     */

    // region Constructor

    private var hasEvent: Boolean = false

    /**
     * Initializes the event

     * @param id          The id of the event.
     * *
     * @param color       The color of the event.
     * *
     * @param title       The title of the event.
     * *
     * @param description The description of the event.
     * *
     * @param location    The location of the event.
     * *
     * @param dateStart   The start date of the event.
     * *
     * @param dateEnd     The end date of the event.
     * *
     * @param allDay      Int that can be equal to 0 or 1.
     * *
     * @param duration    The duration of the event in RFC2445 format.
     */
    constructor(id: Long, color: String, title: String, description: String, location: String, dateStart: Long, dateEnd: Long, allDay: Int, duration: String) {
        this.id = id
        this.name = title
    }

    constructor() {

    }

    /**
     * Initializes the event

     * @param title       The title of the event.
     * *
     * @param description The description of the event.
     * *
     * @param location    The location of the event.
     * *
     * @param color       The color of the event (for display in the app).
     * *
     * @param startTime   The start time of the event.
     * *
     * @param endTime     The end time of the event.
     * *
     * @param allDay      Indicates if the event lasts the whole day.
     */
    constructor(title: String, description: String, location: String, color: String, startTime: Calendar, endTime: Calendar, allDay: Boolean) {
        this.name = title
        this.hasEvent = true
    }

    constructor(name: String, description: String, time: String, color: String, status: Int, id: Long, startTime: Calendar, endTime: Calendar, allDay: Boolean, dayItem: DayItem) {
        this.name = name
        this.id = id
        this.hasEvent = true
        this.dayReference = dayItem
    }

    constructor(title: String, description: String, location: String, color: String, startTime: Calendar, endTime: Calendar, allDay: Boolean, dayItem: DayItem, hasEvent: Boolean) {
        this.name = title
        this.hasEvent = hasEvent
        this.dayReference = dayItem
    }

    constructor(calendarEvent: BaseCalendarEvent) {
        this.id = calendarEvent.id
        this.name = calendarEvent.name
        this.hasEvent = calendarEvent.hasEvent()
    }

    override fun setInstanceDay(mInstanceDay: Calendar): BaseCalendarEvent {
        this.mInstanceDay = mInstanceDay
        this.mInstanceDay!!.set(Calendar.HOUR, 0)
        this.mInstanceDay!!.set(Calendar.MINUTE, 0)
        this.mInstanceDay!!.set(Calendar.SECOND, 0)
        this.mInstanceDay!!.set(Calendar.MILLISECOND, 0)
        this.mInstanceDay!!.set(Calendar.AM_PM, 0)
        return this
    }


    override
    fun copy(): CalendarEvent<Any> {
        return BaseCalendarEvent(this)
    }

    override fun hasEvent(): Boolean {
        return hasEvent
    }

    // endregion

    override
    fun toString(): String {
        return "BaseCalendarEvent{"
                .plus("title='")
                .plus(name)
                .plus(", instanceDay= ")
                .plus(mInstanceDay!!.getTime())
                .plus("}")
    }

}
