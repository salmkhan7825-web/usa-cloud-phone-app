package com.example.cloudphone.data.repository

import com.example.cloudphone.data.local.dao.CloudPhoneDao
import com.example.cloudphone.data.local.entity.FileTransferEntity
import com.example.cloudphone.data.local.entity.SessionLogEntity
import com.example.cloudphone.data.local.entity.VirtualAppEntity
import com.example.cloudphone.data.local.entity.VirtualDeviceConfigEntity
import com.example.cloudphone.data.network.IpTelemetryService
import com.example.cloudphone.data.network.NetworkTelemetryResult
import com.example.cloudphone.model.CloudRegion
import com.example.cloudphone.model.CloudRegions
import com.example.cloudphone.model.DefaultVirtualApps
import com.example.cloudphone.model.FileTransferItem
import com.example.cloudphone.model.TransferDirection
import com.example.cloudphone.model.TransferStatus
import com.example.cloudphone.model.VirtualApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CloudPhoneRepository(
    private val dao: CloudPhoneDao,
    private val telemetryService: IpTelemetryService
) {
    suspend fun initializeCatalogIfNeeded() {
        val count = dao.getAppCount()
        if (count == 0) {
            val entities = DefaultVirtualApps.initialApps.map {
                VirtualAppEntity(
                    id = it.id,
                    packageName = it.packageName,
                    name = it.name,
                    category = it.category,
                    version = it.version,
                    sizeMb = it.sizeMb,
                    isInstalled = it.isInstalled,
                    isSystemApp = it.isSystemApp,
                    description = it.description,
                    iconType = it.iconType
                )
            }
            dao.insertApps(entities)
        }
    }

    fun getVirtualApps(): Flow<List<VirtualApp>> {
        return dao.getAllApps().map { entities ->
            entities.map { entity ->
                VirtualApp(
                    id = entity.id,
                    packageName = entity.packageName,
                    name = entity.name,
                    category = entity.category,
                    version = entity.version,
                    sizeMb = entity.sizeMb,
                    isInstalled = entity.isInstalled,
                    isSystemApp = entity.isSystemApp,
                    description = entity.description,
                    iconType = entity.iconType
                )
            }
        }
    }

    suspend fun setAppInstalled(appId: String, installed: Boolean) {
        dao.updateAppInstallStatus(appId, installed)
    }

    suspend fun installCustomApk(name: String, pkg: String, sizeMb: Double, category: String = "Sideloaded") {
        val newApp = VirtualAppEntity(
            id = "custom_${System.currentTimeMillis()}",
            packageName = pkg,
            name = name,
            category = category,
            version = "1.0.0-sideload",
            sizeMb = sizeMb,
            isInstalled = true,
            isSystemApp = false,
            description = "Sideloaded verified APK installed directly to US cloud phone sandbox.",
            iconType = "market"
        )
        dao.insertApps(listOf(newApp))
    }

    suspend fun recordSession(
        deviceId: String,
        region: CloudRegion,
        publicIp: String,
        startTime: Long,
        durationSeconds: Long,
        avgLatencyMs: Int
    ) {
        val log = SessionLogEntity(
            deviceId = deviceId,
            regionCode = region.code,
            regionName = region.name,
            publicIp = publicIp,
            startTime = startTime,
            durationSeconds = durationSeconds,
            avgLatencyMs = avgLatencyMs,
            bytesTransferredMb = (durationSeconds * 0.85),
            disconnectReason = "User Disconnected"
        )
        dao.insertSessionLog(log)
    }

    fun getSessionLogs(): Flow<List<SessionLogEntity>> = dao.getSessionLogs()

    suspend fun addFileTransfer(
        fileName: String,
        fileSizeBytes: Long,
        direction: TransferDirection,
        status: TransferStatus
    ) {
        val entity = FileTransferEntity(
            id = "ft_${System.currentTimeMillis()}",
            fileName = fileName,
            fileSizeBytes = fileSizeBytes,
            direction = direction.name,
            status = status.name,
            progressPercent = 100,
            timestamp = System.currentTimeMillis()
        )
        dao.insertFileTransfer(entity)
    }

    fun getFileTransfers(): Flow<List<FileTransferItem>> = dao.getFileTransfers().map { entities ->
        entities.map {
            FileTransferItem(
                id = it.id,
                fileName = it.fileName,
                fileSizeBytes = it.fileSizeBytes,
                direction = if (it.direction == TransferDirection.UPLOAD_TO_CLOUD.name) TransferDirection.UPLOAD_TO_CLOUD else TransferDirection.DOWNLOAD_FROM_CLOUD,
                status = TransferStatus.valueOf(it.status),
                progressPercent = it.progressPercent,
                timestamp = it.timestamp
            )
        }
    }

    suspend fun probeClientNetwork(): NetworkTelemetryResult {
        return telemetryService.probeClientNetwork()
    }

    suspend fun pingRegion(region: CloudRegion): Long {
        val endpoint = region.pingEndpoints.firstOrNull() ?: "https://8.8.8.8"
        return telemetryService.testRegionPing(endpoint)
    }

    suspend fun saveDeviceConfig(deviceId: String, regionId: String, resMode: String, fps: Int) {
        dao.saveDeviceConfig(
            VirtualDeviceConfigEntity(
                deviceId = deviceId,
                selectedRegionId = regionId,
                resolutionMode = resMode,
                refreshRateFps = fps,
                audioEnabled = true,
                clipboardSyncEnabled = true,
                lastConnectedTimestamp = System.currentTimeMillis()
            )
        )
    }
}
