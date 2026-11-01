package com.example.data.ai

enum class MessageSource {
    OFFLINE_LOCAL,
    ONLINE_GEMINI,
    SYSTEM
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: String,
    val imageUri: String? = null,
    val source: MessageSource = MessageSource.SYSTEM,
    val suggestedActions: List<String> = emptyList()
)
