package com.example.imagegallery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "analysis_results")
data class AnalysisResult(
    @PrimaryKey val imageId: Int,
    val tagsJson: String
)