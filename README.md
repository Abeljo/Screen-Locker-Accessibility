# 🔒 Screen Locker

> **Solving my own problem** — I wanted a simple, one-tap way to lock my phone screen without pressing the physical power button. Physical buttons wear out over time, and I wanted a software alternative that's fast, minimal, and doesn't require root access.

**Developer:** Abel Yosef

---

## 📥 Download

**[Download APK](app/build/outputs/apk/release/app-release.apk)**

Or build from source (see [Building](#-building) section below).

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| **One-Tap Lock** | Lock your screen instantly with a single tap |
| **Home Screen Widget** | Add a 1x1 widget to your home screen for quick access |
| **Quick Settings Tile** | Lock from the notification shade (swipe down menu) |
| **No Root Required** | Uses Android's AccessibilityService API |
| **Minimal & Lightweight** | No unnecessary permissions or bloat |
| **Battery Friendly** | No background processing or wake locks |

---

## 📱 Screenshots

The app provides three ways to lock your screen:

1. **Main App** — Simple button in the center
2. **Widget** — Circular lock icon on your home screen  
3. **Quick Settings** — Tile in the swipe-down control panel

---

## 🛠️ How It Works

This app uses Android's **AccessibilityService** to perform a global action that locks the screen. This is the same as pressing the power button once.

### The Magic Behind It

```kotlin
performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
```

This single line of code (available since Android 9 / API 28) does all the work. The AccessibilityService has special privileges to perform system-level actions, and `GLOBAL_ACTION_LOCK_SCREEN` is one of them.

---

## 📁 Project Structure

```
app/src/main/
├── java/com/abeljo/screenlocker/
│   ├── MainActivity.kt          # Main UI with lock button
│   ├── ScreenLockService.kt     # AccessibilityService (core)
│   ├── LockScreenWidget.kt      # Home screen widget
│   └── LockScreenTileService.kt # Quick Settings tile
├── res/
│   ├── layout/
│   │   ├── activity_main.xml    # Main activity layout
│   │   └── widget_lock_screen.xml # Widget layout
│   ├── drawable/
│   │   ├── ic_lock.xml          # Lock icon
│   │   └── widget_background.xml # Widget background
│   ├── xml/
│   │   ├── accessibility_service.xml # Service config
│   │   └── widget_lock_screen_info.xml # Widget config
│   └── values/
│       └── strings.xml          # All string resources
└── AndroidManifest.xml          # App manifest
```

---

## 🔧 Code Overview

### 1. ScreenLockService.kt (The Core)

The heart of the app. This AccessibilityService provides a singleton instance that other components use to trigger the screen lock.

```kotlin
class ScreenLockService : AccessibilityService() {
    companion object {
        var instance: ScreenLockService? = null
        
        fun lockScreen(): Boolean {
            return instance?.performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN) ?: false
        }
    }
    
    override fun onServiceConnected() {
        instance = this
    }
}
```

### 2. MainActivity.kt

Simple activity with one button. If the service is enabled, it locks the screen. If not, it guides the user to enable it.

### 3. LockScreenWidget.kt

A home screen widget that sends a broadcast when tapped. The `onReceive()` method catches the broadcast and calls `ScreenLockService.lockScreen()`.

### 4. LockScreenTileService.kt

A Quick Settings tile that appears in the swipe-down menu. When tapped, it directly calls `ScreenLockService.lockScreen()`.

---

## 📋 Requirements

- **Android 9 (API 28) or higher** — `GLOBAL_ACTION_LOCK_SCREEN` was added in API 28
- **Accessibility Service permission** — Must be enabled by the user in Settings

---

## 🚀 Setup Instructions

### First Time Setup

1. **Install the app** on your device
2. **Open the app** — You'll see "Service is not enabled"
3. **Tap "Enable Service"** — This opens Accessibility Settings
4. **Find "Screen Lock Service"** in the list
5. **Enable it** and confirm the permission dialog
6. **Done!** Go back to the app and tap "Lock Screen"

### Adding the Widget

1. Long-press on your **home screen**
2. Select **"Widgets"**
3. Find **"Lock Screen"** widget
4. Drag it to your home screen
5. Tap the widget to lock instantly!

### Adding the Quick Settings Tile

1. **Swipe down** from the top of your screen (twice for full panel)
2. Tap the **pencil/edit icon** to edit tiles
3. Find **"Lock Screen"** tile
4. **Drag it** to your active tiles
5. Tap the tile anytime to lock!

---

## ⚠️ Important Notes for Huawei/EMUI Devices

Huawei's EMUI aggressively kills background services. To prevent the AccessibilityService from being killed:

1. **Settings → Battery → App launch**
   - Find "Screen Locker"
   - Set to "Manage manually"
   - Enable all options (Auto-launch, Secondary launch, Run in background)

2. **Settings → Apps → Screen Locker → Power**
   - Enable "Allow background activity"

3. **Lock the app in Recent Apps**
   - Open recent apps (swipe up or square button)
   - Find Screen Locker
   - Swipe down on it to lock (you'll see a lock icon)

---

## 🏗️ Building

### Prerequisites

- Android Studio (Arctic Fox or newer)
- JDK 11 or higher
- Android SDK 28+

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing config)
./gradlew assembleRelease

# Install directly to connected device
./gradlew installDebug
```

### Output Location

```
app/build/outputs/apk/release/app-release.apk
```

---

## 📝 Technical Details

| Property | Value |
|----------|-------|
| Package Name | `com.abeljo.screenlocker` |
| Min SDK | 28 (Android 9 Pie) |
| Target SDK | 35 (Android 15) |
| Architecture | Universal (all ABIs) |
| UI Framework | Android Views (XML) |
| Language | Kotlin |

---

## 🔒 Permissions

This app requires **only one permission**:

- **Accessibility Service** — Required to use `performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)`

The app does **NOT** request:
- ❌ Internet access
- ❌ Location
- ❌ Camera/Microphone
- ❌ Storage
- ❌ Device Admin

---

## 🤔 Why AccessibilityService?

You might wonder: *"Why use AccessibilityService just to lock the screen?"*

**The alternatives are worse:**

1. **Device Admin** — Requires scary permissions, can't be easily uninstalled
2. **Root access** — Most phones aren't rooted
3. **ADB commands** — Requires computer connection

AccessibilityService is the cleanest solution. Yes, the permission dialog looks scary, but the app's code is minimal and open — you can verify it only calls `GLOBAL_ACTION_LOCK_SCREEN`.

---

## 📜 License

This project is free to use and modify for personal use.

---

## 👨‍💻 Developer

**Abel Yosef**

*Built to solve my own problem — now sharing it with everyone who wants to save their power button!*

---

## 🙏 Acknowledgments

- Material Design icons by Google
- Inspired by the need to preserve my phone's power button

---

*Last updated: January 2026*
