package dev.eknath.jot.ui.constants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class JOTColors(val id: Long, val dark: Color, val light: Color) {
    RED(id = 5001L, dark = Color(0XFF42221F), light = Color(0xFFAE2E24)),
    ORANGE(id = 5002L, dark = Color(0XFF702E00), light = Color(0XFFA54800)),
    GREEN(id = 5003, dark = Color(0XFF37471F), light = Color(0xFF4C6B1F)),
    TEAL(id = 5004L, dark = Color(0XFF164555), light = Color(0xFF227D9B)),
}