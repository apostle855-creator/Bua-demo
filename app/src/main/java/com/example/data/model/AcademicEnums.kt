package com.example.data.model

enum class Level(val displayName: String, val shortName: String, val description: String) {
    LEVEL_100("100 Level", "100L", "Year 1 Foundation"),
    LEVEL_200("200 Level", "200L", "Year 2 Intermediate"),
    LEVEL_300("300 Level", "300L", "Year 3 Advanced"),
    LEVEL_400("400 Level", "400L", "Year 4 Final Year");

    companion object {
        fun fromString(value: String): Level {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true) ||
                        it.shortName.equals(value, ignoreCase = true)
            } ?: LEVEL_100
        }
    }
}

enum class ResourceType(val displayName: String, val iconName: String) {
    PAST_QUESTION("Past Questions", "quiz"),
    LECTURE_NOTE("Lecture Notes", "menu_book"),
    ASSIGNMENT("Assignments", "assignment"),
    TEXTBOOK("Textbooks", "auto_stories"),
    STUDY_GUIDE("Study Guides", "lightbulb"),
    OTHER("Other Documents", "description");

    companion object {
        fun fromString(value: String): ResourceType {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true)
            } ?: OTHER
        }
    }
}

enum class ModerationStatus(val label: String) {
    APPROVED("Approved"),
    PENDING("Pending Review"),
    REJECTED("Rejected"),
    REMOVED("Removed")
}
