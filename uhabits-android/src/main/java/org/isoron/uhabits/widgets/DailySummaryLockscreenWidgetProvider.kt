package org.isoron.uhabits.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import org.isoron.uhabits.HabitsApplication

class DailySummaryLockscreenWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val app = context.applicationContext as HabitsApplication
        val habitList = app.component.habitList

        for (id in appWidgetIds) {
            val widget = DailySummaryLockscreenWidget(context, id, habitList)
            val density = context.resources.displayMetrics.density
            val sizePx = (56 * density).toInt()
            val dims = WidgetDimensions(sizePx, sizePx, sizePx, sizePx)
            widget.setDimensions(dims)
            appWidgetManager.updateAppWidget(id, widget.portraitRemoteViews)
        }
    }
}