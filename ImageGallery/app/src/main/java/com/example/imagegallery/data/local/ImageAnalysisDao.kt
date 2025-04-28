package com.example.imagegallery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imagegallery.data.model.AnalysisResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Dao
interface ImageAnalysisDao {

    // Lưu kết quả phân tích, thay thế nếu đã tồn tại cho imageId đó
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(result: AnalysisResult)

    // Lấy kết quả phân tích dưới dạng JSON string
    @Query("SELECT tagsJson FROM analysis_results WHERE imageId = :id")
    suspend fun getAnalysisJson(id: Int): String?

    // Hàm tiện ích để lấy List<String> trực tiếp
    suspend fun getAnalysis(id: Int): List<String>? {
        val json = getAnalysisJson(id)
        return if (json != null) {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(json, type)
        } else {
            null
        }
    }
}