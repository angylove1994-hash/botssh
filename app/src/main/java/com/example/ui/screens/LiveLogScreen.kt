package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.VerticalAlignBottom
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BotItem
import com.example.model.BotStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentBg
import com.example.ui.theme.AccentColor
import com.example.ui.theme.BadgeBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.PageBg
import com.example.ui.theme.StatusRunningColor
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont
import com.example.ui.util.AnsiParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LiveLogScreen(
    bot: BotItem,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Logs simulados con códigos de color ANSI auténticos de Baileys / Pino logger
    val initialLogs = listOf(
        "\u001B[90m[18:24:00.102]\u001B[0m \u001B[36m[socket]\u001B[0m inicializando conexión con wss://web.whatsapp.com/ws/chat...",
        "\u001B[90m[18:24:00.540]\u001B[0m \u001B[36m[auth]\u001B[0m cargando llaves criptográficas desde \u001B[33m./auth_info\u001B[0m",
        "\u001B[90m[18:24:01.210]\u001B[0m \u001B[32m[auth]\u001B[0m credenciales de sesión validadas (Noise handshake OK)",
        "\u001B[90m[18:24:02.890]\u001B[0m \u001B[36m[baileys]\u001B[0m \u001B[1mconnection.update\u001B[0m: { connection: '\u001B[33mconnecting\u001B[0m' }",
        "\u001B[90m[18:24:03.412]\u001B[0m \u001B[32m[baileys]\u001B[0m \u001B[1mconnection.update\u001B[0m: { connection: '\u001B[32mopen\u001B[0m' }",
        "\u001B[90m[18:24:03.620]\u001B[0m \u001B[32m[bot]\u001B[0m WhatsApp bot iniciado exitosamente en \u001B[1m\u001B[32m+54 9 11 2345-6789\u001B[0m",
        "\u001B[90m[18:24:04.015]\u001B[0m \u001B[90m[sync]\u001B[0m sincronizando chats recientes: 14 conversaciones cacheadas",
        "\u001B[90m[18:24:15.820]\u001B[0m \u001B[36m[msg:recv]\u001B[0m de \u001B[33m5491198765432@s.whatsapp.net\u001B[0m: \"!menu\"",
        "\u001B[90m[18:24:16.104]\u001B[0m \u001B[32m[msg:send]\u001B[0m enviando respuesta automática [142 bytes - OK]",
        "\u001B[90m[18:24:30.001]\u001B[0m \u001B[90m[keepalive]\u001B[0m ping ack recibido (latencia: 24ms)",
        "\u001B[90m[18:24:45.002]\u001B[0m \u001B[90m[keepalive]\u001B[0m ping ack recibido (latencia: 22ms)"
    )

    val logs = remember { mutableStateListOf<String>().apply { addAll(initialLogs) } }
    var inputText by remember { mutableStateOf("") }
    var autoScrollEnabled by remember { mutableStateOf(true) }

    // Simulación periódica de streaming en tiempo real (FASE 1 Mock)
    LaunchedEffect(autoScrollEnabled) {
        var counter = 1
        while (true) {
            delay(4000)
            val timestamp = "18:25:${String.format("%02d", (counter * 4) % 60)}"
            val simulatedEvent = when (counter % 3) {
                0 -> "\u001B[90m[$timestamp.100]\u001B[0m \u001B[90m[keepalive]\u001B[0m ping ack recibido (latencia: ${20 + (counter % 15)}ms)"
                1 -> "\u001B[90m[$timestamp.420]\u001B[0m \u001B[36m[event]\u001B[0m presencia actualizada: disponible en sesión tmux"
                else -> "\u001B[90m[$timestamp.890]\u001B[0m \u001B[32m[sync]\u001B[0m estado de batería del dispositivo: 94% [cargando]"
            }
            logs.add(simulatedEvent)
            counter++

            if (autoScrollEnabled && logs.isNotEmpty()) {
                listState.animateScrollToItem(logs.size - 1)
            }
        }
    }

    fun handleSendInput() {
        if (inputText.isBlank()) return
        val textToSend = inputText.trim()
        inputText = ""

        // Eco en la consola del comando enviado por stdin
        logs.add("\u001B[33m❯ [stdin enviado]: $textToSend\u001B[0m")

        // Respuesta simulada interactiva si ingresa pairing code o número
        coroutineScope.launch {
            if (autoScrollEnabled) {
                listState.animateScrollToItem(logs.size - 1)
            }
            delay(600)
            if (textToSend.contains(Regex("[0-9]{8,}"))) {
                logs.add("\u001B[32m[baileys] Número de teléfono detectado. Solicitando pairing code...\u001B[0m")
                delay(800)
                logs.add("\u001B[32m[baileys] Código de vinculación generado: \u001B[1m\u001B[33m7K92-B4X1\u001B[0m")
            } else {
                logs.add("\u001B[90m[tmux:in]\u001B[0m tmux send-keys -t ${bot.name} \"$textToSend\" Enter")
            }
            if (autoScrollEnabled) {
                listState.animateScrollToItem(logs.size - 1)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PageBg,
        topBar = {
            // Cabecera de terminal en vivo (Lenguaje visual consistente)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .border(width = 1.dp, color = DividerColor)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        // Botón Volver con tipografía Inter
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_back_from_logs")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Volver al monitor",
                                tint = TextPrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // REGLA OBLIGATORIA: Punto de color de estado (mismo lenguaje)
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(bot.status.color)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Nombre de bot en JetBrains Mono (TechnicalFont)
                        Text(
                            text = bot.name,
                            fontFamily = TechnicalFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimaryColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Status Badge en Inter (UiFont)
                        StatusBadge(status = bot.status)
                    }

                    // Acciones de la consola: Auto-scroll y limpiar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Toggle Follow / Auto-scroll con acento ámbar (#F0A868)
                        Row(
                            modifier = Modifier
                                .background(
                                    if (autoScrollEnabled) AccentBg else BadgeBg,
                                    RoundedCornerShape(2.dp)
                                )
                                .border(
                                    1.dp,
                                    if (autoScrollEnabled) AccentColor else DividerColor,
                                    RoundedCornerShape(2.dp)
                                )
                                .clickable { autoScrollEnabled = !autoScrollEnabled }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.VerticalAlignBottom,
                                contentDescription = "Auto scroll",
                                tint = if (autoScrollEnabled) AccentColor else TextSecondaryColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (autoScrollEnabled) "FOLLOW" else "PAUSA",
                                fontFamily = UiFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = if (autoScrollEnabled) AccentColor else TextSecondaryColor
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Botón Limpiar buffer
                        IconButton(
                            onClick = { logs.clear() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteSweep,
                                contentDescription = "Limpiar consola",
                                tint = TextSecondaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Fila de metadatos de sesión (JetBrains Mono para sesión y PID, Inter para etiquetas)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sesión tmux: ",
                            fontFamily = UiFont,
                            fontSize = 11.sp,
                            color = TextSecondaryColor
                        )
                        Text(
                            text = bot.tmuxSession,
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = TextPrimaryColor
                        )
                        if (bot.pid != null) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "PID: ",
                                fontFamily = UiFont,
                                fontSize = 11.sp,
                                color = TextSecondaryColor
                            )
                            Text(
                                text = "${bot.pid}",
                                fontFamily = TechnicalFont,
                                fontSize = 11.sp,
                                color = TextPrimaryColor
                            )
                        }
                    }

                    Text(
                        text = "SSH STREAM (FASE 1)",
                        fontFamily = UiFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = AccentColor
                    )
                }
            }
        },
        bottomBar = {
            // Campo de input inferior para enviar texto/comandos a la sesión de tmux
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .border(1.dp, DividerColor)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Prompt de terminal en ámbar (#F0A868)
                    Text(
                        text = "❯",
                        fontFamily = TechnicalFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AccentColor,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    // Campo de entrada de texto
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "Enviar texto (pairing code, teléfono, comando)...",
                                fontFamily = UiFont,
                                fontSize = 12.sp,
                                color = TextSecondaryColor
                            )
                        },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = TechnicalFont,
                            fontSize = 12.sp,
                            color = TextPrimaryColor
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { handleSendInput() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CodeBg,
                            unfocusedContainerColor = CodeBg,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor,
                            cursorColor = AccentColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("terminal_input_field")
                    )

                    // Botón Enviar con acento ámbar #F0A868
                    Row(
                        modifier = Modifier
                            .height(44.dp)
                            .background(AccentBg, RoundedCornerShape(2.dp))
                            .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                            .clickable { handleSendInput() }
                            .padding(horizontal = 12.dp)
                            .testTag("terminal_send_button"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Enviar",
                            tint = AccentColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Enviar",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = AccentColor
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Área principal de la terminal en fondo #0A0E14
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PageBg)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .testTag("terminal_log_output"),
                contentPadding = PaddingValues(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                items(logs) { line ->
                    // Parser ANSI renderiza secuencias de color reales en JetBrains Mono
                    Text(
                        text = AnsiParser.parse(line),
                        fontFamily = TechnicalFont,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
