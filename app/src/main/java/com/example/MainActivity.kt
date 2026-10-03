package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cloudphone.model.NavigationTab
import com.example.cloudphone.ui.components.CloudPhoneTopAppBar
import com.example.cloudphone.ui.screens.AppStoreScreen
import com.example.cloudphone.ui.screens.DashboardScreen
import com.example.cloudphone.ui.screens.InfrastructureScreen
import com.example.cloudphone.ui.screens.RegionNetworkScreen
import com.example.cloudphone.ui.screens.RemoteViewerScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimarySky
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevatedDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloudphone.ui.viewmodel.CloudPhoneViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: CloudPhoneViewModel = viewModel()
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: CloudPhoneViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedRegion by viewModel.selectedRegion.collectAsStateWithLifecycle()
    val deviceProfile by viewModel.deviceProfile.collectAsStateWithLifecycle()
    val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()
    val allApps by viewModel.allApps.collectAsStateWithLifecycle()
    val clientTelemetry by viewModel.clientTelemetry.collectAsStateWithLifecycle()
    val regionPings by viewModel.regionPings.collectAsStateWithLifecycle()
    val isPinging by viewModel.isPinging.collectAsStateWithLifecycle()

    val virtualBrowserUrl by viewModel.virtualBrowserUrl.collectAsStateWithLifecycle()
    val virtualTerminalLogs by viewModel.virtualTerminalLogs.collectAsStateWithLifecycle()
    val virtualNotes by viewModel.virtualNotes.collectAsStateWithLifecycle()
    val virtualFiles by viewModel.virtualFilesList.collectAsStateWithLifecycle()
    val recentApps by viewModel.recentAppHistory.collectAsStateWithLifecycle()

    // Handle Hardware Back Button
    BackHandler(enabled = currentTab != NavigationTab.DASHBOARD || sessionState.activeAppId != "home" || sessionState.showRecentAppsOverview) {
        if (currentTab == NavigationTab.REMOTE_PHONE) {
            if (sessionState.showRecentAppsOverview || sessionState.activeAppId != "home") {
                viewModel.virtualHardwareBack()
            } else {
                viewModel.selectTab(NavigationTab.DASHBOARD)
            }
        } else {
            viewModel.selectTab(NavigationTab.DASHBOARD)
        }
    }

    Scaffold(
        topBar = {
            CloudPhoneTopAppBar(
                currentRegion = selectedRegion,
                status = sessionState.status,
                onRegionClick = { viewModel.selectTab(NavigationTab.REGIONS_NETWORK) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                NavigationTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF021526),
                            selectedTextColor = PrimarySky,
                            indicatorColor = PrimarySky,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        },
        containerColor = BackgroundDark,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavigationTab.DASHBOARD -> DashboardScreen(
                    deviceProfile = deviceProfile,
                    sessionState = sessionState,
                    selectedRegion = selectedRegion,
                    clientTelemetry = clientTelemetry,
                    onConnect = { viewModel.connectToCloudPhone() },
                    onDisconnect = { viewModel.disconnectCloudPhone() },
                    onReboot = { viewModel.rebootCloudPhone() },
                    onNavigateToPhone = { viewModel.selectTab(NavigationTab.REMOTE_PHONE) },
                    onSelectRegion = { viewModel.selectRegion(it) },
                    onNavigateTab = { viewModel.selectTab(it) }
                )

                NavigationTab.REMOTE_PHONE -> RemoteViewerScreen(
                    deviceProfile = deviceProfile,
                    sessionState = sessionState,
                    selectedRegion = selectedRegion,
                    installedApps = allApps,
                    virtualBrowserUrl = virtualBrowserUrl,
                    virtualTerminalLogs = virtualTerminalLogs,
                    virtualNotes = virtualNotes,
                    virtualFiles = virtualFiles,
                    recentApps = recentApps,
                    onConnect = { viewModel.connectToCloudPhone() },
                    onDisconnect = { viewModel.disconnectCloudPhone() },
                    onReboot = { viewModel.rebootCloudPhone() },
                    onOpenApp = { viewModel.openVirtualApp(it) },
                    onVirtualHome = { viewModel.virtualHardwareHome() },
                    onVirtualBack = { viewModel.virtualHardwareBack() },
                    onVirtualRecents = { viewModel.virtualHardwareRecents() },
                    onToggleTouchMode = { viewModel.toggleTouchMode() },
                    onSyncClipboard = { viewModel.syncClipboardToCloud(it) },
                    onTakeScreenshot = { viewModel.takeScreenshot() },
                    onToggleRecording = { viewModel.toggleRecording() },
                    onChangeQuality = { viewModel.setStreamQuality(it) },
                    onNavigateBrowser = { viewModel.navigateVirtualBrowser(it) },
                    onExecuteTerminal = { viewModel.executeTerminalCommand(it) },
                    onUpdateNotes = { viewModel.updateNotes(it) },
                    onUploadFile = { name, bytes -> viewModel.uploadFileToCloud(name, bytes) },
                    onInstallApp = { viewModel.installApp(it) },
                    onUninstallApp = { viewModel.uninstallApp(it) },
                    onUpdateVirtualTimezone = { viewModel.updateVirtualTimezone(it) }
                )

                NavigationTab.REGIONS_NETWORK -> RegionNetworkScreen(
                    deviceProfile = deviceProfile,
                    selectedRegion = selectedRegion,
                    clientTelemetry = clientTelemetry,
                    regionPings = regionPings,
                    isPinging = isPinging,
                    onSelectRegion = { viewModel.selectRegion(it) },
                    onRefreshPings = { viewModel.testRegionPings() }
                )

                NavigationTab.APP_STORE -> AppStoreScreen(
                    deviceProfile = deviceProfile,
                    allApps = allApps,
                    onInstallApp = { viewModel.installApp(it) },
                    onUninstallApp = { viewModel.uninstallApp(it) },
                    onSideloadApk = { name, pkg, size -> viewModel.sideloadCustomApk(name, pkg, size) },
                    onLaunchAppInViewer = {
                        viewModel.openVirtualApp(it)
                        viewModel.selectTab(NavigationTab.REMOTE_PHONE)
                    }
                )

                NavigationTab.INFRASTRUCTURE -> InfrastructureScreen()
            }
        }
    }
}
