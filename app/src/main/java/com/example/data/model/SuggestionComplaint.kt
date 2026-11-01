package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SuggestionCategory(val displayName: String) {
    ACADEMIC_ISSUES("Academic Issues"),
    DEPARTMENTAL_IMPROVEMENT("Departmental Improvement"),
    STUDENT_WELFARE("Student Welfare"),
    ACADEMIC_PROGRAMMES("Academic Programmes"),
    COMPLAINTS("Complaints"),
    GENERAL_SUGGESTIONS("General Suggestions");

    companion object {
        fun fromString(value: String): SuggestionCategory {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            } ?: GENERAL_SUGGESTIONS
        }
    }
}

enum class SuggestionStatus(val displayName: String) {
    PENDING("Pending"),
    UNDER_REVIEW("Under Review"),
    RESOLVED("Resolved"),
    REJECTED("Rejected")
}

@Entity(tableName = "suggestions_complaints")
data class SuggestionComplaint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ticketNumber: String,
    val subject: String,
    val category: String, // SuggestionCategory.name
    val message: String,
    val studentName: String = "",
    val studentContact: String = "", // Email or Phone
    val isAnonymous: Boolean = false,
    val status: String = SuggestionStatus.PENDING.name,
    val excosResponse: String = "",
    val respondedBy: String = "",
    val submissionDate: String,
    val resolutionDate: String = ""
)
