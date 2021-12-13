package com.example.a19012021038_soundrecorder

import androidx.room.*

@Dao
interface AudioRecordDao {
    @Query("SELECT * FROM audioRecords")
    fun getAll():List<AudioRecord>

    @Insert
    fun insert(vararg audioRecord: AudioRecord)

    @Delete
    fun delete(audioRecord: AudioRecord)

    @Delete
    fun delete(audioRecord: Array<AudioRecord>)

    @Update
    fun update(audioRecord: AudioRecord)
}