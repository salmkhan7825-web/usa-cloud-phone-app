package com.example.cloudphone.model

data class VirtualApp(
    val id: String,
    val packageName: String,
    val name: String,
    val category: String,
    val version: String,
    val sizeMb: Double,
    val isInstalled: Boolean,
    val isSystemApp: Boolean = false,
    val description: String,
    val iconType: String // e.g., "browser", "market", "settings", "terminal", "files", "notes", "youtube", "maps", "authenticator", "social"
)

object DefaultVirtualApps {
    val initialApps = listOf(
        VirtualApp(
            id = "browser",
            packageName = "com.cloud.browser.us",
            name = "US Chrome Browser",
            category = "Internet & Web",
            version = "128.0.6613.88",
            sizeMb = 94.2,
            isInstalled = true,
            isSystemApp = true,
            description = "High-speed US IP web browser with native DNS leak protection and WebRTC shield.",
            iconType = "browser"
        ),
        VirtualApp(
            id = "app_store",
            packageName = "com.cloud.market.us",
            name = "US App Market",
            category = "App Management",
            version = "4.2.1",
            sizeMb = 48.0,
            isInstalled = true,
            isSystemApp = true,
            description = "Curated US app catalog, Aurora Store client, and verified APK installer.",
            iconType = "market"
        ),
        VirtualApp(
            id = "settings",
            packageName = "com.android.settings",
            name = "Cloud Settings",
            category = "System",
            version = "14.0",
            sizeMb = 32.5,
            isInstalled = true,
            isSystemApp = true,
            description = "Virtual Android OS settings, US timezone controls, network routing, and display modes.",
            iconType = "settings"
        ),
        VirtualApp(
            id = "terminal",
            packageName = "com.cloud.termux.core",
            name = "Cloud Shell",
            category = "Developer Tools",
            version = "0.118",
            sizeMb = 76.4,
            isInstalled = true,
            isSystemApp = true,
            description = "Interactive ARM64 Linux shell environment with curl, ping, top, and Android getprop tools.",
            iconType = "terminal"
        ),
        VirtualApp(
            id = "files",
            packageName = "com.cloud.files.vault",
            name = "Cloud Files",
            category = "Storage",
            version = "3.1.0",
            sizeMb = 28.3,
            isInstalled = true,
            isSystemApp = true,
            description = "Encrypted virtual storage explorer with bi-directional transfer to physical phone.",
            iconType = "files"
        ),
        VirtualApp(
            id = "notes",
            packageName = "com.cloud.notes.secure",
            name = "Secure Notes",
            category = "Productivity",
            version = "2.0.4",
            sizeMb = 14.1,
            isInstalled = true,
            isSystemApp = false,
            description = "Private sandbox notebook with cloud clipboard integration and AES-256 local encryption.",
            iconType = "notes"
        ),
        VirtualApp(
            id = "authenticator",
            packageName = "com.cloud.auth.totp",
            name = "US 2FA Auth",
            category = "Security",
            version = "6.0.1",
            sizeMb = 21.0,
            isInstalled = false,
            isSystemApp = false,
            description = "Secure two-factor authenticator isolated inside the US cloud environment.",
            iconType = "authenticator"
        ),
        VirtualApp(
            id = "social_connect",
            packageName = "com.cloud.social.suite",
            name = "Social Suite",
            category = "Social",
            version = "5.4.0",
            sizeMb = 112.5,
            isInstalled = false,
            isSystemApp = false,
            description = "Run US accounts securely without battery drain or storage consumption on physical device.",
            iconType = "social"
        ),
        VirtualApp(
            id = "streaming_hub",
            packageName = "com.cloud.stream.us",
            name = "US Media Streamer",
            category = "Entertainment",
            version = "3.8.2",
            sizeMb = 65.4,
            isInstalled = false,
            isSystemApp = false,
            description = "Low-latency media player optimized for 60 FPS cloud video rendering.",
            iconType = "youtube"
        )
    )
}
