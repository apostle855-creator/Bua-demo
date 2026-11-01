package com.example

import com.example.data.ai.OfflineKnowledgeBase
import com.example.data.model.Level
import com.example.data.model.ResourceType
import com.example.data.model.SuggestionCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testLevelParsing() {
        assertEquals(Level.LEVEL_100, Level.fromString("100L"))
        assertEquals(Level.LEVEL_200, Level.fromString("200 Level"))
        assertEquals(Level.LEVEL_300, Level.fromString("300L"))
        assertEquals(Level.LEVEL_400, Level.fromString("400 Level"))
    }

    @Test
    fun testResourceTypeParsing() {
        assertEquals(ResourceType.PAST_QUESTION, ResourceType.fromString("Past Questions"))
        assertEquals(ResourceType.LECTURE_NOTE, ResourceType.fromString("Lecture Notes"))
        assertEquals(ResourceType.ASSIGNMENT, ResourceType.fromString("Assignments"))
    }

    @Test
    fun testSuggestionCategoryParsing() {
        assertEquals(SuggestionCategory.ACADEMIC_ISSUES, SuggestionCategory.fromString("Academic Issues"))
        assertEquals(SuggestionCategory.STUDENT_WELFARE, SuggestionCategory.fromString("Student Welfare"))
        assertEquals(SuggestionCategory.COMPLAINTS, SuggestionCategory.fromString("Complaints"))
    }

    @Test
    fun testOfflineKnowledgeBaseSearch() {
        val fayolResult = OfflineKnowledgeBase.search("Explain Fayol 14 principles of management")
        assertNotNull(fayolResult)
        assertTrue(fayolResult!!.contains("Henri Fayol"))

        val cgpaResult = OfflineKnowledgeBase.search("How is CGPA calculated at UNIMAID?")
        assertNotNull(cgpaResult)
        assertTrue(cgpaResult!!.contains("5.00"))

        val uploadResult = OfflineKnowledgeBase.search("How do I upload lecture notes?")
        assertNotNull(uploadResult)
        assertTrue(uploadResult!!.contains("100MB"))

        val pinResult = OfflineKnowledgeBase.search("How to change EXCOS portal pin?")
        assertNotNull(pinResult)
        assertTrue(pinResult!!.contains("2026"))
        assertTrue(pinResult!!.contains("Change EXCOS Portal PIN"))

        val logoutResult = OfflineKnowledgeBase.search("How do I logout of the excos portal?")
        assertNotNull(logoutResult)
        assertTrue(logoutResult!!.contains("Logout"))
    }
}

