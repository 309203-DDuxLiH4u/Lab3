// AppDatabase.kt
package com.example.imagegallery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imagegallery.data.model.AnalysisResult

@Database(entities = [AnalysisResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageAnalysisDao(): ImageAnalysisDao
}