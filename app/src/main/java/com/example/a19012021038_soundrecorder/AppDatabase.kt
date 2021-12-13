package com.example.a19012021038_soundrecorder

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(AudioRecord::class),version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun audioRecordDao() : AudioRecordDao
}