package com.example.cloudphone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "virtual_apps")
data class VirtualAppEntity(
    @PrimaryKey val id: String,
    val packageName: String,
    val name: String,
    val category: String,
    val version: String,
    val sizeMb: Double,
    val isInstalled: Boolean,
    val isSystemApp: Boolean,
    val description: String,
    val iconType: String
)

@Entity(tableName = "session_logs")
data class SessionLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val deviceId: String,
    val regionCode: String,
    val regionName: String,
    val publicIp: String,
    val startTime: Long,
    val durationSeconds: Long,
    val avgLatencyMs: Int,
    val bytesTransferredMb: Double,
    val disconnectReason: String
)

@Entity(tableName = "file_transfers")
data class FileTransferEntity(
    @PrimaryKey val id: String,
    val fileName: String,
    val fileSizeBytes: Long,
    val direction: String, // "UPLOAD" or "DOWNLOAD"
    val status: String, // "COMPLETED", "IN_PROGRESS", "FAILED"
    val progressPercent: Int,
    val timestamp: Long
)

@Entity(tableName = "virtual_device_config")
data class VirtualDeviceConfigEntity(
    @PrimaryKey val deviceId: String,
    val selectedRegionId: String,
    val resolutionMode: String,
    val refreshRateFps: Int,
    val audioEnabled: Boolean,
    val clipboardSyncEnabled: Boolean,
    val lastConnectedTimestamp: Long
)
