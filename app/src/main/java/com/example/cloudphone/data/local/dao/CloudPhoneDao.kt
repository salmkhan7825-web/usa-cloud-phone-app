package com.example.cloudphone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cloudphone.data.local.entity.FileTransferEntity
import com.example.cloudphone.data.local.entity.SessionLogEntity
import com.example.cloudphone.data.local.entity.VirtualAppEntity
import com.example.cloudphone.data.local.entity.VirtualDeviceConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CloudPhoneDao {
    // Virtual Apps
    @Query("SELECT * FROM virtual_apps ORDER BY isSystemApp DESC, name ASC")
    fun getAllApps(): Flow<List<VirtualAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<VirtualAppEntity>)

    @Query("UPDATE virtual_apps SET isInstalled = :installed WHERE id = :appId")
    suspend fun updateAppInstallStatus(appId: String, installed: Boolean)

    @Query("SELECT COUNT(*) FROM virtual_apps")
    suspend fun getAppCount(): Int

    // Session Logs
    @Query("SELECT * FROM session_logs ORDER BY startTime DESC LIMIT 50")
    fun getSessionLogs(): Flow<List<SessionLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionLog(log: SessionLogEntity): Long

    // File Transfers
    @Query("SELECT * FROM file_transfers ORDER BY timestamp DESC")
    fun getFileTransfers(): Flow<List<FileTransferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileTransfer(transfer: FileTransferEntity)

    // Device Config
    @Query("SELECT * FROM virtual_device_config WHERE deviceId = :deviceId")
    suspend fun getDeviceConfig(deviceId: String): VirtualDeviceConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDeviceConfig(config: VirtualDeviceConfigEntity)
}
