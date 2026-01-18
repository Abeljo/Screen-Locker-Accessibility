# Screen Locker ProGuard Rules
# Keep AccessibilityService - required for the app to work
-keep class com.abeljo.screenlocker.ScreenLockService { *; }

# Keep Widget classes
-keep class com.abeljo.screenlocker.LockScreenWidget { *; }

# Keep TileService
-keep class com.abeljo.screenlocker.LockScreenTileService { *; }

# Keep MainActivity
-keep class com.abeljo.screenlocker.MainActivity { *; }

# Standard Android optimizations
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Keep R8 from stripping interfaces
-keep interface * { *; }