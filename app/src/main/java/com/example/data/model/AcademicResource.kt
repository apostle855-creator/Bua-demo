package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "academic_resources")
data class AcademicResource(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val courseCode: String,
    val courseTitle: String,
    val level: String, // Level.name
    val resourceType: String, // ResourceType.name
    val fileFormat: String, // PDF, DOCX, PPTX, etc.
    val fileSizeMb: Double,
    val uploadDate: String,
    val contributorName: String,
    val contributorMatric: String = "",
    val moderationStatus: String = ModerationStatus.APPROVED.name,
    val moderationNote: String = "",
    val downloadCount: Int = 0,
    val flagCount: Int = 0,
    val previewSummary: String = "",
    val localUri: String = ""
)
