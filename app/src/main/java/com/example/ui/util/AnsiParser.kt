package com.example.ui.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.AccentColor
import com.example.ui.theme.StatusErrorColor
import com.example.ui.theme.StatusRunningColor
import com.example.ui.theme.StatusStoppedColor
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor

/**
 * Parser de secuencias de escape ANSI para terminales de Baileys/Node.js.
 * Convierte códigos como \u001B[32m a colores reales en Compose AnnotatedString
 * respetando la paleta de monitor de infraestructura.
 */
object AnsiParser {

    private val ANSI_REGEX = Regex("\u001B\\[([0-9;]*)m")

    // Paleta de terminal mapeada al diseño de infraestructura
    private val ANSI_BLACK = Color(0xFF1F2430)
    private val ANSI_RED = StatusErrorColor        // #F85149
    private val ANSI_GREEN = StatusRunningColor    // #3FB950
    private val ANSI_YELLOW = AccentColor          // #F0A868
    private val ANSI_BLUE = Color(0xFF58A6FF)
    private val ANSI_MAGENTA = Color(0xFFBC8CFF)
    private val ANSI_CYAN = Color(0xFF79C0FF)
    private val ANSI_WHITE = TextPrimaryColor      // #E6E8EB
    private val ANSI_DIM = TextSecondaryColor      // #6B7280

    fun parse(text: String): AnnotatedString {
        return buildAnnotatedString {
            var currentIndex = 0
            var currentColor = ANSI_WHITE
            var isBold = false
            var isDim = false

            ANSI_REGEX.findAll(text).forEach { matchResult ->
                // Texto antes de la secuencia ANSI
                val textBefore = text.substring(currentIndex, matchResult.range.first)
                if (textBefore.isNotEmpty()) {
                    val finalColor = if (isDim) ANSI_DIM else currentColor
                    val style = SpanStyle(
                        color = finalColor,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
                    )
                    append(AnnotatedString(textBefore, style))
                }

                // Parsear códigos dentro de \u001B[...m
                val codes = matchResult.groupValues[1].split(';').filter { it.isNotEmpty() }
                if (codes.isEmpty() || codes.contains("0")) {
                    // Reset
                    currentColor = ANSI_WHITE
                    isBold = false
                    isDim = false
                }

                codes.forEach { code ->
                    when (code) {
                        "1" -> isBold = true
                        "2" -> isDim = true
                        "22" -> {
                            isBold = false
                            isDim = false
                        }
                        // Foreground standard
                        "30" -> currentColor = ANSI_BLACK
                        "31" -> currentColor = ANSI_RED
                        "32" -> currentColor = ANSI_GREEN
                        "33" -> currentColor = ANSI_YELLOW
                        "34" -> currentColor = ANSI_BLUE
                        "35" -> currentColor = ANSI_MAGENTA
                        "36" -> currentColor = ANSI_CYAN
                        "37" -> currentColor = ANSI_WHITE
                        "39" -> currentColor = ANSI_WHITE
                        // Bright / high intensity
                        "90" -> currentColor = ANSI_DIM
                        "91" -> currentColor = ANSI_RED
                        "92" -> currentColor = ANSI_GREEN
                        "93" -> currentColor = ANSI_YELLOW
                        "94" -> currentColor = ANSI_BLUE
                        "95" -> currentColor = ANSI_MAGENTA
                        "96" -> currentColor = ANSI_CYAN
                        "97" -> currentColor = ANSI_WHITE
                    }
                }

                currentIndex = matchResult.range.last + 1
            }

            // Añadir el resto de la cadena
            if (currentIndex < text.length) {
                val remaining = text.substring(currentIndex)
                val finalColor = if (isDim) ANSI_DIM else currentColor
                val style = SpanStyle(
                    color = finalColor,
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
                )
                append(AnnotatedString(remaining, style))
            }
        }
    }
}
