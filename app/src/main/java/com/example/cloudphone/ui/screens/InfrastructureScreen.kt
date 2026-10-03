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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudphone.model.CloudInfrastructureSpec
import com.example.cloudphone.model.InfrastructureData
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

@Composable
fun InfrastructureScreen(
    spec: CloudInfrastructureSpec = InfrastructureData.defaultSpec,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Deployment & Blueprints", "Infrastructure Costs", "AOSP Licensing")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Cloud Backend & DevOps",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Architecture, Containerization, Pricing & Legal Compliance",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Sub Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = PrimarySky,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimarySky
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // Blueprints & Architecture
                item {
                    ArchitectureOverviewCard(spec)
                }

                item {
                    CodeManifestCard(
                        title = "Docker Compose (Redroid ARM64 + WebRTC Gateway)",
                        code = spec.dockerComposeSample,
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(spec.dockerComposeSample))
                            Toast.makeText(context, "Copied Docker Compose manifest!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                item {
                    CodeManifestCard(
                        title = "Kubernetes ARM64 Cluster Deployment (us-east-1)",
                        code = spec.k8sDeploymentSample,
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(spec.k8sDeploymentSample))
                            Toast.makeText(context, "Copied Kubernetes manifest!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            1 -> {
                // Costs & Cloud Providers
                item {
                    CostBreakdownCard(spec)
                }

                item {
                    HardwareRecommendationCard()
                }
            }
            2 -> {
                // Legal & Licensing
                item {
                    LicensingComplianceCard(spec)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ArchitectureOverviewCard(spec: CloudInfrastructureSpec) {
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
                Icon(Icons.Default.Cloud, contentDescription = null, tint = PrimarySky)
                Text(
                    text = "USA Cloud Architecture Stack",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TechStackRow("Cloud Infrastructure", spec.provider)
            TechStackRow("Virtualization Engine", spec.virtualizationEngine)
            TechStackRow("Recommended Instance", spec.recommendedInstance)
            TechStackRow("Streaming Gateway", spec.webrtcGateway)
            TechStackRow("NAT Traversal (STUN/TURN)", spec.turnStunSetup)
            TechStackRow("Session Management", "Redis Cluster + Auto-cleanup on 15m inactivity")
            TechStackRow("Video Hardware Encoder", "AV1 / H.264 60 FPS hardware accelerated (V4L2)")
        }
    }
}

@Composable
fun TechStackRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 11.sp)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun CodeManifestCard(
    title: String,
    code: String,
    onCopy: () -> Unit
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = SecondaryIndigo)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Button(
                    onClick = onCopy,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevatedDark),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = PrimarySky, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", color = TextPrimary, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF030712),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = code,
                    color = Color(0xFF93C5FD),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun CostBreakdownCard(spec: CloudInfrastructureSpec) {
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
                Icon(Icons.Default.AttachMoney, contentDescription = null, tint = StatusConnectedGreen)
                Text(
                    text = "Cloud Infrastructure Cost Matrix",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            CostItem("AWS EC2 c6g.medium (Spot US-East)", "$0.0136 / hr (~$9.80 / mo)", "1 vCPU, 2GB RAM (Minimal)")
            CostItem("AWS EC2 c6g.large (Spot US-East)", "$0.0272 / hr (~$19.60 / mo)", "2 vCPU, 4GB RAM (Standard)")
            CostItem("AWS EC2 c6g.metal (Dedicated Bare-Metal)", "$2.176 / hr", "Runs up to 48 concurrent Redroid phones (~$0.045/phone/hr)")
            CostItem("GCP Tau T2A (t2a-standard-4)", "$0.154 / hr (~$28.50 / mo)", "4 vCPU, 16GB RAM (High perf)")
            CostItem("Bandwidth Egress (WebRTC 60 FPS)", "~$0.02 / hr (at 8.5 Mbps)", "Coturn STUN/TURN in us-east-1")
        }
    }
}

@Composable
fun CostItem(name: String, price: String, notes: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(text = price, style = MaterialTheme.typography.bodyMedium, color = StatusConnectedGreen, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
        }
        Text(text = notes, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Divider(color = SurfaceBorderDark.copy(alpha = 0.5f), modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
fun HardwareRecommendationCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Why ARM64 Bare-Metal is Recommended",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimarySky
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Android OS is compiled natively for ARM64 architecture (aarch64). Running Redroid on x86 servers requires libndk / libhoudini binary translation, which causes up to 40% CPU overhead and visual stutter. By deploying on AWS Graviton (c6g/c7g) or Oracle Ampere A1, Android runs at 100% native bare-metal speed with zero emulation penalty.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun LicensingComplianceCard(spec: CloudInfrastructureSpec) {
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
                Icon(Icons.Default.Gavel, contentDescription = null, tint = StatusWarningAmber)
                Text(
                    text = "AOSP & Android Licensing Requirements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = spec.licensingNotes,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceElevatedDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Key Compliance Guidelines:",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ComplianceCheckItem("AOSP (Android Open Source Project) is Apache 2.0 licensed and royalty-free.")
                    ComplianceCheckItem("Google Mobile Services (GMS/Play Store) cannot be pre-bundled without MADA.")
                    ComplianceCheckItem("Aurora Store and F-Droid provide open, compliant APK distribution channels.")
                    ComplianceCheckItem("MicroG provides open-source implementation of Google Play services APIs.")
                }
            }
        }
    }
}

@Composable
fun ComplianceCheckItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("✓", color = StatusConnectedGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}
