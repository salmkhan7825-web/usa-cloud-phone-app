package com.example.cloudphone.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD(
        title = "Dashboard",
        selectedIcon = Icons.Filled.Speed,
        unselectedIcon = Icons.Outlined.Speed,
        testTag = "tab_dashboard"
    ),
    REMOTE_PHONE(
        title = "Cloud Phone",
        selectedIcon = Icons.Filled.PhoneAndroid,
        unselectedIcon = Icons.Outlined.PhoneAndroid,
        testTag = "tab_remote_phone"
    ),
    REGIONS_NETWORK(
        title = "US Network",
        selectedIcon = Icons.Filled.Dns,
        unselectedIcon = Icons.Outlined.Dns,
        testTag = "tab_regions_network"
    ),
    APP_STORE(
        title = "App Market",
        selectedIcon = Icons.Filled.Apps,
        unselectedIcon = Icons.Outlined.Apps,
        testTag = "tab_app_store"
    ),
    INFRASTRUCTURE(
        title = "Cloud Specs",
        selectedIcon = Icons.Filled.Cloud,
        unselectedIcon = Icons.Outlined.Cloud,
        testTag = "tab_infrastructure"
    )
}
