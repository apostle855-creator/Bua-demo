package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String, // e.g., BUS 101
    val title: String, // e.g., Introduction to Business I
    val level: String, // Level.name
    val semester: String, // First Semester / Second Semester
    val creditUnits: Int,
    val description: String = "",
    val lecturerInCharge: String = ""
)

@Entity(tableName = "resource_reports")
data class ResourceReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val resourceId: Long,
    val resourceTitle: String,
    val courseCode: String,
    val reason: String, // "Inaccurate Content", "Wrong Course", "Poor Quality", "Copyright", "Other"
    val details: String,
    val reportDate: String,
    val isResolved: Boolean = false,
    val resolvedBy: String = ""
)

@Entity(tableName = "department_announcements")
data class DepartmentAnnouncement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: String,
    val author: String,
    val priority: String = "NORMAL" // "URGENT", "NORMAL", "EXAM"
)

@Entity(tableName = "excos_members")
data class ExcosMember(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val roleTitle: String, // President, Academic Secretary, Welfare Director, etc.
    val session: String, // e.g. "2025/2026 Academic Session"
    val phone: String,
    val email: String,
    val matricNumber: String,
    val accessPasscode: String = "2026", // Security passcode
    val isCurrent: Boolean = true
)
