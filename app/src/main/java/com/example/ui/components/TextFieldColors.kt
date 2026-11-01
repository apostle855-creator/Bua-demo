package com.example.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.UnimaidNavy
import com.example.ui.theme.White

@Composable
fun unimaidTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = White,
    unfocusedContainerColor = White,
    disabledContainerColor = Color(0xFFF1F5F9),
    cursorColor = UnimaidNavy,
    focusedBorderColor = UnimaidNavy,
    unfocusedBorderColor = Color(0xFFCBD5E1),
    focusedPlaceholderColor = TextMuted,
    unfocusedPlaceholderColor = TextMuted,
    focusedLeadingIconColor = UnimaidNavy,
    unfocusedLeadingIconColor = TextMuted,
    focusedTrailingIconColor = UnimaidNavy,
    unfocusedTrailingIconColor = TextMuted
)
