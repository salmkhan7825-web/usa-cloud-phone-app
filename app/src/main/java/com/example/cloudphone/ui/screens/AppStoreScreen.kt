package com.example.cloudphone.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudphone.model.VirtualApp
import com.example.cloudphone.model.VirtualDeviceProfile
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.PrimarySky
import com.example.ui.theme.SecondaryIndigo
import com.example.ui.theme.StatusConnectedGreen
import com.example.ui.theme.StatusErrorRed
import com.example.ui.theme.SurfaceBorderDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevatedDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TertiaryAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AppStoreScreen(
    deviceProfile: VirtualDeviceProfile,
    allApps: List<VirtualApp>,
    onInstallApp: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onSideloadApk: (name: String, pkg: String, sizeMb: Double) -> Unit,
    onLaunchAppInViewer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showSideloadDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Installed", "System", "Internet & Web", "Productivity", "Security", "Developer Tools")

    val filteredApps = allApps.filter { app ->
        val matchesCategory = when (selectedCategory) {
            "All" -> true
            "Installed" -> app.isInstalled
            "System" -> app.isSystemApp
            else -> app.category.equals(selectedCategory, ignoreCase = true)
        }
        val matchesSearch = searchQuery.isEmpty() ||
                app.name.contains(searchQuery, ignoreCase = true) ||
                app.packageName.contains(searchQuery, ignoreCase = true) ||
                app.category.contains(searchQuery, ignoreCase = true)

        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Sideload action
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Virtual App Market",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Install & Manage Cloud Phone Sandboxed Apps",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { showSideloadDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySky),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_open_sideload_modal")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF021526), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sideload APK", color = Color(0xFF021526), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Storage Bar
        item {
            VirtualStorageOverviewCard(
                usedGb = deviceProfile.storageUsedGb,
                totalGb = deviceProfile.storageTotalGb,
                appCount = allApps.count { it.isInstalled }
            )
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search virtual packages or apps...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimarySky,
                    unfocusedBorderColor = SurfaceBorderDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) PrimarySky else SurfaceVariantDark,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) PrimarySky else SurfaceBorderDark
                        ),
                        modifier = Modifier
                            .clickable { selectedCategory = category }
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color(0xFF021526) else TextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // App List
        items(filteredApps) { app ->
            VirtualAppMarketCard(
                app = app,
                onInstall = {
                    onInstallApp(app.id)
                    Toast.makeText(context, "Installing ${app.name} into US Cloud container...", Toast.LENGTH_SHORT).show()
                },
                onUninstall = {
                    onUninstallApp(app.id)
                    Toast.makeText(context, "Uninstalled ${app.name}", Toast.LENGTH_SHORT).show()
                },
                onLaunch = {
                    onLaunchAppInViewer(app.id)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showSideloadDialog) {
        SideloadApkDialog(
            onSideload = { name, pkg, size ->
                onSideloadApk(name, pkg, size)
                Toast.makeText(context, "Sideloaded verified APK $pkg into US cloud sandbox!", Toast.LENGTH_SHORT).show()
                showSideloadDialog = false
            },
            onDismiss = { showSideloadDialog = false }
        )
    }
}

@Composable
fun VirtualStorageOverviewCard(
    usedGb: Int,
    totalGb: Int,
    appCount: Int
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
                    Icon(Icons.Default.Storage, contentDescription = null, tint = PrimarySky)
                    Text(
                        text = "Cloud Internal Storage (NVMe)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "$usedGb GB / $totalGb GB ($appCount Apps)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val progress = (usedGb.toFloat() / totalGb.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PrimarySky,
                trackColor = SurfaceVariantDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "AOSP System: 14 GB", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Text(text = "Installed Apps: ${(usedGb - 14).coerceAtLeast(0)} GB", style = MaterialTheme.typography.labelSmall, color = SecondaryIndigo)
                Text(text = "Free: ${totalGb - usedGb} GB", style = MaterialTheme.typography.labelSmall, color = StatusConnectedGreen)
            }
        }
    }
}

@Composable
fun VirtualAppMarketCard(
    app: VirtualApp,
    onInstall: () -> Unit,
    onUninstall: () -> Unit,
    onLaunch: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val (icon, bgBrush) = getAppCardVisuals(app.iconType)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = app.name, tint = Color.White, modifier = Modifier.size(24.dp))
                    }

                    Column {
                        Text(
                            text = app.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${app.packageName} • v${app.version}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = app.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Action Buttons
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (app.isInstalled) {
                        Button(
                            onClick = onLaunch,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusConnectedGreen),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_launch_${app.id}")
                        ) {
                            Text("Launch", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        if (!app.isSystemApp) {
                            TextButton(
                                onClick = onUninstall,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Uninstall", color = StatusErrorRed, fontSize = 11.sp)
                            }
                        }
                    } else {
                        Button(
                            onClick = onInstall,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimarySky),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_install_${app.id}")
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, tint = Color(0xFF021526), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Install", color = Color(0xFF021526), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "${app.sizeMb} MB",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

private fun getAppCardVisuals(iconType: String): Pair<ImageVector, Brush> {
    return when (iconType) {
        "browser" -> Pair(Icons.Default.Search, Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))))
        "market" -> Pair(Icons.Default.CloudDownload, Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF047857))))
        "settings" -> Pair(Icons.Default.Settings, Brush.linearGradient(listOf(Color(0xFF475569), Color(0xFF334155))))
        "terminal" -> Pair(Icons.Default.Speed, Brush.linearGradient(listOf(Color(0xFFEA580C), Color(0xFFC2410C))))
        "files" -> Pair(Icons.Default.Layers, Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF4F46E5))))
        "notes" -> Pair(Icons.Default.Security, Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED))))
        "youtube" -> Pair(Icons.Default.Videocam, Brush.linearGradient(listOf(Color(0xFFDC2626), Color(0xFFB91C1C))))
        "authenticator" -> Pair(Icons.Default.Lock, Brush.linearGradient(listOf(Color(0xFF0D9488), Color(0xFF0F766E))))
        else -> Pair(Icons.Default.PhoneAndroid, Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))))
    }
}

@Composable
fun SideloadApkDialog(
    onSideload: (name: String, pkg: String, sizeMb: Double) -> Unit,
    onDismiss: () -> Unit
) {
    var appName by remember { mutableStateOf("") }
    var packageName by remember { mutableStateOf("") }
    var packageSize by remember { mutableStateOf("45.2") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sideload Verified APK into US Cloud Phone", color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Specify APK metadata to push and install directly via ADB into the cloud phone instance:",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = appName,
                    onValueChange = { appName = it },
                    label = { Text("Application Name") },
                    placeholder = { Text("e.g. Signal US, DuckDuckGo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("sideload_name_input")
                )

                OutlinedTextField(
                    value = packageName,
                    onValueChange = { packageName = it },
                    label = { Text("Package Name (Android Namespace)") },
                    placeholder = { Text("e.g. org.thoughtcrime.securesms") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("sideload_pkg_input")
                )

                OutlinedTextField(
                    value = packageSize,
                    onValueChange = { packageSize = it },
                    label = { Text("Estimated Size (MB)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Quick Presets:",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PresetChip("Telegram US") {
                        appName = "Telegram Cloud"
                        packageName = "org.telegram.messenger"
                        packageSize = "68.5"
                    }
                    PresetChip("F-Droid") {
                        appName = "F-Droid Store"
                        packageName = "org.fdroid.fdroid"
                        packageSize = "18.2"
                    }
                    PresetChip("Proton VPN") {
                        appName = "Proton Mail & VPN"
                        packageName = "ch.protonvpn.android"
                        packageSize = "42.0"
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val size = packageSize.toDoubleOrNull() ?: 35.0
                    onSideload(appName.ifEmpty { "Custom App" }, packageName.ifEmpty { "com.cloud.custom.app" }, size)
                },
                enabled = appName.isNotBlank() || packageName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimarySky)
            ) {
                Text("Install APK", color = Color(0xFF021526), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        containerColor = SurfaceDark
    )
}

@Composable
fun PresetChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SurfaceElevatedDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = PrimarySky,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
