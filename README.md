# Kotlin-AgendaCalendarView
Kotlin-AgendaCalendarView based on [AgendaCalendarView](https://github.com/Tibolte/AgendaCalendarView)

Kotlin-AgendaCalendarView is a awesome calendar widget with a list of events.
Events can be dynamically added to the calendar.
Please feel free to contribute to this project by creating pull request on feauture or bugfix branches.


# Demo: 

![alt text][logo]

[logo]: https://github.com/ognev-zair/Kotlin-AgendaCalendarView/blob/master/calendar.gif


# Import project 

Gradle:
--------

```java
        compile 'com.ognev.kotlin.agendacalendarview:kotlin-agendacalendarview:1.0'
````

Maven:
--------
```java
        <dependency>
          <groupId>com.ognev.kotlin.agendacalendarview</groupId>
          <artifactId>kotlin-agendacalendarview</artifactId>
          <version>1.0</version>
          <type>pom</type>
        </dependency>
````

# Usage

Layout xml file

```java
    <com.ognev.kotlin.agendacalendarview.AgendaCalendarView
        android:id="@+id/agenda_calendar_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        agendaCalendar:calendarCurrentDayTextColor="@color/main_blue"
        agendaCalendar:calendarSelectedDayTextColor="@color/white"
        agendaCalendar:calendarMonthTextColor="@color/black"
        agendaCalendar:calendarHeaderColor="@color/main_blue"
        agendaCalendar:circleBackgroundColor="@drawable/selected_day_background"
        agendaCalendar:calendarPastDayTextColor="@android:color/darker_gray"
        agendaCalendar:cellPastBackgroundColor="@color/calendar_past_days_bg"
        agendaCalendar:cellNowadaysDayColor="@color/white"
        tools:layout_editor_absoluteY="8dp"
        tools:layout_editor_absoluteX="8dp" />
````
Implement your activity/fragment from CalendarController

```java
    fun getEmptyEventLayout(): Int

    fun getEventLayout() : Int

    fun onDaySelected(dayItem: IDayItem)

    fun onScrollToDate(calendar: Calendar)
````
    
Create your Model class:
```java
    class SampleEvent(name: String, description: String) {
        var id: Long = 0
        var name: String = name
        var desciption: String = description
    }
````

Create model wrapper class for calendar widget:

```java    
    class MyCalendarEvent: BaseCalendarEvent {

    override lateinit var startTime: Calendar
    override lateinit var endTime: Calendar
    override var event: Any? = null

    override lateinit var instanceDay: Calendar


    override lateinit var dayReference: IDayItem

    override lateinit var weekReference: IWeekItem

    override fun setEventInstanceDay(instanceDay: Calendar): MyCalendarEvent {
        this.instanceDay = instanceDay
        this.instanceDay.set(Calendar.HOUR, 0)
        this.instanceDay.set(Calendar.MINUTE, 0)
        this.instanceDay.set(Calendar.SECOND, 0)
        this.instanceDay.set(Calendar.MILLISECOND, 0)
        this.instanceDay.set(Calendar.AM_PM, 0)
        return this
    }

    constructor(calendarEvent: CalendarEvent) {
    }


    constructor(startTime: Calendar,
                endTime: Calendar,
                dayItem: DayItem,
                event: SampleEvent?)  {
        this.startTime = startTime
        this.endTime = endTime
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

    override
    fun toString(): String {
        return super.toString()
     }
    }
````

Create adapter:
```java
    class SampleEventAgendaAdapter(var context: Context) : DefaultEventAdapter() {
        private var format: SimpleDateFormat? = null

    init {
        format = SimpleDateFormat(context.getString(com.ognev.kotlin.agendacalendarview.R.string.header_date),
                Locale.ENGLISH)
    }

    override fun getHeaderLayout(): Int {
        return R.layout.view_agenda_header
    }

    override fun getHeaderItemView(view: View, day: Calendar) {
        val txtDayOfMonth = view.findViewById(R.id.view_agenda_day_of_month) as TextView
        val today = CalendarManager.instance!!.today

        if (DateHelper.sameDate(day, today)) {
            txtDayOfMonth.setTextColor(context.resources.getColor(R.color.main_blue))
        } else{
            txtDayOfMonth.setTextColor(context.resources.getColor(R.color.text_light_color))
        }

        txtDayOfMonth.text = format!!.format(day.time)
    }

    override fun getEventItemView(view: View, event: CalendarEvent, position: Int) {
        val myEvent = event as MyCalendarEvent
        val myObject: SampleEvent? = myEvent.event as SampleEvent?

        if(myEvent.hasEvent()) {
            (view.findViewById(R.id.name)
                    as TextView).text = myObject!!.name

            (view.findViewById(R.id.description)
                    as TextView).text = myObject.desciption
        }

        view.setOnClickListener {
            Toast.makeText(view.context, "Item: ".plus(position), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getEventLayout(hasEvent: Boolean): Int {
        return if(hasEvent) R.layout.view_agenda_event else R.layout.view_agenda_empty_event
     }
    }
````

Configure calendar in your code with a CalendarContentManager:
```java
        minDate = Calendar.getInstance()
        maxDate = Calendar.getInstance()
        minDate.add(Calendar.MONTH, -10)
        minDate.add(Calendar.YEAR, -1)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)

        eventList = ArrayList()

        contentManager = CalendarContentManager(this, agenda_calendar_view, SampleEventAgendaAdapter(applicationContext))

        contentManager.locale = Locale.ENGLISH
        contentManager.setDateRange(minDate, maxDate)
````

Add events to calendar from start
 ```java
        contentManager.loadItemsFromStart(eventList!!)
````
Add events to calendar to End of list
```java
        contentManager.loadFromEndCalendar(eventList!!)
````
# Licence
-----------

 Copyright 2017 Ognev Zair

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
