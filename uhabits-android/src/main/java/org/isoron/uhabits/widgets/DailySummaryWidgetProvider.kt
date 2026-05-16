package org.isoron.uhabits.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import org.isoron.uhabits.HabitsApplication

class DailySummaryWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val app = context.applicationContext as HabitsApplication
        val habitList = app.component.habitList

        for (id in appWidgetIds) {
            val widget = DailySummaryWidget(context, id, habitList)
            val dims = WidgetDimensions(250, 150, 250, 150)
            widget.setDimensions(dims)
            appWidgetManager.updateAppWidget(id, widget.portraitRemoteViews)
        }
    }
}