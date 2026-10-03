package com.example.cloudphone.model

data class CloudInfrastructureSpec(
    val title: String,
    val provider: String,
    val recommendedInstance: String,
    val monthlyEstimatedCost: String,
    val hourlyCost: String,
    val virtualizationEngine: String,
    val webrtcGateway: String,
    val turnStunSetup: String,
    val licensingNotes: String,
    val dockerComposeSample: String,
    val k8sDeploymentSample: String
)

object InfrastructureData {
    val defaultSpec = CloudInfrastructureSpec(
        title = "Production USA Cloud Phone Infrastructure Specification",
        provider = "AWS / GCP / Oracle Cloud US Regions (us-east-1, us-west-2, us-east-2)",
        recommendedInstance = "AWS EC2 c6g.metal / c7g.metal (Graviton ARM64) or GCP t2a-standard-8",
        monthlyEstimatedCost = "$28.50 - $42.00 / month (Spot/Reserved ARM64)",
        hourlyCost = "$0.038 - $0.058 / hour",
        virtualizationEngine = "Redroid (Android 14 ARM64) with native kernel binder & ashmem passthrough",
        webrtcGateway = "Janus WebRTC Gateway or LiveKit Cloud Server with H.264 / AV1 hardware encoder",
        turnStunSetup = "Coturn deployed on dual US IP nodes (Virginia 54.210.84.192, Oregon 44.234.19.112)",
        licensingNotes = "Android Open Source Project (AOSP) Apache 2.0 License. Commercial distribution of AOSP is free of royalty. MicroG / Aurora Store integration avoids GMS proprietary bundling restrictions.",
        dockerComposeSample = """
version: '3.8'
services:
  redroid-android14:
    image: redroid/redroid:14.0.0-latest
    container_name: us_cloud_phone_01
    privileged: true
    restart: unless-stopped
    ports:
      - "5555:5555" # ADB
      - "8080:8080" # WebRTC Streaming HTTP
      - "8443:8443" # WSS Signaling
    volumes:
      - /data/android-data:/data
    command:
      - androidboot.hardware=mt6885
      - ro.secure=0
      - ro.boot.hwc=DRM_FB
      - ro.opengles.version=196610
      - redroid.width=1080
      - redroid.height=2400
      - redroid.fps=60
      - redroid.dpi=420

  webrtc-streamer:
    image: mpromonet/webrtc-streamer:latest
    container_name: us_webrtc_streamer
    restart: unless-stopped
    ports:
      - "8000:8000"
    command: ["-H", "0.0.0.0:8000", "-S", "stun.l.google.com:19302"]
""".trimIndent(),
        k8sDeploymentSample = """
apiVersion: apps/v1
kind: Deployment
metadata:
  name: us-cloud-phone-pool
  namespace: cloudphone-us
spec:
  replicas: 3
  selector:
    matchLabels:
      app: us-cloud-phone
  template:
    metadata:
      labels:
        app: us-cloud-phone
        region: us-east-1
    spec:
      nodeSelector:
        kubernetes.io/arch: arm64
      containers:
      - name: redroid-node
        image: redroid/redroid:14.0.0-arm64
        securityContext:
          privileged: true
        resources:
          limits:
            cpu: "4"
            memory: 6Gi
          requests:
            cpu: "2"
            memory: 4Gi
""".trimIndent()
    )
}
