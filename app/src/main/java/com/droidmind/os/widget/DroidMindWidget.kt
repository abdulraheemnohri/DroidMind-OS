package com.droidmind.os.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.droidmind.os.R

class DroidMindWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.droidmind_widget)
            views.setTextViewText(R.id.widget_title, "DroidMind OS")
            views.setTextViewText(R.id.widget_status, "AI Protection: Active")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
