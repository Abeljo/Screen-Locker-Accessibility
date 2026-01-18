package com.abeljo.screenlocker

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

/**
 * ScreenLockService - The core of the app.
 * 
 * This AccessibilityService locks the screen using performGlobalAction().
 * 
 * How it works:
 * 1. The user must enable this service in Settings > Accessibility
 * 2. Once enabled, the service runs in the background
 * 3. Other components (MainActivity, Widget, Tile) call lockScreen() via the singleton instance
 * 4. lockScreen() calls performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
 * 
 * Why AccessibilityService?
 * - GLOBAL_ACTION_LOCK_SCREEN (API 28+) is only available to AccessibilityServices
 * - It's the same as pressing the power button once
 * - No Device Admin or special permissions required beyond accessibility access
 */
class ScreenLockService : AccessibilityService() {

    companion object {
        // Singleton instance - other components use this to trigger screen lock
        @Volatile
        var instance: ScreenLockService? = null
            private set

        /**
         * Checks if the service is currently running and ready to lock.
         */
        fun isServiceEnabled(): Boolean = instance != null

        /**
         * Locks the screen if the service is enabled.
         * @return true if lock was triggered, false if service not enabled
         */
        fun lockScreen(): Boolean {
            return instance?.performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN) ?: false
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Store instance so other components can access it
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear instance when service is destroyed
        instance = null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // Clear instance when service is unbound
        instance = null
        return super.onUnbind(intent)
    }

    // Required override - we don't process any accessibility events
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not used - we only use performGlobalAction()
    }

    // Required override - nothing to interrupt
    override fun onInterrupt() {
        // Not used
    }
}
