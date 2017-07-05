package com.ognev.kotlin.agendacalendarview.render

import android.graphics.Color
import android.support.annotation.NonNull
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.models.BaseCalendarEvent
import com.ognev.kotlin.agendacalendarview.utils.VisitColorUtil

/**
 * Class helping to inflate our default layout in the AgendaAdapter
 */
class DefaultEventRenderer : EventRenderer<BaseCalendarEvent>() {

    // region class - EventRenderer

    override
    fun render(@NonNull view: View, @NonNull event: BaseCalendarEvent) {
        var time = view.findViewById(R.id.time) as TextView?
        var name = view.findViewById(R.id.name_item_reception) as TextView?
        var descr = view.findViewById(R.id.description_item_reception) as TextView?
        var back = view.findViewById(R.id.layout_item_reception) as RelativeLayout?
        //    ImageView status = (ImageView) view.findViewById(com.ognev.kotlin.agendacalendarview.R.id.status_item_reception);
        var divider = view.findViewById(R.id.divider_item_reception)

        if (time != null) {
            time.setText(event.time)
            name!!.setTextColor(if (event.status === 2) view.getContext().getResources().getColor(R.color.no_reception) else Color.parseColor(event.color))
            descr!!.setTextColor(if (event.status === 2) view.getContext().getResources().getColor(R.color.no_reception) else Color.parseColor(event.color))
            divider.setBackgroundColor(if (event.status === 2) view.getContext().getResources().getColor(R.color.no_reception) else Color.parseColor(event.color))

            back!!.setBackgroundColor(if (event.status === 2) view.getContext().getResources().getColor(R.color.no_reception_transparent) else VisitColorUtil.getBackgroundColor(view.getContext(), event.status))
            //      status.setVisibility(event.getStatus() == 0 ? View.VISIBLE : View.GONE);
            name.setText(if (event.status === 2) view.getContext().getString(R.string.donot_accepting) else event.name)
            descr.setText(if (event.status === 2) "" else event.description)
        }


        //    LinearLayout descriptionContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_description_container);
        //    LinearLayout locationContainer = (LinearLayout) view.findViewById(R.id.view_agenda_event_location_container);

        //    if (name != null) {
        ////        descriptionContainer.setVisibility(View.VISIBLE);
        //      name.setTextColor(view.getResources().getColor(android.R.color.black));
        //
        //      name.setText(event.getTitle());
        //      descr.setText(event.getTime());
        //      if (event.getTime().length() > 0) {
        //        locationContainer.setVisibility(View.VISIBLE);
        //        descr.setText(event.getTime());
        //      } else {
        //        locationContainer.setVisibility(View.GONE);
        //      }
        //
        //      if (event.getTitle().equals(view.getResources().getString(R.string.agenda_event_no_events))) {
        //        name.setTextColor(view.getResources().getColor(android.R.color.black));
        //      } else {
        //        name.setTextColor(view.getResources().getColor(R.color.theme_text_icons));
        //      }
        //      descriptionContainer.setBackgroundColor(event.getColor());
        //      descr.setTextColor(view.getResources().getColor(R.color.theme_text_icons));
        //    }
    }

    override
    fun getEventLayout(b: Boolean): Int {
        return if (b) R.layout.view_agenda_event else R.layout.view_agenda_empty_event
    }

    // endregion
}
