package com.abeljo.screenlocker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * MainActivity - The main UI of the app.
 * 
 * Features:
 * 1. Shows service status (enabled/disabled)
 * 2. Button to lock the screen (if service is enabled)
 * 3. Button to open Accessibility Settings (if service is disabled)
 * 
 * The user must enable the AccessibilityService before the lock button works.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var lockButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle system bar insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        statusText = findViewById(R.id.statusText)
        lockButton = findViewById(R.id.lockButton)

        // Set up lock button click listener
        lockButton.setOnClickListener {
            if (ScreenLockService.isServiceEnabled()) {
                // Service is enabled - lock the screen
                val success = ScreenLockService.lockScreen()
                if (!success) {
                    Toast.makeText(this, R.string.lock_failed, Toast.LENGTH_SHORT).show()
                }
            } else {
                // Service not enabled - open Accessibility Settings
                openAccessibilitySettings()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Update UI based on service status
        updateServiceStatus()
    }

    /**
     * Updates the UI to reflect whether the AccessibilityService is enabled.
     */
    private fun updateServiceStatus() {
        if (ScreenLockService.isServiceEnabled()) {
            statusText.text = getString(R.string.service_enabled)
            lockButton.text = getString(R.string.lock_screen)
        } else {
            statusText.text = getString(R.string.service_disabled)
            lockButton.text = getString(R.string.enable_service)
        }
    }

    /**
     * Opens the Accessibility Settings page.
     * The user must manually find and enable the Screen Locker service.
     */
    private fun openAccessibilitySettings() {
        Toast.makeText(this, R.string.enable_service_hint, Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}