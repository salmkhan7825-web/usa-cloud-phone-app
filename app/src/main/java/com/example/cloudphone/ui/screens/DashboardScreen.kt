package com.example.cloudphone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudphone.data.network.NetworkTelemetryResult
import com.example.cloudphone.model.CloudRegion
import com.example.cloudphone.model.CloudRegions
import com.example.cloudphone.model.ConnectionStatus
import com.example.cloudphone.model.DeviceSessionState
import com.example.cloudphone.model.NavigationTab
import com.example.cloudphone.model.VirtualDeviceProfile
import com.example.cloudphone.ui.components.MetricStatTile
import com.example.cloudphone.ui.components.TransparencyNoticeCard
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.PrimarySky
import com.example.ui.theme.PrimarySkyDark
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

@Composable
fun DashboardScreen(
    deviceProfile: VirtualDeviceProfile,
    sessionState: DeviceSessionState,
    selectedRegion: CloudRegion,
    clientTelemetry: NetworkTelemetryResult?,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onReboot: () -> Unit,
    onNavigateToPhone: () -> Unit,
    onSelectRegion: (CloudRegion) -> Unit,
    onNavigateTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var showRegionSelector by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Connection Panel
        item {
            HeroConnectionCard(
                deviceProfile = deviceProfile,
                sessionState = sessionState,
                selectedRegion = selectedRegion,
                onConnect = onConnect,
                onDisconnect = onDisconnect,
                onNavigateToPhone = onNavigateToPhone,
                onOpenRegionSelector = { showRegionSelector = true }
            )
        }

        // Live Telemetry Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricStatTile(
                    label = "Latency",
                    value = if (sessionState.status == ConnectionStatus.CONNECTED) "${sessionState.latencyMs} ms" else "${selectedRegion.latencyMs} ms",
                    icon = Icons.Default.Speed,
                    accentColor = if (sessionState.latencyMs < 40) StatusConnectedGreen else StatusWarningAmber,
                    modifier = Modifier.weight(1f)
                )
                MetricStatTile(
                    label = "Stream Rate",
                    value = if (sessionState.status == ConnectionStatus.CONNECTED) "${(sessionState.bitRateKbps / 1024.0).let { "%.1f".format(it) }} Mbps" else "8.5 Mbps",
                    icon = Icons.Default.Wifi,
                    accentColor = PrimarySky,
                    modifier = Modifier.weight(1f)
                )
                MetricStatTile(
                    label = "Frame Rate",
                    value = if (sessionState.status == ConnectionStatus.CONNECTED) "${sessionState.currentFps} FPS" else "60 FPS",
                    icon = Icons.Default.PhoneAndroid,
                    accentColor = SecondaryIndigo,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Virtual Device Specifications Card
        item {
            VirtualDeviceProfileCard(
                deviceProfile = deviceProfile,
                selectedRegion = selectedRegion,
                onReboot = onReboot,
                sessionStatus = sessionState.status
            )
        }

        // USA Cloud Network & Public IP Card
        item {
            CloudNetworkStatusCard(
                deviceProfile = deviceProfile,
                selectedRegion = selectedRegion,
                clientTelemetry = clientTelemetry,
                onSelectRegion = { showRegionSelector = true }
            )
        }

        // Quick Navigation Hub
        item {
            QuickActionsHub(
                sessionStatus = sessionState.status,
                onNavigateToPhone = onNavigateToPhone,
                onNavigateTab = onNavigateTab
            )
        }

        // Legal & Transparency Badge
        item {
            TransparencyNoticeCard()
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showRegionSelector) {
        RegionSelectorDialog(
            currentRegion = selectedRegion,
            onRegionSelected = {
                onSelectRegion(it)
                showRegionSelector = false
            },
            onDismiss = { showRegionSelector = false }
        )
    }
}

@Composable
fun HeroConnectionCard(
    deviceProfile: VirtualDeviceProfile,
    sessionState: DeviceSessionState,
    selectedRegion: CloudRegion,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onNavigateToPhone: () -> Unit,
    onOpenRegionSelector: () -> Unit
) {
    val isConnected = sessionState.status == ConnectionStatus.CONNECTED
    val isBusy = sessionState.status == ConnectionStatus.CONNECTING ||
            sessionState.status == ConnectionStatus.PROVISIONING ||
            sessionState.status == ConnectionStatus.REBOOTING

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_glow")
    val glowProgress by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isConnected) StatusConnectedGreen.copy(alpha = 0.5f) else PrimarySky.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isConnected) Color(0x2210B981) else Color(0x220284C7),
                            Color.Transparent
                        ),
                        radius = 600f
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val statusDotColor = when (sessionState.status) {
                            ConnectionStatus.CONNECTED -> StatusConnectedGreen
                            ConnectionStatus.CONNECTING, ConnectionStatus.PROVISIONING, ConnectionStatus.REBOOTING -> StatusWarningAmber
                            ConnectionStatus.DISCONNECTED, ConnectionStatus.PAUSED -> TextMuted
                        }

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(statusDotColor)
                        )

                        Text(
                            text = when (sessionState.status) {
                                ConnectionStatus.CONNECTED -> "CLOUD PHONE ONLINE"
                                ConnectionStatus.CONNECTING -> "CONNECTING TO US REGION..."
                                ConnectionStatus.PROVISIONING -> "PROVISIONING ARM64 CONTAINER..."
                                ConnectionStatus.REBOOTING -> "REBOOTING INSTANCE..."
                                ConnectionStatus.DISCONNECTED -> "STANDBY / READY"
                                ConnectionStatus.PAUSED -> "PAUSED"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isConnected) StatusConnectedGreen else TextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Region badge tag
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceElevatedDark,
                        modifier = Modifier
                            .clickable { onOpenRegionSelector() }
                            .testTag("hero_region_tag")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🇺🇸 ${selectedRegion.stateCode}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isConnected) "Session Active (${formatDuration(sessionState.sessionDurationSeconds)})" else "USA Cloud Android Instance",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )

                Text(
                    text = "Encrypted WebRTC streaming directly to US datacenter node in ${selectedRegion.name} with dedicated US IP.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!isConnected) {
                        Button(
                            onClick = onConnect,
                            enabled = !isBusy,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimarySky,
                                contentColor = Color(0xFF021526)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("btn_connect_cloud_phone")
                        ) {
                            if (isBusy) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color(0xFF021526),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Connecting...")
                            } else {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Connect to Cloud Phone",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = onNavigateToPhone,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StatusConnectedGreen,
                                contentColor = Color(0xFF022010)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(50.dp)
                                .testTag("btn_open_cloud_screen")
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Open Phone Viewer",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onDisconnect,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TertiaryAccent
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryAccent.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(0.7f)
                                .height(50.dp)
                                .testTag("btn_disconnect_cloud_phone")
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Disconnect")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VirtualDeviceProfileCard(
    deviceProfile: VirtualDeviceProfile,
    selectedRegion: CloudRegion,
    onReboot: () -> Unit,
    sessionStatus: ConnectionStatus
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = PrimarySky
                    )
                    Text(
                        text = deviceProfile.modelName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(
                    onClick = onReboot,
                    modifier = Modifier.testTag("btn_reboot_instance")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reboot Virtual Instance",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Specs Table
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpecItem(title = "OS Version", value = deviceProfile.androidVersion, modifier = Modifier.weight(1f))
                SpecItem(title = "CPU / Arch", value = "8 vCPU ARM64", modifier = Modifier.weight(1f))
                SpecItem(title = "Memory", value = "8 GB LPDDR5", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpecItem(title = "Resolution", value = deviceProfile.currentResolution, modifier = Modifier.weight(1f))
                SpecItem(title = "GPU Engine", value = "Vulkan 1.3 Passthrough", modifier = Modifier.weight(1f))
                SpecItem(title = "Storage", value = "128 GB NVMe SSD", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SpecItem(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceVariantDark,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CloudNetworkStatusCard(
    deviceProfile: VirtualDeviceProfile,
    selectedRegion: CloudRegion,
    clientTelemetry: NetworkTelemetryResult?,
    onSelectRegion: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Dns, contentDescription = null, tint = SecondaryIndigo)
                    Text(
                        text = "USA Cloud Network Intelligence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "Change",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimarySky,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onSelectRegion() }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cloud Server Public IP Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceElevatedDark)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VIRTUAL DEVICE PUBLIC US IP",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    Text(
                        text = deviceProfile.publicIp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimarySky,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "${selectedRegion.cityName}, ${selectedRegion.stateCode} (${selectedRegion.datacenterProvider})",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E3A8A)
                ) {
                    Text(
                        text = "US ASN 16509",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Local Physical Client IP Row for 100% transparency
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Physical Phone Gateway:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = clientTelemetry?.clientPublicIp ?: "Detecting carrier gateway...",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun QuickActionsHub(
    sessionStatus: ConnectionStatus,
    onNavigateToPhone: () -> Unit,
    onNavigateTab: (NavigationTab) -> Unit
) {
    Column {
        Text(
            text = "Platform Navigation",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionTile(
                title = "Launch Phone",
                subtitle = "Virtual Screen",
                icon = Icons.Default.PhoneAndroid,
                accentColor = PrimarySky,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToPhone,
                testTag = "quick_action_phone"
            )

            QuickActionTile(
                title = "US Regions",
                subtitle = "Ping & Nodes",
                icon = Icons.Default.Dns,
                accentColor = SecondaryIndigo,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(NavigationTab.REGIONS_NETWORK) },
                testTag = "quick_action_regions"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionTile(
                title = "App Market",
                subtitle = "US Store & APKs",
                icon = Icons.Default.CloudDownload,
                accentColor = StatusConnectedGreen,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(NavigationTab.APP_STORE) },
                testTag = "quick_action_apps"
            )

            QuickActionTile(
                title = "Cloud Specs",
                subtitle = "DevOps & Redroid",
                icon = Icons.Default.CloudQueue,
                accentColor = TertiaryAccent,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(NavigationTab.INFRASTRUCTURE) },
                testTag = "quick_action_specs"
            )
        }
    }
}

@Composable
fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
fun RegionSelectorDialog(
    currentRegion: CloudRegion,
    onRegionSelected: (CloudRegion) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Select USA Cloud Region",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "All nodes are located on certified US cloud infrastructure with static US IP assignment.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CloudRegions.allRegions.forEach { region ->
                        val isSelected = region.id == currentRegion.id
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) SurfaceElevatedDark else SurfaceVariantDark,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) PrimarySky else Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onRegionSelected(region) }
                                .testTag("dialog_region_${region.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "🇺🇸", fontSize = 20.sp)
                                    Column {
                                        Text(
                                            text = region.name,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${region.datacenterProvider} • ${region.baseIp}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${region.latencyMs}ms",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (region.latencyMs < 35) StatusConnectedGreen else StatusWarningAmber
                                    )
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = PrimarySky,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevatedDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = TextPrimary)
                }
            }
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hrs > 0) {
        String.format("%02d:%02d:%02d", hrs, mins, secs)
    } else {
        String.format("%02d:%02d", mins, secs)
    }
}
