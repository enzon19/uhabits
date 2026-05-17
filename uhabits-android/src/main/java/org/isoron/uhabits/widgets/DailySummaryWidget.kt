package org.isoron.uhabits.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.isoron.uhabits.core.models.HabitList
import org.isoron.uhabits.widgets.views.DailySummaryWidgetView

class DailySummaryWidget(
    context: Context,
    id: Int,
    private val habitList: HabitList,
    stacked: Boolean = false
) : BaseWidget(context, id, stacked) {

    override val defaultHeight: Int = 150
    override val defaultWidth: Int = 250

    override fun getOnClickPendingIntent(context: Context): PendingIntent? {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun refreshData(widgetView: View) {
        var total = 0
        var completed = 0

        for (habit in habitList) {
            if (habit.isArchived) continue
            total++
            if (habit.isCompletedToday()) completed++
        }

        val pending = total - completed
        val summaryView = widgetView as DailySummaryWidgetView
        //summaryView.setBackgroundAlpha(preferedBackgroundAlpha)
        summaryView.setPendingCount(pending)
        summaryView.setTotalCount(total)
    }

    override fun buildView(): View {
        return DailySummaryWidgetView(context)
    }
}