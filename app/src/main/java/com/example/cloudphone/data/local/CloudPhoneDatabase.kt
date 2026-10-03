package com.example.cloudphone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cloudphone.data.local.dao.CloudPhoneDao
import com.example.cloudphone.data.local.entity.FileTransferEntity
import com.example.cloudphone.data.local.entity.SessionLogEntity
import com.example.cloudphone.data.local.entity.VirtualAppEntity
import com.example.cloudphone.data.local.entity.VirtualDeviceConfigEntity

@Database(
    entities = [
        VirtualAppEntity::class,
        SessionLogEntity::class,
        FileTransferEntity::class,
        VirtualDeviceConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CloudPhoneDatabase : RoomDatabase() {
    abstract fun cloudPhoneDao(): CloudPhoneDao

    companion object {
        @Volatile
        private var INSTANCE: CloudPhoneDatabase? = null

        fun getInstance(context: Context): CloudPhoneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CloudPhoneDatabase::class.java,
                    "us_cloud_phone.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
