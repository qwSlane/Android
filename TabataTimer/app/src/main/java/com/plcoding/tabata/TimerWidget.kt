package com.plcoding.tabata

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_FORWARD
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_NEXT
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_PAUSE
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_PREV
import com.plcoding.tabata.feature_drill.presentation.timer.TimerService
import com.plcoding.tabata.feature_drill.presentation.timer.TimerSettings


/**
 * Implementation of App Widget functionality.
 */
class TimerWidget : AppWidgetProvider() {
    companion object{
        const val INTENT_COMMAND = "Command"
        const val INTENT_COMMAND_FORWARD = "forward"
        const val INTENT_COMMAND_PAUSE = "pause"
        const val INTENT_COMMAND_PREV = "prev"
        const val INTENT_COMMAND_NEXT = "next"


    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//    override fun onReceive(context: Context?, intent: Intent?) {
//        val appWidgetManager = AppWidgetManager.getInstance(context!!.applicationContext)
//        val thisWidget = ComponentName(
//            context!!.applicationContext,
//            TimerWidget::class.java
//        )
//        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
//        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
//            onUpdate(context!!, appWidgetManager, appWidgetIds)
//        }
//    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.timer_widget)
//    views.setTextViewText(R.id.appwidget_text, TimerSettings.currentTime.value.toString())
    // Instruct the widget manager to update the widget

    val forwardIntent = Intent(context, TimerService::class.java).apply {
        putExtra(INTENT_COMMAND, INTENT_COMMAND_PAUSE)
    }
    val forwardPendingIntent = PendingIntent.getService(
        context,
        1,
        forwardIntent,
        0
    )

    views.setOnClickPendingIntent(R.id.b_start_stop, forwardPendingIntent)

    val prevIntent = Intent(context, TimerService::class.java).apply {
        putExtra(INTENT_COMMAND, INTENT_COMMAND_PREV)
    }
    val prevPendingIntent = PendingIntent.getService(
        context,
        1,
        prevIntent,
        0
    )

    views.setOnClickPendingIntent(R.id.b_prev, prevPendingIntent)


    val nextIntent = Intent(context, TimerService::class.java).apply {
        putExtra(INTENT_COMMAND, INTENT_COMMAND_NEXT)
    }
    val nextPendingIntent = PendingIntent.getService(
        context,
        1,
        nextIntent,
        0
    )

    views.setOnClickPendingIntent(R.id.b_next, nextPendingIntent)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}