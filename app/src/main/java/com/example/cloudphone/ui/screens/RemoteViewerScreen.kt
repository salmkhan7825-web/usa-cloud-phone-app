package com.example.cloudphone.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudphone.model.CloudRegion
import com.example.cloudphone.model.ConnectionStatus
import com.example.cloudphone.model.DeviceSessionState
import com.example.cloudphone.model.StreamQuality
import com.example.cloudphone.model.TouchInputMode
import com.example.cloudphone.model.VirtualApp
import com.example.cloudphone.model.VirtualDeviceProfile
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.PrimarySky
import com.example.ui.theme.SecondaryIndigo
import com.example.ui.theme.StatusConnectedGreen
import com.example.ui.theme.StatusErrorRed
import com.example.ui.theme.StatusWarningAmber
import com.example.ui.theme.SurfaceBorderDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevatedDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TertiaryAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UsFlagNavy
import com.example.ui.theme.UsFlagRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun RemoteViewerScreen(
    deviceProfile: VirtualDeviceProfile,
    sessionState: DeviceSessionState,
    selectedRegion: CloudRegion,
    installedApps: List<VirtualApp>,
    virtualBrowserUrl: String,
    virtualTerminalLogs: List<String>,
    virtualNotes: String,
    virtualFiles: List<String>,
    recentApps: List<String>,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onReboot: () -> Unit,
    onOpenApp: (String) -> Unit,
    onVirtualHome: () -> Unit,
    onVirtualBack: () -> Unit,
    onVirtualRecents: () -> Unit,
    onToggleTouchMode: () -> Unit,
    onSyncClipboard: (String) -> Unit,
    onTakeScreenshot: () -> String,
    onToggleRecording: () -> Unit,
    onChangeQuality: (StreamQuality) -> Unit,
    onNavigateBrowser: (String) -> Unit,
    onExecuteTerminal: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onUploadFile: (String, Long) -> Unit,
    onInstallApp: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onUpdateVirtualTimezone: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showKeyboardDialog by remember { mutableStateOf(false) }
    var showClipboardDialog by remember { mutableStateOf(false) }
    var showQualityMenu by remember { mutableStateOf(false) }
    var showFileTransferDialog by remember { mutableStateOf(false) }
    var showApkUploadDialog by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            toastMessage = null
        }
    }

    if (sessionState.status != ConnectionStatus.CONNECTED) {
        // Disconnected / Offline Placeholder view
        DisconnectedPlaceholder(
            status = sessionState.status,
            region = selectedRegion,
            onConnect = onConnect
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // 1. Top Remote Control Bar
        RemoteControlToolbar(
            sessionState = sessionState,
            selectedRegion = selectedRegion,
            onToggleTouchMode = onToggleTouchMode,
            onOpenKeyboard = { showKeyboardDialog = true },
            onOpenClipboard = { showClipboardDialog = true },
            onOpenFileTransfer = { showFileTransferDialog = true },
            onTakeScreenshot = {
                val path = onTakeScreenshot()
                toastMessage = "Screenshot saved: $path"
            },
            onToggleRecording = {
                onToggleRecording()
                toastMessage = if (!sessionState.isRecording) "Recording cloud session..." else "Recording saved to gallery"
            },
            onOpenQualityMenu = { showQualityMenu = true },
            onReboot = onReboot,
            onDisconnect = onDisconnect
        )

        // 2. The Interactive Virtual Phone Screen Frame
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            VirtualPhoneCanvas(
                deviceProfile = deviceProfile,
                sessionState = sessionState,
                selectedRegion = selectedRegion,
                installedApps = installedApps,
                virtualBrowserUrl = virtualBrowserUrl,
                virtualTerminalLogs = virtualTerminalLogs,
                virtualNotes = virtualNotes,
                virtualFiles = virtualFiles,
                recentApps = recentApps,
                onOpenApp = onOpenApp,
                onNavigateBrowser = onNavigateBrowser,
                onExecuteTerminal = onExecuteTerminal,
                onUpdateNotes = onUpdateNotes,
                onOpenFileTransfer = { showFileTransferDialog = true },
                onInstallApp = onInstallApp,
                onUninstallApp = onUninstallApp,
                onUpdateVirtualTimezone = onUpdateVirtualTimezone,
                onOpenApkUpload = { showApkUploadDialog = true }
            )
        }

        // 3. Virtual Hardware Navigation Bar (Back, Home, Recents)
        VirtualHardwareNavBar(
            onBack = onVirtualBack,
            onHome = onVirtualHome,
            onRecents = onVirtualRecents
        )
    }

    // Keyboard Input Dialog
    if (showKeyboardDialog) {
        VirtualKeyboardDialog(
            onSendText = { text ->
                onSyncClipboard(text)
                toastMessage = "Injected text into cloud phone: $text"
                showKeyboardDialog = false
            },
            onDismiss = { showKeyboardDialog = false }
        )
    }

    // Clipboard Sync Dialog
    if (showClipboardDialog) {
        VirtualClipboardDialog(
            currentCloudClipboard = sessionState.virtualClipboard,
            onPushToCloud = { text ->
                onSyncClipboard(text)
                toastMessage = "Clipboard synced to US Cloud phone!"
                showClipboardDialog = false
            },
            onPullToClient = { text ->
                clipboardManager.setText(AnnotatedString(text))
                toastMessage = "Copied cloud clipboard to physical phone!"
                showClipboardDialog = false
            },
            onDismiss = { showClipboardDialog = false }
        )
    }

    // File Transfer Dialog
    if (showFileTransferDialog) {
        VirtualFileTransferDialog(
            onUploadSample = { name, bytes ->
                onUploadFile(name, bytes)
                toastMessage = "Uploaded $name to /sdcard/Download"
                showFileTransferDialog = false
            },
            onDismiss = { showFileTransferDialog = false }
        )
    }
}

@Composable
fun RemoteControlToolbar(
    sessionState: DeviceSessionState,
    selectedRegion: CloudRegion,
    onToggleTouchMode: () -> Unit,
    onOpenKeyboard: () -> Unit,
    onOpenClipboard: () -> Unit,
    onOpenFileTransfer: () -> Unit,
    onTakeScreenshot: () -> Unit,
    onToggleRecording: () -> Unit,
    onOpenQualityMenu: () -> Unit,
    onReboot: () -> Unit,
    onDisconnect: () -> Unit
) {
    Surface(
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Live Telemetry Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(StatusConnectedGreen)
                )
                Text(
                    text = "${sessionState.currentFps} FPS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = StatusConnectedGreen,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "• ${sessionState.latencyMs}ms",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "• ${sessionState.streamQuality.label.take(4)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimarySky,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Right: Floating Control Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onToggleTouchMode,
                    modifier = Modifier.size(36.dp).testTag("tool_touch_mode")
                ) {
                    Icon(
                        imageVector = if (sessionState.touchMode == TouchInputMode.DIRECT_TOUCH) Icons.Default.TouchApp else Icons.Default.Mouse,
                        contentDescription = "Touch Mode",
                        tint = if (sessionState.touchMode == TouchInputMode.DIRECT_TOUCH) PrimarySky else SecondaryIndigo,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onOpenKeyboard,
                    modifier = Modifier.size(36.dp).testTag("tool_keyboard")
                ) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Virtual Keyboard",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onOpenClipboard,
                    modifier = Modifier.size(36.dp).testTag("tool_clipboard")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = "Clipboard Sync",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onOpenFileTransfer,
                    modifier = Modifier.size(36.dp).testTag("tool_file_transfer")
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "File Transfer",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onTakeScreenshot,
                    modifier = Modifier.size(36.dp).testTag("tool_screenshot")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Take Screenshot",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onToggleRecording,
                    modifier = Modifier.size(36.dp).testTag("tool_record")
                ) {
                    Icon(
                        imageVector = Icons.Default.FiberManualRecord,
                        contentDescription = "Record Screen",
                        tint = if (sessionState.isRecording) StatusErrorRed else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onReboot,
                    modifier = Modifier.size(36.dp).testTag("tool_reboot")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reboot Device",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDisconnect,
                    modifier = Modifier.size(36.dp).testTag("tool_disconnect")
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Disconnect Session",
                        tint = TertiaryAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VirtualPhoneCanvas(
    deviceProfile: VirtualDeviceProfile,
    sessionState: DeviceSessionState,
    selectedRegion: CloudRegion,
    installedApps: List<VirtualApp>,
    virtualBrowserUrl: String,
    virtualTerminalLogs: List<String>,
    virtualNotes: String,
    virtualFiles: List<String>,
    recentApps: List<String>,
    onOpenApp: (String) -> Unit,
    onNavigateBrowser: (String) -> Unit,
    onExecuteTerminal: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onOpenFileTransfer: () -> Unit,
    onInstallApp: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onUpdateVirtualTimezone: (String) -> Unit,
    onOpenApkUpload: () -> Unit
) {
    // Phone Bezel Frame
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFF030712),
        border = androidx.compose.foundation.BorderStroke(3.dp, Color(0xFF1E293B)),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 480.dp)
            .clip(RoundedCornerShape(26.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Android Status Bar
            VirtualAndroidStatusBar(
                region = selectedRegion
            )

            // Screen Content (App window or Home screen)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (sessionState.showRecentAppsOverview) {
                    RecentAppsOverview(
                        recentApps = recentApps,
                        installedApps = installedApps,
                        onOpenApp = onOpenApp
                    )
                } else {
                    when (sessionState.activeAppId) {
                        "home" -> VirtualHomeScreen(
                            deviceProfile = deviceProfile,
                            selectedRegion = selectedRegion,
                            installedApps = installedApps,
                            onOpenApp = onOpenApp
                        )
                        "browser" -> VirtualBrowserWindow(
                            url = virtualBrowserUrl,
                            publicIp = deviceProfile.publicIp,
                            region = selectedRegion,
                            onNavigate = onNavigateBrowser
                        )
                        "app_store" -> VirtualAppStoreWindow(
                            apps = installedApps,
                            onInstall = onInstallApp,
                            onUninstall = onUninstallApp,
                            onOpen = onOpenApp,
                            onOpenApkUpload = onOpenApkUpload
                        )
                        "settings" -> VirtualSettingsWindow(
                            deviceProfile = deviceProfile,
                            selectedRegion = selectedRegion,
                            onUpdateTimezone = onUpdateVirtualTimezone
                        )
                        "terminal" -> VirtualTerminalWindow(
                            logs = virtualTerminalLogs,
                            onExecute = onExecuteTerminal
                        )
                        "files" -> VirtualFilesWindow(
                            files = virtualFiles,
                            onUpload = onOpenFileTransfer
                        )
                        "notes" -> VirtualNotesWindow(
                            notes = virtualNotes,
                            onUpdateNotes = onUpdateNotes
                        )
                        else -> VirtualGenericAppWindow(
                            appId = sessionState.activeAppId,
                            installedApps = installedApps,
                            onBackToHome = { onOpenApp("home") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VirtualAndroidStatusBar(
    region: CloudRegion
) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.US).apply {
        timeZone = TimeZone.getTimeZone(region.timezone)
    }
    val currentTime = remember { timeFormat.format(Date()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color(0xFF030712))
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Time & US Cloud Carrier
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "• US Cloud 5G",
                color = PrimarySky,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Center: Camera Notch
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F172A))
        )

        // Right: Wi-Fi, Signal, Battery
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Icon(
                imageVector = Icons.Default.BatteryChargingFull,
                contentDescription = null,
                tint = StatusConnectedGreen,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "98%",
                color = Color.White,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun VirtualHomeScreen(
    deviceProfile: VirtualDeviceProfile,
    selectedRegion: CloudRegion,
    installedApps: List<VirtualApp>,
    onOpenApp: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A1128),
                        Color(0xFF0F1D38),
                        Color(0xFF020617)
                    )
                )
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Widgets
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Search Widget
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0x33FFFFFF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenApp("browser") }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search US Web",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Search Google US or enter URL...",
                            color = Color(0xBBFFFFFF),
                            fontSize = 12.sp
                        )
                    }
                }

                // Cloud System Telemetry Widget
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0x221E293B),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "USA CLOUD HOST • ${selectedRegion.stateCode}",
                                color = PrimarySky,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "${selectedRegion.cityName} (${selectedRegion.datacenterProvider.take(18)})",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "IP: ${deviceProfile.publicIp} • Android 14",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusConnectedGreen)
                        ) {
                            Text(
                                text = "60 FPS",
                                color = StatusConnectedGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Center/Bottom Section: App Grid
            val appsToDisplay = installedApps.filter { it.isInstalled }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                item {
                    Text(
                        text = "VIRTUAL APPS",
                        color = Color(0x88FFFFFF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                }

                // App Rows of 4 items
                val chunks = appsToDisplay.chunked(4)
                items(chunks) { rowApps ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        rowApps.forEach { app ->
                            AppIconItem(
                                app = app,
                                onClick = { onOpenApp(app.id) }
                            )
                        }
                    }
                }
            }

            // Bottom Favorite Dock
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0x331E293B),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DockAppItem(name = "Browser", icon = Icons.Default.Search, color = PrimarySky) { onOpenApp("browser") }
                    DockAppItem(name = "Market", icon = Icons.Default.CloudDownload, color = StatusConnectedGreen) { onOpenApp("app_store") }
                    DockAppItem(name = "Shell", icon = Icons.Default.Speed, color = Color(0xFFF97316)) { onOpenApp("terminal") }
                    DockAppItem(name = "Files", icon = Icons.Default.Layers, color = SecondaryIndigo) { onOpenApp("files") }
                    DockAppItem(name = "Settings", icon = Icons.Default.Settings, color = Color(0xFF94A3B8)) { onOpenApp("settings") }
                }
            }
        }
    }
}

@Composable
fun AppIconItem(
    app: VirtualApp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { onClick() }
            .testTag("app_icon_${app.id}")
    ) {
        val (icon, bgBrush) = getAppVisuals(app.iconType)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(bgBrush)
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = app.name,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = app.name,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
fun DockAppItem(
    name: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.25f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
    }
}

private fun getAppVisuals(iconType: String): Pair<ImageVector, Brush> {
    return when (iconType) {
        "browser" -> Pair(
            Icons.Default.Search,
            Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1)))
        )
        "market" -> Pair(
            Icons.Default.CloudDownload,
            Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF047857)))
        )
        "settings" -> Pair(
            Icons.Default.Settings,
            Brush.linearGradient(listOf(Color(0xFF475569), Color(0xFF334155)))
        )
        "terminal" -> Pair(
            Icons.Default.Speed,
            Brush.linearGradient(listOf(Color(0xFFEA580C), Color(0xFFC2410C)))
        )
        "files" -> Pair(
            Icons.Default.Layers,
            Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF4F46E5)))
        )
        "notes" -> Pair(
            Icons.Default.Security,
            Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED)))
        )
        "youtube" -> Pair(
            Icons.Default.Videocam,
            Brush.linearGradient(listOf(Color(0xFFDC2626), Color(0xFFB91C1C)))
        )
        "authenticator" -> Pair(
            Icons.Default.Lock,
            Brush.linearGradient(listOf(Color(0xFF0D9488), Color(0xFF0F766E)))
        )
        else -> Pair(
            Icons.Default.PhoneAndroid,
            Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)))
        )
    }
}

// ----------------- VIRTUAL APPS (WINDOWS) -----------------

@Composable
fun VirtualBrowserWindow(
    url: String,
    publicIp: String,
    region: CloudRegion,
    onNavigate: (String) -> Unit
) {
    var inputUrl by remember { mutableStateOf(url) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B132B))
    ) {
        // Browser URL Bar
        Surface(
            color = Color(0xFF1C2541),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Secure HTTPS",
                    tint = StatusConnectedGreen,
                    modifier = Modifier.size(16.dp)
                )

                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = { inputUrl = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { onNavigate(inputUrl) }),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimarySky,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xDDFFFFFF)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("browser_url_input")
                )

                IconButton(
                    onClick = { onNavigate(inputUrl) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Go",
                        tint = PrimarySky,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Web Page Content - Simulated Live US IP Verification Page
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111D3B)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySky.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🇺🇸", fontSize = 24.sp)
                            Column {
                                Text(
                                    text = "IP GEOLOCATION VERIFICATION",
                                    color = PrimarySky,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "United States Cloud Environment",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        WebInfoRow("Public IPv4 Address", publicIp)
                        WebInfoRow("Host Location", "${region.cityName}, ${region.stateCode}, USA")
                        WebInfoRow("Datacenter Provider", region.datacenterProvider)
                        WebInfoRow("ASN / Organization", "AS16509 Amazon.com US-East")
                        WebInfoRow("DNS Leak Protection", "PASSED (Cloudflare US 1.1.1.1)")
                        WebInfoRow("WebRTC IP Leak", "SHIELDED (No Client IP Leak)")
                    }
                }
            }

            item {
                Text(
                    text = "Quick Verification Bookmarks",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BookmarkChip(title = "Fast.com US", url = "https://fast.com") { onNavigate(it) }
                    BookmarkChip(title = "Google US", url = "https://www.google.com") { onNavigate(it) }
                    BookmarkChip(title = "IPLeak.net", url = "https://ipleak.net") { onNavigate(it) }
                }
            }
        }
    }
}

@Composable
fun WebInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextMuted, fontSize = 11.sp)
        Text(text = value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun BookmarkChip(title: String, url: String, onNavigate: (String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E293B),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF)),
        modifier = Modifier.clickable { onNavigate(url) }
    ) {
        Text(
            text = title,
            color = PrimarySky,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun VirtualAppStoreWindow(
    apps: List<VirtualApp>,
    onInstall: (String) -> Unit,
    onUninstall: (String) -> Unit,
    onOpen: (String) -> Unit,
    onOpenApkUpload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // App Store Header
        Surface(
            color = Color(0xFF1E293B),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "US Cloud App Market",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "AOSP Sandboxed App Catalog",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onOpenApkUpload,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySky),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_upload_apk_header")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF021526))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sideload APK", fontSize = 11.sp, color = Color(0xFF021526), fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(apps) { app ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val (icon, bgBrush) = getAppVisuals(app.iconType)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bgBrush),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(text = app.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "${app.category} • ${app.sizeMb} MB", color = TextMuted, fontSize = 10.sp)
                                Text(text = app.description, color = TextSecondary, fontSize = 10.sp, maxLines = 1)
                            }
                        }

                        if (app.isInstalled) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Button(
                                    onClick = { onOpen(app.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusConnectedGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text("Open", fontSize = 11.sp, color = Color.White)
                                }
                                if (!app.isSystemApp) {
                                    IconButton(
                                        onClick = { onUninstall(app.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Uninstall", tint = StatusErrorRed, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        } else {
                            Button(
                                onClick = { onInstall(app.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimarySky),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text("Install", fontSize = 11.sp, color = Color(0xFF021526), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VirtualSettingsWindow(
    deviceProfile: VirtualDeviceProfile,
    selectedRegion: CloudRegion,
    onUpdateTimezone: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Cloud Android Settings",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "USA Regional Configuration", color = PrimarySky, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    WebInfoRow("Active Cloud Region", "${selectedRegion.name} (${selectedRegion.code})")
                    WebInfoRow("Current Timezone", selectedRegion.timezone)
                    WebInfoRow("Selected Timezone Display", selectedRegion.timezoneDisplay)
                    WebInfoRow("Language / Locale", "${deviceProfile.language} (${deviceProfile.locale})")
                    WebInfoRow("Date Format", deviceProfile.dateFormat)

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Select US Timezone:", color = TextSecondary, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TimezoneChip("Eastern", "America/New_York", selectedRegion.timezone) { onUpdateTimezone(it) }
                        TimezoneChip("Central", "America/Chicago", selectedRegion.timezone) { onUpdateTimezone(it) }
                        TimezoneChip("Pacific", "America/Los_Angeles", selectedRegion.timezone) { onUpdateTimezone(it) }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Hardware & Container Info", color = SecondaryIndigo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    WebInfoRow("Device Model", deviceProfile.modelName)
                    WebInfoRow("Android OS", deviceProfile.androidVersion)
                    WebInfoRow("Kernel", deviceProfile.kernelVersion)
                    WebInfoRow("Build ID", deviceProfile.buildId)
                    WebInfoRow("CPU Hardware", deviceProfile.cpuAllocation)
                    WebInfoRow("RAM Used", "${deviceProfile.ramUsedMb} MB / ${deviceProfile.ramTotalMb} MB")
                    WebInfoRow("Internal Storage", "${deviceProfile.storageUsedGb} GB / ${deviceProfile.storageTotalGb} GB")
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Device Location Mode", color = Color(0xFFF59E0B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = deviceProfile.locationModeDescription,
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TimezoneChip(name: String, tz: String, currentTz: String, onSelect: (String) -> Unit) {
    val isSelected = tz == currentTz
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrimarySky else Color(0xFF334155),
        modifier = Modifier.clickable { onSelect(tz) }
    ) {
        Text(
            text = name,
            color = if (isSelected) Color(0xFF021526) else Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun VirtualTerminalWindow(
    logs: List<String>,
    onExecute: (String) -> Unit
) {
    var commandInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030712))
            .padding(8.dp)
    ) {
        Text(
            text = "US Cloud ARM64 Root Shell [redroid@localhost ~]#",
            color = StatusConnectedGreen,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            items(logs) { log ->
                Text(
                    text = log,
                    color = if (log.startsWith("$")) PrimarySky else Color(0xFFE2E8F0),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Quick Command Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CommandChip("uname -a") { onExecute(it) }
            CommandChip("ip info") { onExecute(it) }
            CommandChip("getprop") { onExecute(it) }
            CommandChip("ping 8.8.8.8") { onExecute(it) }
            CommandChip("df -h") { onExecute(it) }
        }

        // Shell Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$ ", color = StatusConnectedGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    onExecute(commandInput)
                    commandInput = ""
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimarySky,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("terminal_input")
            )
            IconButton(
                onClick = {
                    onExecute(commandInput)
                    commandInput = ""
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Run", tint = PrimarySky, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun CommandChip(cmd: String, onClick: (String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFF1E293B),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF)),
        modifier = Modifier.clickable { onClick(cmd) }
    ) {
        Text(
            text = cmd,
            color = Color(0xFF93C5FD),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun VirtualFilesWindow(
    files: List<String>,
    onUpload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Cloud Internal Storage (/sdcard/)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = onUpload,
                colors = ButtonDefaults.buttonColors(containerColor = PrimarySky),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF021526))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload File", fontSize = 10.sp, color = Color(0xFF021526), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(files) { filePath ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Layers, contentDescription = null, tint = SecondaryIndigo, modifier = Modifier.size(20.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = filePath, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text(text = "Cloud Encrypted • Readable by installed apps", color = TextMuted, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VirtualNotesWindow(
    notes: String,
    onUpdateNotes: (String) -> Unit
) {
    var text by remember { mutableStateOf(notes) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Secure Cloud Notes", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = { onUpdateNotes(text) },
                colors = ButtonDefaults.buttonColors(containerColor = StatusConnectedGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("Save", fontSize = 11.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimarySky,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxSize()
                .testTag("virtual_notes_input")
        )
    }
}

@Composable
fun VirtualGenericAppWindow(
    appId: String,
    installedApps: List<VirtualApp>,
    onBackToHome: () -> Unit
) {
    val app = installedApps.firstOrNull { it.id == appId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = app?.name ?: "Cloud App",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Running isolated in US Cloud container (${app?.packageName})",
            color = TextSecondary,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBackToHome,
            colors = ButtonDefaults.buttonColors(containerColor = PrimarySky)
        ) {
            Text("Return to Home Screen", color = Color(0xFF021526))
        }
    }
}

@Composable
fun RecentAppsOverview(
    recentApps: List<String>,
    installedApps: List<VirtualApp>,
    onOpenApp: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEE030712))
            .padding(16.dp)
    ) {
        Text(
            text = "Active Cloud Tasks",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(recentApps) { appId ->
                val app = installedApps.firstOrNull { it.id == appId }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySky),
                    modifier = Modifier
                        .width(180.dp)
                        .height(280.dp)
                        .clickable { onOpenApp(appId) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = app?.name ?: appId,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Running Sandbox",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }

                        Text(
                            text = "Tap to switch",
                            color = PrimarySky,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// ----------------- HARDWARE NAV BAR -----------------

@Composable
fun VirtualHardwareNavBar(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onRecents: () -> Unit
) {
    Surface(
        color = Color(0xFF030712),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button (◄)
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(44.dp).testTag("virtual_hw_back")
            ) {
                Text(
                    text = "◀",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // Home Button (●)
            IconButton(
                onClick = onHome,
                modifier = Modifier.size(44.dp).testTag("virtual_hw_home")
            ) {
                Text(
                    text = "●",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            // Recents Button (■)
            IconButton(
                onClick = onRecents,
                modifier = Modifier.size(44.dp).testTag("virtual_hw_recents")
            ) {
                Text(
                    text = "■",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// ----------------- DIALOGS & OVERLAYS -----------------

@Composable
fun VirtualKeyboardDialog(
    onSendText: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Send Keystrokes to Cloud Phone", color = TextPrimary) },
        text = {
            Column {
                Text("Type text to inject directly into the active virtual screen input:", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Enter text, URLs, or commands...") },
                    modifier = Modifier.fillMaxWidth().testTag("keyboard_dialog_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSendText(inputText) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimarySky)
            ) {
                Text("Send Text", color = Color(0xFF021526))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        containerColor = SurfaceDark
    )
}

@Composable
fun VirtualClipboardDialog(
    currentCloudClipboard: String,
    onPushToCloud: (String) -> Unit,
    onPullToClient: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textToPush by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bi-Directional Clipboard Sync", color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Cloud Phone Clipboard Content:", color = TextSecondary, fontSize = 12.sp)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceElevatedDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = currentCloudClipboard.ifEmpty { "(Empty clipboard)" },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Button(
                    onClick = { onPullToClient(currentCloudClipboard) },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy to Physical Smartphone")
                }

                Divider(color = SurfaceBorderDark)

                Text("Push to Cloud Clipboard:", color = TextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = textToPush,
                    onValueChange = { textToPush = it },
                    placeholder = { Text("Paste or type text to send...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onPushToCloud(textToPush) },
                enabled = textToPush.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimarySky)
            ) {
                Text("Push to Cloud", color = Color(0xFF021526))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        containerColor = SurfaceDark
    )
}

@Composable
fun VirtualFileTransferDialog(
    onUploadSample: (String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload File to Cloud Phone", color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select sample media or package to transfer directly into the cloud phone's /sdcard/Download folder:", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))

                FileOptionButton("Sample US Document.pdf", 450000L) { onUploadSample("US_Tax_Document.pdf", 450000L) }
                FileOptionButton("High-Res Photo.jpg", 3200000L) { onUploadSample("IMG_20261002_US.jpg", 3200000L) }
                FileOptionButton("Cloud Configuration.json", 12000L) { onUploadSample("cloud_config.json", 12000L) }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        containerColor = SurfaceDark
    )
}

@Composable
fun FileOptionButton(name: String, size: Long, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceElevatedDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, color = Color.White, fontSize = 12.sp)
            Text(text = "${size / 1024} KB", color = PrimarySky, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
fun DisconnectedPlaceholder(
    status: ConnectionStatus,
    region: CloudRegion,
    onConnect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
            modifier = Modifier.fillMaxWidth().widthIn(max = 400.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PrimarySky.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = PrimarySky,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Cloud Phone is Standby",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Target Node: ${region.name} (${region.cityName}, ${region.stateCode})\nDedicated US IP: ${region.baseIp}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontFamily = FontFamily.Monospace
                )

                Button(
                    onClick = onConnect,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySky),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF021526))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Cloud Phone Session", color = Color(0xFF021526), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
