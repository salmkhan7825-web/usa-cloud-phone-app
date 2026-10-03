package com.example.cloudphone.model

enum class TransferDirection {
    UPLOAD_TO_CLOUD,
    DOWNLOAD_FROM_CLOUD
}

enum class TransferStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}

data class FileTransferItem(
    val id: String,
    val fileName: String,
    val fileSizeBytes: Long,
    val direction: TransferDirection,
    val status: TransferStatus,
    val progressPercent: Int,
    val timestamp: Long = System.currentTimeMillis()
)
