package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.ChatMessage
import com.example.data.ai.MessageSource
import com.example.data.ai.OfflineKnowledgeBase
import com.example.ui.components.unimaidTextFieldColors
import com.example.ui.theme.OffWhite
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.UnimaidGold
import com.example.ui.theme.UnimaidGoldDark
import com.example.ui.theme.UnimaidGoldLight
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.UnimaidNavyDark
import com.example.ui.theme.UnimaidNavyLight
import com.example.ui.theme.UnimaidNavyMedium
import com.example.ui.theme.White
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun AiAssistantScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    val isOfflineMode by viewModel.aiManager.isOfflineMode.collectAsState()
    val isModelDownloaded by viewModel.aiManager.isModelDownloaded.collectAsState()
    val isDownloading by viewModel.aiManager.isDownloading.collectAsState()
    val downloadProgress by viewModel.aiManager.downloadProgress.collectAsState()

    var inputPrompt by remember { mutableStateOf("") }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showModelInfoSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedImageBitmap = bitmap
                Toast.makeText(context, "Note/Document photo attached for AI analysis", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Could not load image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // AI Header Bar: Online / Offline Mode Toggle & Model Info
        Surface(
            color = UnimaidNavy,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(UnimaidGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = null,
                                tint = UnimaidNavyDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Departmental AI Assistant",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                            Text(
                                text = if (isOfflineMode) "Offline Local Knowledge Mode" else "Online Gemini AI Mode",
                                fontSize = 11.sp,
                                color = if (isOfflineMode) Color(0xFFFFA000) else Color(0xFF4ADE80),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Mode Switch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isOfflineMode) "Offline" else "Online",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = !isOfflineMode,
                            onCheckedChange = { online ->
                                viewModel.aiManager.setOfflineMode(!online)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = UnimaidGold,
                                checkedTrackColor = UnimaidNavyMedium,
                                uncheckedThumbColor = Color(0xFFFFA000),
                                uncheckedTrackColor = UnimaidNavyDark
                            ),
                            modifier = Modifier.testTag("ai_online_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Offline Package Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isModelDownloaded) Icons.Default.CloudDone else Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = UnimaidGoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isModelDownloaded)
                                "UNIMAID-BusAdmin-v2.1 Knowledge Base (12.4 MB - Installed)"
                            else
                                "Offline Package Not Installed",
                            fontSize = 10.sp,
                            color = UnimaidGoldLight
                        )
                    }

                    if (!isModelDownloaded) {
                        Text(
                            text = if (isDownloading) "Downloading..." else "Download (12.4 MB)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = UnimaidGold,
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.aiManager.downloadOfflineModel {
                                            Toast.makeText(context, "Offline Knowledge Model Ready!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                .padding(4.dp)
                        )
                    }
                }

                if (isDownloading) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { downloadProgress },
                        color = UnimaidGold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }
            }
        }

        // Quick Suggestion Prompts
        Surface(color = White, tonalElevation = 1.dp) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(OfflineKnowledgeBase.getAllQuickPrompts()) { prompt ->
                    AssistChip(
                        onClick = {
                            viewModel.sendChatMessage(prompt, selectedImageBitmap)
                            selectedImageBitmap = null
                        },
                        label = { Text(prompt, fontSize = 11.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = OffWhite,
                            labelColor = UnimaidNavy
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.testTag("prompt_chip_${prompt.hashCode()}")
                    )
                }
            }
        }

        // Chat Messages Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatMessages, key = { it.id }) { msg ->
                ChatBubbleItem(message = msg)
            }

            if (isChatLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = UnimaidNavy,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOfflineMode) "Consulting local UNIMAID knowledge base..." else "Reasoning with Gemini...",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Attached Photo Preview (if any)
        selectedImageBitmap?.let { bmp ->
            Surface(
                color = White,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Attached Photo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Photo of study note attached",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "AI will analyze text, formulas and diagrams",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = { selectedImageBitmap = null }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFDC2626))
                    }
                }
            }
        }

        // Input Bar
        Surface(
            color = White,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo Attachment Button
                IconButton(
                    onClick = { photoPickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(OffWhite)
                        .testTag("attach_photo_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Attach lecture note photo",
                        tint = UnimaidNavy,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = inputPrompt,
                    onValueChange = { inputPrompt = it },
                    placeholder = {
                        Text(
                            text = if (isOfflineMode) "Ask about courses, Fayol's 14 principles, CGPA..." else "Ask any academic question or analyze notes...",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
                    colors = unimaidTextFieldColors(),
                    maxLines = 4,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        val query = inputPrompt
                        val bmp = selectedImageBitmap
                        if (query.isNotBlank() || bmp != null) {
                            inputPrompt = ""
                            selectedImageBitmap = null
                            viewModel.sendChatMessage(query, bmp)
                        }
                    },
                    enabled = (inputPrompt.isNotBlank() || selectedImageBitmap != null) && !isChatLoading,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (inputPrompt.isNotBlank() || selectedImageBitmap != null) UnimaidNavy else Color(0xFFE2E8F0))
                        .testTag("send_ai_message_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (inputPrompt.isNotBlank() || selectedImageBitmap != null) White else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(UnimaidNavy),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = UnimaidGold,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            color = if (isUser) UnimaidNavy else White,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            tonalElevation = if (isUser) 0.dp else 2.dp,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header badge for bot
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Surface(
                            color = when (message.source) {
                                MessageSource.OFFLINE_LOCAL -> Color(0xFFFEF3C7)
                                MessageSource.ONLINE_GEMINI -> Color(0xFFDCFCE7)
                                else -> Color(0xFFE0EDFD)
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = when (message.source) {
                                    MessageSource.OFFLINE_LOCAL -> "Offline Knowledge Engine"
                                    MessageSource.ONLINE_GEMINI -> "Gemini AI"
                                    else -> "Departmental System"
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (message.source) {
                                    MessageSource.OFFLINE_LOCAL -> UnimaidGoldDark
                                    MessageSource.ONLINE_GEMINI -> SuccessGreen
                                    else -> UnimaidNavy
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (isUser) White else TextPrimary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.timestamp,
                    fontSize = 10.sp,
                    color = if (isUser) Color(0xFFCAD7E8) else TextMuted,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
