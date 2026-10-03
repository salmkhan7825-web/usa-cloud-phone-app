package com.example.cloudphone.model

enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    PROVISIONING,
    CONNECTED,
    REBOOTING,
    PAUSED
}

enum class TouchInputMode {
    DIRECT_TOUCH,
    PRECISION_POINTER,
    GAMEPAD_MAPPING
}

data class DeviceSessionState(
    val status: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val sessionDurationSeconds: Long = 0L,
    val currentFps: Int = 60,
    val bitRateKbps: Int = 8450,
    val latencyMs: Int = 26,
    val packetLossPercent: Float = 0.0f,
    val activeAppId: String = "home", // "home", "browser", "app_store", "settings", "terminal", "files", "notes"
    val touchMode: TouchInputMode = TouchInputMode.DIRECT_TOUCH,
    val virtualClipboard: String = "https://developer.android.com",
    val isRecording: Boolean = false,
    val recordingDurationSec: Int = 0,
    val isAudioMuted: Boolean = false,
    val volumeLevel: Int = 85, // 0 - 100
    val streamQuality: StreamQuality = StreamQuality.FHD_60FPS,
    val showRecentAppsOverview: Boolean = false,
    val lastScreenshotUri: String? = null,
    val sessionStartTime: Long = System.currentTimeMillis()
)

enum class StreamQuality(val label: String, val resolution: String, val fps: Int, val targetBitrate: String) {
    HD_30FPS("720p Balanced", "720 x 1600", 30, "4.0 Mbps"),
    FHD_60FPS("1080p High Performance", "1080 x 2400", 60, "8.5 Mbps"),
    QHD_90FPS("1440p Ultra Quality", "1440 x 3200", 90, "16.0 Mbps")
}
