package com.abeljo.screenlocker

import android.service.quicksettings.TileService
import android.widget.Toast

/**
 * LockScreenTileService - Quick Settings tile for locking the screen.
 * 
 * This tile appears in the swipe-down control panel (Quick Settings).
 * When the user taps it, the screen locks immediately.
 * 
 * How to add the tile:
 * 1. Swipe down to open Quick Settings
 * 2. Tap the pencil/edit icon
 * 3. Find "Lock Screen" tile and drag it to active tiles
 * 
 * Note: The tile cannot programmatically add itself - the user must do it manually.
 */
class LockScreenTileService : TileService() {

    /**
     * Called when the tile is tapped.
     * Locks the screen if the AccessibilityService is enabled.
     */
    override fun onClick() {
        super.onClick()

        if (ScreenLockService.isServiceEnabled()) {
            // Lock the screen
            ScreenLockService.lockScreen()
        } else {
            // Service not enabled - show toast
            Toast.makeText(
                this,
                R.string.service_not_enabled,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Called when the tile becomes visible.
     * We don't need to update the tile state since it's a simple action tile.
     */
    override fun onStartListening() {
        super.onStartListening()
        // No state to update - tile is always clickable
    }
}
