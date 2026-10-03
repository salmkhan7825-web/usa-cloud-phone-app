package com.example.cloudphone.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudphone.data.network.NetworkTelemetryResult
import com.example.cloudphone.model.CloudRegion
import com.example.cloudphone.model.CloudRegions
import com.example.cloudphone.model.VirtualDeviceProfile
import com.example.cloudphone.ui.components.TransparencyNoticeCard
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.PrimarySky
import com.example.ui.theme.SecondaryIndigo
import com.example.ui.theme.StatusConnectedGreen
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
fun RegionNetworkScreen(
    deviceProfile: VirtualDeviceProfile,
    selectedRegion: CloudRegion,
    clientTelemetry: NetworkTelemetryResult?,
    regionPings: Map<String, Long>,
    isPinging: Boolean,
    onSelectRegion: (CloudRegion) -> Unit,
    onRefreshPings: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "USA Regional Routing",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Dedicated US Cloud IP & Datacenter Benchmarks",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = onRefreshPings,
                    enabled = !isPinging,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevatedDark),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    if (isPinging) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = PrimarySky, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = PrimarySky, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ping All", color = TextPrimary, fontSize = 12.sp)
                }
            }
        }

        // Active Cloud IP Detailed Inspection Card
        item {
            ActiveIpInspectorCard(
                deviceProfile = deviceProfile,
                selectedRegion = selectedRegion
            )
        }

        // Physical Gateway vs Cloud Gateway Transparency Comparison Card
        item {
            GatewayComparisonCard(
                clientTelemetry = clientTelemetry,
                cloudRegion = selectedRegion,
                deviceProfile = deviceProfile
            )
        }

        // Selectable US Regions List
        item {
            Text(
                text = "Select US Hosting Region",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        items(CloudRegions.allRegions) { region ->
            val isSelected = region.id == selectedRegion.id
            val livePing = regionPings[region.id] ?: region.latencyMs.toLong()

            RegionCardItem(
                region = region,
                isSelected = isSelected,
                latencyMs = livePing,
                onSelect = { onSelectRegion(region) }
            )
        }

        // DNS & WebRTC Security Shield Card
        item {
            SecurityShieldCard()
        }

        // Transparency Card
        item {
            TransparencyNoticeCard()
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ActiveIpInspectorCard(
    deviceProfile: VirtualDeviceProfile,
    selectedRegion: CloudRegion
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySky.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E3A8A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🇺🇸", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "ASSIGNED US PUBLIC IP",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimarySky,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = deviceProfile.publicIp,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StatusConnectedGreen.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusConnectedGreen)
                ) {
                    Text(
                        text = "STATIC RESIDENTIAL/CLOUD",
                        color = StatusConnectedGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = SurfaceBorderDark)
            Spacer(modifier = Modifier.height(14.dp))

            NetworkDetailRow(label = "Cloud Datacenter", value = selectedRegion.datacenterProvider)
            NetworkDetailRow(label = "Host City & State", value = "${selectedRegion.cityName}, ${selectedRegion.stateCode} (${selectedRegion.code})")
            NetworkDetailRow(label = "Autonomous System (ASN)", value = "AS16509 Amazon / AWS US-East")
            NetworkDetailRow(label = "Reverse DNS PTR", value = deviceProfile.reverseDns)
            NetworkDetailRow(label = "Target Timezone", value = "${selectedRegion.timezone} (${selectedRegion.timezoneDisplay})")
            NetworkDetailRow(label = "System Locale", value = "${deviceProfile.language} (${deviceProfile.locale})")
            NetworkDetailRow(label = "Date Display Standard", value = deviceProfile.dateFormat)
        }
    }
}

@Composable
fun NetworkDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun GatewayComparisonCard(
    clientTelemetry: NetworkTelemetryResult?,
    cloudRegion: CloudRegion,
    deviceProfile: VirtualDeviceProfile
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Dual-Gateway Transparency Inspector",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Verification proving isolation between physical device and US cloud container:",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Physical Client Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceVariantDark,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = "Physical Smartphone", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = clientTelemetry?.clientPublicIp ?: "Detecting...",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(text = "Local Carrier Wi-Fi / LTE", color = TextSecondary, fontSize = 10.sp)
                        Text(text = "GPS: Unaltered (Physical)", color = StatusWarningAmber, fontSize = 10.sp)
                    }
                }

                // Cloud Environment Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F2646),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySky),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = "Remote Cloud Android", color = PrimarySky, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = deviceProfile.publicIp,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(text = "${cloudRegion.cityName}, ${cloudRegion.stateCode} USA", color = TextSecondary, fontSize = 10.sp)
                        Text(text = "Location: US Cloud DC", color = StatusConnectedGreen, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun RegionCardItem(
    region: CloudRegion,
    isSelected: Boolean,
    latencyMs: Long,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SurfaceElevatedDark else SurfaceDark
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) PrimarySky else SurfaceBorderDark
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .testTag("region_item_${region.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "🇺🇸", fontSize = 24.sp)
                Column {
                    Text(
                        text = region.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${region.cityName}, ${region.stateCode} • ${region.datacenterProvider}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Node IP: ${region.baseIp} • ${region.timezoneDisplay}",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimarySky,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (latencyMs < 35) StatusConnectedGreen.copy(alpha = 0.2f) else StatusWarningAmber.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${latencyMs}ms",
                        color = if (latencyMs < 35) StatusConnectedGreen else StatusWarningAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (isSelected) {
                    Text(
                        text = "ACTIVE",
                        color = PrimarySky,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SecurityShieldCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = StatusConnectedGreen)
                Text(
                    text = "Security & Network Isolation Safeguards",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SecurityBullet("Cloudflare & Google US Anycast DNS 1.1.1.1 / 8.8.8.8 enforced.")
            SecurityBullet("WebRTC ICE candidate filtering prevents physical client IP leaks.")
            SecurityBullet("Containerized namespaces isolate user memory and flash storage.")
            SecurityBullet("Zero fake GPS spoofing; strictly compliant with Google Play policies.")
        }
    }
}

@Composable
fun SecurityBullet(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusConnectedGreen, modifier = Modifier.size(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}
