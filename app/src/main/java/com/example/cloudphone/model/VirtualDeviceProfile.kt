package com.example.cloudphone.model

data class VirtualDeviceProfile(
    val deviceId: String = "us-cloud-px8-01",
    val modelName: String = "Pixel 8 Pro (Cloud ARM64 Edition)",
    val androidVersion: String = "Android 14 (AOSP)",
    val apiLevel: Int = 34,
    val buildId: String = "AP2A.240805.005.A1-cloud",
    val kernelVersion: String = "6.1.75-cloud-arm64-android",
    val cpuAllocation: String = "8 vCPU ARM Neoverse-N1 / Kyro 680",
    val ramTotalMb: Int = 8192,
    val ramUsedMb: Int = 3420,
    val storageTotalGb: Int = 128,
    val storageUsedGb: Int = 36,
    val currentResolution: String = "1080 x 2400 (FHD+)",
    val refreshRateFps: Int = 60,
    val gpuRenderer: String = "Mali-G715 / Vulkan 1.3 Passthrough",
    val selectedRegion: CloudRegion = CloudRegions.getDefaultRegion(),
    val publicIp: String = "54.210.84.192",
    val reverseDns: String = "ec2-54-210-84-192.compute-1.amazonaws.com",
    val asnProvider: String = "AS16509 Amazon.com, Inc. (Ashburn, VA)",
    val locale: String = "en-US",
    val language: String = "English (United States)",
    val dateFormat: String = "MM/DD/YYYY",
    val isLocationServiceAvailable: Boolean = true,
    val locationModeDescription: String = "Cloud Datacenter Network Location (GPS Mocking Disabled - Play Store Compliant)",
    val uptimeHours: Double = 142.6
)
