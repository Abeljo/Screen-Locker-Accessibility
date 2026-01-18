package com.abeljo.screenlocker

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast

/**
 * LockScreenWidget - Home screen widget that locks the screen with one tap.
 * 
 * How it works:
 * 1. Widget displays a simple lock button
 * 2. When tapped, it sends a broadcast with action "LOCK_SCREEN"
 * 3. onReceive() handles the broadcast and calls ScreenLockService.lockScreen()
 * 
 * The widget is a 1x1 cell with just an icon - minimal and fast.
 */
class LockScreenWidget : AppWidgetProvider() {

    companion object {
        // Custom action for the lock screen broadcast
        const val ACTION_LOCK_SCREEN = "com.abeljo.screenlocker.LOCK_SCREEN"
    }

    /**
     * Called when the widget needs to be updated.
     * Sets up the click pending intent for each widget instance.
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update each widget instance
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Called when any broadcast is received.
     * Handles both widget updates and our custom lock action.
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Handle our custom lock screen action
        if (intent.action == ACTION_LOCK_SCREEN) {
            if (ScreenLockService.isServiceEnabled()) {
                ScreenLockService.lockScreen()
            } else {
                // Service not enabled - show toast and open settings
                Toast.makeText(
                    context,
                    R.string.service_not_enabled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Updates a single widget instance with the lock button.
     */
    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Create the RemoteViews for the widget layout
        val views = RemoteViews(context.packageName, R.layout.widget_lock_screen)

        // Create a pending intent that broadcasts our lock action
        val lockIntent = Intent(context, LockScreenWidget::class.java).apply {
            action = ACTION_LOCK_SCREEN
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            lockIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the click listener on the widget root
        views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent)

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
