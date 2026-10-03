package com.example.cloudphone.model

data class CloudRegion(
    val id: String,
    val name: String,
    val code: String, // e.g., us-east-1
    val stateCode: String, // e.g., VA
    val cityName: String, // e.g., Ashburn
    val datacenterProvider: String, // e.g., AWS US-East / Equinix
    val baseIp: String, // Public US Cloud Server IP
    val timezone: String, // America/New_York
    val timezoneDisplay: String, // Eastern Time (ET)
    val latencyMs: Int,
    val status: RegionStatus = RegionStatus.OPTIMAL,
    val pingEndpoints: List<String> = emptyList()
)

enum class RegionStatus {
    OPTIMAL,
    NORMAL,
    HIGH_LOAD
}

object CloudRegions {
    val allRegions = listOf(
        CloudRegion(
            id = "us-east-1",
            name = "US East (N. Virginia)",
            code = "us-east-1",
            stateCode = "VA",
            cityName = "Ashburn",
            datacenterProvider = "AWS US-East Bare-Metal Node",
            baseIp = "54.210.84.192",
            timezone = "America/New_York",
            timezoneDisplay = "EDT (UTC-4)",
            latencyMs = 26,
            status = RegionStatus.OPTIMAL,
            pingEndpoints = listOf("https://dynamodb.us-east-1.amazonaws.com", "https://8.8.8.8")
        ),
        CloudRegion(
            id = "us-east-2",
            name = "US East (Ohio)",
            code = "us-east-2",
            stateCode = "OH",
            cityName = "Columbus",
            datacenterProvider = "GCP us-east4 Edge Cloud",
            baseIp = "18.222.140.75",
            timezone = "America/New_York",
            timezoneDisplay = "EDT (UTC-4)",
            latencyMs = 32,
            status = RegionStatus.OPTIMAL,
            pingEndpoints = listOf("https://dynamodb.us-east-2.amazonaws.com")
        ),
        CloudRegion(
            id = "us-west-2",
            name = "US West (Oregon)",
            code = "us-west-2",
            stateCode = "OR",
            cityName = "Portland / Boardman",
            datacenterProvider = "AWS US-West ARM64 Cluster",
            baseIp = "44.234.19.112",
            timezone = "America/Los_Angeles",
            timezoneDisplay = "PDT (UTC-7)",
            latencyMs = 45,
            status = RegionStatus.OPTIMAL,
            pingEndpoints = listOf("https://dynamodb.us-west-2.amazonaws.com")
        ),
        CloudRegion(
            id = "us-west-1",
            name = "US West (N. California)",
            code = "us-west-1",
            stateCode = "CA",
            cityName = "San Jose / Silicon Valley",
            datacenterProvider = "Oracle Cloud US-West Ashburn/SJ",
            baseIp = "13.56.241.68",
            timezone = "America/Los_Angeles",
            timezoneDisplay = "PDT (UTC-7)",
            latencyMs = 49,
            status = RegionStatus.NORMAL,
            pingEndpoints = listOf("https://dynamodb.us-west-1.amazonaws.com")
        ),
        CloudRegion(
            id = "us-south-1",
            name = "US Central (Texas)",
            code = "us-south-1",
            stateCode = "TX",
            cityName = "Dallas - Fort Worth",
            datacenterProvider = "Equinix Infomart Dallas DC",
            baseIp = "64.225.12.89",
            timezone = "America/Chicago",
            timezoneDisplay = "CDT (UTC-5)",
            latencyMs = 38,
            status = RegionStatus.OPTIMAL,
            pingEndpoints = listOf("https://1.1.1.1")
        )
    )

    fun getDefaultRegion(): CloudRegion = allRegions.first()
}
