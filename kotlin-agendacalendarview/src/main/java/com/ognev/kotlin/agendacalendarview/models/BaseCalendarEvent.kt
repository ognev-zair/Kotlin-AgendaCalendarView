package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

/**
 * Event model class containing the information to be displayed on the agenda view.
 */
class BaseCalendarEvent : CalendarEvent {
    override val instanceDay: Calendar
        get() = mInstanceDay!!

    override var status: Int = 0
        private set
    /**
     * Id of the event.
     */
    override var id: Long = 0
    /**
     * Color to be displayed in the agenda view.
     */
    // endregion

    // region Getters/Setters

    var color: String? = null
    /**
     * Title of the event.
     */
    override lateinit var name: String
        private set
    /**
     * Description of the event.
     */
    var description: String? = null
    /**
     * Where the event takes place.
     */
    override lateinit var time: String
        private set
    /**
     * Calendar instance helping sorting the events per section in the agenda view.
     */
    private var mInstanceDay: Calendar? = null
    /**
     * Start time of the event.
     */
    override lateinit var startTime: Calendar
    /**
     * End time of the event.
     */
    override lateinit var endTime: Calendar
    /**
     * Indicates if the event lasts all day.
     */
    var isAllDay: Boolean = false
    /**
     * Tells if this BaseCalendarEvent instance is used as a placeholder in the agenda view, if there's
     * no event for that day.
     */
    override var isPlaceholder: Boolean = false
    /**
     * Tells if this BaseCalendarEvent instance is used as a forecast information holder in the agenda
     * view.
     */
    var isWeather: Boolean = false
    /**
     * Duration of the event.
     */
    var duration: String? = null
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
    var weatherIcon: String? = null
    /**
     * Temperature value returned by the Dark Sky API.
     */
    var temperature: Double = 0.toDouble()

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
        this.color = color
        this.isAllDay = allDay == 1
        this.duration = duration
        this.name = title
        this.description = description
        this.time = location

        this.startTime = Calendar.getInstance()
        this.startTime!!.setTimeInMillis(dateStart)
        this.endTime = Calendar.getInstance()
        this.endTime!!.setTimeInMillis(dateEnd)
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
        this.description = description
        this.time = location
        this.color = color
        this.startTime = startTime
        this.endTime = endTime
        this.isAllDay = allDay
        this.hasEvent = true
    }

    constructor(name: String, description: String, time: String, color: String, status: Int, id: Long, startTime: Calendar, endTime: Calendar, allDay: Boolean, dayItem: DayItem) {
        this.name = name
        this.description = description
        this.time = time
        this.color = color
        this.status = status
        this.startTime = startTime
        this.endTime = endTime
        this.isAllDay = allDay
        this.id = id
        this.hasEvent = true
        this.dayReference = dayItem
    }

    constructor(title: String, description: String, location: String, color: String, startTime: Calendar, endTime: Calendar, allDay: Boolean, dayItem: DayItem, hasEvent: Boolean) {
        this.name = title
        this.description = description
        this.time = location
        this.color = color
        this.startTime = startTime
        this.endTime = endTime
        this.isAllDay = allDay
        this.hasEvent = hasEvent
        this.dayReference = dayItem
    }

    constructor(calendarEvent: BaseCalendarEvent) {
        this.id = calendarEvent.id
        this.color = calendarEvent.color
        this.isAllDay = calendarEvent.isAllDay
        this.duration = calendarEvent.duration
        this.status = calendarEvent.status
        this.name = calendarEvent.name
        this.description = calendarEvent.description
        this.time = calendarEvent.time
        this.startTime = calendarEvent.startTime
        this.endTime = calendarEvent.endTime
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

    override fun setLocation(mLocation: String) {
        this.time = mLocation
    }

    override fun setTitle(mTitle: String) {
        this.name = mTitle
    }

    fun isPlaceHolder(): Boolean {
        return isPlaceholder
    }

    fun setPlaceHolder(mPlaceHolder: Boolean) {
        this.isPlaceholder = mPlaceHolder
    }

    override
    fun copy(): CalendarEvent {
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
