package com.example.cloudphone.ui.viewmodel

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudphone.data.local.CloudPhoneDatabase
import com.example.cloudphone.data.local.entity.SessionLogEntity
import com.example.cloudphone.data.network.IpTelemetryService
import com.example.cloudphone.data.network.NetworkTelemetryResult
import com.example.cloudphone.data.repository.CloudPhoneRepository
import com.example.cloudphone.model.CloudRegion
import com.example.cloudphone.model.CloudRegions
import com.example.cloudphone.model.ConnectionStatus
import com.example.cloudphone.model.DeviceSessionState
import com.example.cloudphone.model.FileTransferItem
import com.example.cloudphone.model.NavigationTab
import com.example.cloudphone.model.StreamQuality
import com.example.cloudphone.model.TouchInputMode
import com.example.cloudphone.model.TransferDirection
import com.example.cloudphone.model.TransferStatus
import com.example.cloudphone.model.VirtualApp
import com.example.cloudphone.model.VirtualDeviceProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CloudPhoneViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CloudPhoneRepository

    private val _currentTab = MutableStateFlow(NavigationTab.DASHBOARD)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _selectedRegion = MutableStateFlow(CloudRegions.getDefaultRegion())
    val selectedRegion: StateFlow<CloudRegion> = _selectedRegion.asStateFlow()

    private val _deviceProfile = MutableStateFlow(VirtualDeviceProfile())
    val deviceProfile: StateFlow<VirtualDeviceProfile> = _deviceProfile.asStateFlow()

    private val _sessionState = MutableStateFlow(DeviceSessionState())
    val sessionState: StateFlow<DeviceSessionState> = _sessionState.asStateFlow()

    private val _allApps = MutableStateFlow<List<VirtualApp>>(emptyList())
    val allApps: StateFlow<List<VirtualApp>> = _allApps.asStateFlow()

    private val _clientTelemetry = MutableStateFlow<NetworkTelemetryResult?>(null)
    val clientTelemetry: StateFlow<NetworkTelemetryResult?> = _clientTelemetry.asStateFlow()

    private val _regionPings = MutableStateFlow<Map<String, Long>>(emptyMap())
    val regionPings: StateFlow<Map<String, Long>> = _regionPings.asStateFlow()

    private val _isPinging = MutableStateFlow(false)
    val isPinging: StateFlow<Boolean> = _isPinging.asStateFlow()

    private val _sessionLogs = MutableStateFlow<List<SessionLogEntity>>(emptyList())
    val sessionLogs: StateFlow<List<SessionLogEntity>> = _sessionLogs.asStateFlow()

    private val _fileTransfers = MutableStateFlow<List<FileTransferItem>>(emptyList())
    val fileTransfers: StateFlow<List<FileTransferItem>> = _fileTransfers.asStateFlow()

    // Virtual OS internal app state
    private val _virtualBrowserUrl = MutableStateFlow("https://ipinfo.io")
    val virtualBrowserUrl: StateFlow<String> = _virtualBrowserUrl.asStateFlow()

    private val _virtualTerminalLogs = MutableStateFlow<List<String>>(
        listOf(
            "US Cloud ARM64 Kernel 6.1.75-android-redroid ready",
            "Root shell isolated sandbox session initialized.",
            "Type 'help' or tap a command below:"
        )
    )
    val virtualTerminalLogs: StateFlow<List<String>> = _virtualTerminalLogs.asStateFlow()

    private val _virtualNotes = MutableStateFlow(
        "US Cloud Session Notes:\n- Datacenter: AWS Ashburn VA\n- Android 14 Container isolated\n- No client battery consumption\n- Clipboard synchronized"
    )
    val virtualNotes: StateFlow<String> = _virtualNotes.asStateFlow()

    private val _virtualFilesList = MutableStateFlow(
        listOf(
            "DCIM/Screenshot_20261002_US_Cloud.png",
            "Download/aurora_store_v4.4.apk",
            "Documents/cloud_security_certificate.crt",
            "Download/redroid_config.env"
        )
    )
    val virtualFilesList: StateFlow<List<String>> = _virtualFilesList.asStateFlow()

    private val _recentAppHistory = MutableStateFlow<List<String>>(listOf("browser", "settings"))
    val recentAppHistory: StateFlow<List<String>> = _recentAppHistory.asStateFlow()

    private var sessionTimerJob: Job? = null

    init {
        val db = CloudPhoneDatabase.getInstance(application)
        val telemetry = IpTelemetryService()
        repository = CloudPhoneRepository(db.cloudPhoneDao(), telemetry)

        viewModelScope.launch {
            repository.initializeCatalogIfNeeded()
        }

        viewModelScope.launch {
            repository.getVirtualApps().collect { apps ->
                _allApps.value = apps
            }
        }

        viewModelScope.launch {
            repository.getSessionLogs().collect { logs ->
                _sessionLogs.value = logs
            }
        }

        viewModelScope.launch {
            repository.getFileTransfers().collect { transfers ->
                _fileTransfers.value = transfers
            }
        }

        // Probe initial network
        probeClientNetwork()
        testRegionPings()
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun probeClientNetwork() {
        viewModelScope.launch {
            val result = repository.probeClientNetwork()
            _clientTelemetry.value = result
        }
    }

    fun testRegionPings() {
        viewModelScope.launch {
            _isPinging.value = true
            val results = mutableMapOf<String, Long>()
            for (region in CloudRegions.allRegions) {
                val ping = repository.pingRegion(region)
                results[region.id] = ping
            }
            _regionPings.value = results
            _isPinging.value = false
        }
    }

    fun selectRegion(region: CloudRegion) {
        _selectedRegion.value = region
        _deviceProfile.update {
            it.copy(
                selectedRegion = region,
                publicIp = region.baseIp,
                reverseDns = "cloud-node-${region.code}.us-datacenter.net",
                asnProvider = "${region.datacenterProvider} (${region.cityName}, ${region.stateCode})"
            )
        }
    }

    fun connectToCloudPhone() {
        if (_sessionState.value.status == ConnectionStatus.CONNECTED) return

        viewModelScope.launch {
            _sessionState.update { it.copy(status = ConnectionStatus.CONNECTING) }
            delay(700)
            _sessionState.update { it.copy(status = ConnectionStatus.PROVISIONING) }
            delay(900)
            _sessionState.update {
                it.copy(
                    status = ConnectionStatus.CONNECTED,
                    sessionDurationSeconds = 0L,
                    sessionStartTime = System.currentTimeMillis(),
                    currentFps = 60,
                    bitRateKbps = 8450,
                    latencyMs = _selectedRegion.value.latencyMs
                )
            }
            startSessionTicker()
        }
    }

    fun disconnectCloudPhone() {
        val currentState = _sessionState.value
        if (currentState.status == ConnectionStatus.CONNECTED) {
            val duration = currentState.sessionDurationSeconds
            val startTime = currentState.sessionStartTime
            val profile = _deviceProfile.value
            viewModelScope.launch {
                repository.recordSession(
                    deviceId = profile.deviceId,
                    region = _selectedRegion.value,
                    publicIp = profile.publicIp,
                    startTime = startTime,
                    durationSeconds = duration,
                    avgLatencyMs = currentState.latencyMs
                )
            }
        }
        sessionTimerJob?.cancel()
        _sessionState.update {
            it.copy(
                status = ConnectionStatus.DISCONNECTED,
                sessionDurationSeconds = 0L,
                showRecentAppsOverview = false
            )
        }
    }

    fun rebootCloudPhone() {
        viewModelScope.launch {
            sessionTimerJob?.cancel()
            _sessionState.update {
                it.copy(
                    status = ConnectionStatus.REBOOTING,
                    activeAppId = "home",
                    showRecentAppsOverview = false
                )
            }
            delay(1200)
            _sessionState.update { it.copy(status = ConnectionStatus.PROVISIONING) }
            delay(800)
            _sessionState.update {
                it.copy(
                    status = ConnectionStatus.CONNECTED,
                    sessionDurationSeconds = 0L,
                    sessionStartTime = System.currentTimeMillis()
                )
            }
            startSessionTicker()
        }
    }

    private fun startSessionTicker() {
        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _sessionState.update { current ->
                    if (current.status == ConnectionStatus.CONNECTED) {
                        val newDur = current.sessionDurationSeconds + 1
                        val jitterFps = (58..60).random()
                        val jitterBitrate = (8200..8700).random()
                        val jitterLatency = (_selectedRegion.value.latencyMs - 2.._selectedRegion.value.latencyMs + 3).random().coerceAtLeast(15)
                        current.copy(
                            sessionDurationSeconds = newDur,
                            currentFps = jitterFps,
                            bitRateKbps = jitterBitrate,
                            latencyMs = jitterLatency
                        )
                    } else current
                }
            }
        }
    }

    // Virtual Phone OS Interactions
    fun openVirtualApp(appId: String) {
        _sessionState.update {
            it.copy(
                activeAppId = appId,
                showRecentAppsOverview = false
            )
        }
        val history = _recentAppHistory.value.toMutableList()
        history.remove(appId)
        history.add(0, appId)
        _recentAppHistory.value = history.take(6)
    }

    fun virtualHardwareHome() {
        _sessionState.update {
            it.copy(
                activeAppId = "home",
                showRecentAppsOverview = false
            )
        }
    }

    fun virtualHardwareBack() {
        val current = _sessionState.value
        if (current.showRecentAppsOverview) {
            _sessionState.update { it.copy(showRecentAppsOverview = false) }
        } else if (current.activeAppId != "home") {
            _sessionState.update { it.copy(activeAppId = "home") }
        }
    }

    fun virtualHardwareRecents() {
        _sessionState.update {
            it.copy(showRecentAppsOverview = !it.showRecentAppsOverview)
        }
    }

    fun setStreamQuality(quality: StreamQuality) {
        _sessionState.update { it.copy(streamQuality = quality) }
        _deviceProfile.update {
            it.copy(
                currentResolution = quality.resolution,
                refreshRateFps = quality.fps
            )
        }
    }

    fun toggleTouchMode() {
        _sessionState.update {
            val next = when (it.touchMode) {
                TouchInputMode.DIRECT_TOUCH -> TouchInputMode.PRECISION_POINTER
                TouchInputMode.PRECISION_POINTER -> TouchInputMode.GAMEPAD_MAPPING
                TouchInputMode.GAMEPAD_MAPPING -> TouchInputMode.DIRECT_TOUCH
            }
            it.copy(touchMode = next)
        }
    }

    fun syncClipboardToCloud(text: String) {
        _sessionState.update { it.copy(virtualClipboard = text) }
        addTerminalLog("Clipboard sync [IN]: $text")
    }

    fun copyFromCloudClipboard(): String {
        return _sessionState.value.virtualClipboard
    }

    fun takeScreenshot(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val filename = "CloudShot_${sdf.format(Date())}.png"
        val path = "/sdcard/Pictures/Screenshots/$filename"
        _sessionState.update { it.copy(lastScreenshotUri = path) }

        val files = _virtualFilesList.value.toMutableList()
        files.add(0, "Pictures/Screenshots/$filename")
        _virtualFilesList.value = files

        viewModelScope.launch {
            repository.addFileTransfer(
                fileName = filename,
                fileSizeBytes = 2450000L,
                direction = TransferDirection.DOWNLOAD_FROM_CLOUD,
                status = TransferStatus.COMPLETED
            )
        }
        return path
    }

    fun toggleRecording() {
        _sessionState.update {
            val next = !it.isRecording
            it.copy(isRecording = next, recordingDurationSec = if (next) 1 else 0)
        }
    }

    fun setVolume(volume: Int) {
        _sessionState.update { it.copy(volumeLevel = volume.coerceIn(0, 100)) }
    }

    // Virtual Browser actions
    fun navigateVirtualBrowser(url: String) {
        val sanitized = if (url.startsWith("http://") || url.startsWith("https://")) url else "https://$url"
        _virtualBrowserUrl.value = sanitized
    }

    // Terminal command actions
    fun executeTerminalCommand(input: String) {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return

        val logs = _virtualTerminalLogs.value.toMutableList()
        logs.add("$ $trimmed")

        when {
            trimmed.equals("help", ignoreCase = true) -> {
                logs.add("Available commands: uname, getprop, ip, ping, df, top, uptime, clear")
            }
            trimmed.startsWith("uname") -> {
                logs.add("Linux localhost 6.1.75-android-redroid-arm64 #1 SMP PREEMPT aarch64 GNU/Linux")
            }
            trimmed.startsWith("getprop") -> {
                logs.add("[ro.build.version.release]: [14]")
                logs.add("[ro.product.model]: [Pixel 8 Pro (Cloud ARM64)]")
                logs.add("[ro.product.manufacturer]: [Google / Redroid]")
                logs.add("[ro.redroid.region]: [${_selectedRegion.value.code}]")
            }
            trimmed.startsWith("ip") || trimmed.contains("curl") -> {
                logs.add("eth0: inet ${_deviceProfile.value.publicIp} netmask 255.255.255.0")
                logs.add("Region: ${_selectedRegion.value.name} (${_selectedRegion.value.stateCode})")
                logs.add("ISP: ${_deviceProfile.value.asnProvider}")
            }
            trimmed.startsWith("ping") -> {
                logs.add("PING 8.8.8.8 (8.8.8.8) 56(84) bytes of data.")
                logs.add("64 bytes from 8.8.8.8: icmp_seq=1 ttl=118 time=1.82 ms")
                logs.add("64 bytes from 8.8.8.8: icmp_seq=2 ttl=118 time=1.75 ms")
                logs.add("--- 8.8.8.8 ping statistics --- 0% packet loss")
            }
            trimmed.startsWith("df") -> {
                logs.add("Filesystem     Size  Used Avail Use% Mounted on")
                logs.add("/dev/block/dm-0 128G   36G   92G  28% /data")
            }
            trimmed.startsWith("uptime") -> {
                logs.add("up 5 days, 22:14, load average: 0.18, 0.22, 0.15")
            }
            trimmed.equals("clear", ignoreCase = true) -> {
                logs.clear()
            }
            else -> {
                logs.add("sh: command executed: exit code 0")
            }
        }
        _virtualTerminalLogs.value = logs.takeLast(40)
    }

    private fun addTerminalLog(msg: String) {
        val logs = _virtualTerminalLogs.value.toMutableList()
        logs.add(msg)
        _virtualTerminalLogs.value = logs.takeLast(40)
    }

    // App Store actions
    fun installApp(appId: String) {
        viewModelScope.launch {
            repository.setAppInstalled(appId, true)
            addTerminalLog("pm install -r /data/local/tmp/$appId.apk -> SUCCESS")
        }
    }

    fun uninstallApp(appId: String) {
        viewModelScope.launch {
            repository.setAppInstalled(appId, false)
            addTerminalLog("pm uninstall $appId -> SUCCESS")
        }
    }

    fun sideloadCustomApk(name: String, pkg: String, sizeMb: Double) {
        viewModelScope.launch {
            repository.installCustomApk(name, pkg, sizeMb)
            repository.addFileTransfer(
                fileName = "$pkg.apk",
                fileSizeBytes = (sizeMb * 1024 * 1024).toLong(),
                direction = TransferDirection.UPLOAD_TO_CLOUD,
                status = TransferStatus.COMPLETED
            )
            addTerminalLog("pm install -i com.cloud.sideload $pkg -> SUCCESS")
        }
    }

    // Virtual Notes
    fun updateNotes(content: String) {
        _virtualNotes.value = content
    }

    // File transfer mock upload
    fun uploadFileToCloud(fileName: String, sizeBytes: Long) {
        viewModelScope.launch {
            repository.addFileTransfer(
                fileName = fileName,
                fileSizeBytes = sizeBytes,
                direction = TransferDirection.UPLOAD_TO_CLOUD,
                status = TransferStatus.COMPLETED
            )
            val files = _virtualFilesList.value.toMutableList()
            files.add(0, "Download/$fileName")
            _virtualFilesList.value = files
            addTerminalLog("File transfer received: Download/$fileName (${sizeBytes / 1024} KB)")
        }
    }

    fun updateVirtualTimezone(tz: String) {
        _deviceProfile.update {
            it.copy(
                selectedRegion = it.selectedRegion.copy(timezone = tz)
            )
        }
    }
}
