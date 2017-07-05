package com.ognev.kotlin.agendacalendarview.utils

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import com.ognev.kotlin.agendacalendarview.R

object VisitColorUtil {

    @DrawableRes fun getBackgroundDrawable(color: String): Int {
        var back = R.drawable.reception_gradient_azure

        if ("#53b428".equals(color)) {
            back = R.drawable.reception_gradient_grass
        }

        if ("#9c56b8".equals(color)) {
            back = R.drawable.reception_gradient_amethyst
        }

        if ("#e4b501".equals(color)) {
            back = R.drawable.reception_gradient_golden
        }

        if ("#b29077".equals(color)) {
            back = R.drawable.reception_gradient_pale_brown
        }

        if ("#e14e6d".equals(color)) {
            back = R.drawable.reception_gradient_darkish_pink
        }

        if ("#f58c27".equals(color)) {
            back = R.drawable.reception_gradient_dusty_orange
        }

        return back
    }

    /**

     * @see kz.azimut.doctorlinkkz.general.models.Visit
     * public static final int NOT_CONFIRMED = 0;
     * public static final int IN_WAIT = 1;
     * public static final int CLOSE = 2;
     * public static final int CANCELED = 3;
     * public static final int IN_PROGRESS= 4;
     * public static final int FINISHED= 5;

     * @param context
     * *
     * @param status
     * *
     * @return
     */
    @ColorInt fun getBackgroundColor(context: Context, status: Int): Int {
        var colorInt = 0
        when (status) {
            0 -> colorInt = Color.parseColor("#1ae3913b")

            1, 4 -> colorInt = Color.parseColor("#1a417db6")

            5 -> colorInt = Color.parseColor("#1a1ea065")
        }//        gradientDrawable = new GradientDrawable(
        //            GradientDrawable.Orientation.TOP_BOTTOM,
        //            new int[] {Color.parseColor("#d1effe"), Color.parseColor("#1a417db6")});
        //        gradientDrawable.setCornerRadius(0f);
        //        gradientDrawable = new GradientDrawable(
        //            GradientDrawable.Orientation.TOP_BOTTOM,
        //            new int[] {Color.parseColor("#d4e6f7"), Color.parseColor("#1ae3913b")});
        //        gradientDrawable.setCornerRadius(0f);
        //        gradientDrawable = new GradientDrawable(
        //            GradientDrawable.Orientation.TOP_BOTTOM,
        //            new int[] {Color.parseColor("#d4e6f7"), Color.parseColor("#1a1ea065")});
        //        gradientDrawable.setCornerRadius(0f);

        return colorInt
    }

    //  public static int getNoReceptionGradient(Context context) {
    //    GradientDrawable gd = new GradientDrawable(
    //        GradientDrawable.Orientation.TOP_BOTTOM,
    //        new int[] {Color.parseColor("#1a7685a1"), Color.parseColor("#1a7685a1")});
    //    gd.setCornerRadius(0f);
    //
    //    return gd;
    //  }
}
