package com.ognev.kotlin.agendacalendarview.render

import android.support.annotation.LayoutRes
import android.view.View
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent

import java.lang.reflect.ParameterizedType

/**
 * Base class for helping layout rendering
 */
abstract class EventRenderer<T : CalendarEvent> {
    abstract fun render(view: View, event: T)

    @LayoutRes
    abstract fun getEventLayout(b: Boolean): Int

    val renderType: Class<T>
        get() {
            val type = javaClass.genericSuperclass as ParameterizedType
            return type.actualTypeArguments[0] as Class<T>
        }
}
