package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BotItem
import com.example.ui.theme.AccentBg
import com.example.ui.theme.AccentColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.PageBg
import com.example.ui.theme.StatusRunningColor
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont

@Composable
fun IndexEditorScreen(
    bot: BotItem,
    filePath: String = "index.js",
    onBackClick: () -> Unit,
    onSaveFile: (fileName: String, content: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialCode = remember(bot.name, filePath) {
        """// ============================================================
// BOT: ${bot.name} (Termux Node.js runtime)
// ARCHIVO: $filePath
// ============================================================
const { default: makeWASocket, useMultiFileAuthState, DisconnectReason } = require('@whiskeysockets/baileys');
const pino = require('pino');
const path = require('path');
const fs = require('fs');

// Configuración crítica de ruta de ejecución
const BOT_NAME = '${bot.name}';
const BOT_PATH = '/data/data/com.termux/files/home/${bot.name}';
const AUTH_DIR = path.join(BOT_PATH, '${bot.authFolder}');

const CONFIG = {
    botPath: BOT_PATH,
    adminId: '5491123456789@s.whatsapp.net',
    ruletaDataDir: '/storage/emulated/0/Download/RULETA',
    autoRestart: true
};

async function startBot() {
    console.log(`[${bot.name}] Iniciando sesión en ${'$'}{BOT_PATH}...`);
    
    const { state, saveCreds } = await useMultiFileAuthState(AUTH_DIR);
    
    const sock = makeWASocket({
        auth: state,
        logger: pino({ level: 'info' }),
        printQRInTerminal: true,
        browser: ['Termux-Android', 'Chrome', '1.0.0']
    });

    sock.ev.on('creds.update', saveCreds);

    sock.ev.on('connection.update', (update) => {
        const { connection, lastDisconnect } = update;
        if (connection === 'close') {
            const shouldReconnect = (lastDisconnect?.error?.output?.statusCode !== DisconnectReason.loggedOut);
            console.log(`[${bot.name}] Conexión cerrada. Reconectando: ${'$'}{shouldReconnect}`);
            if (shouldReconnect) startBot();
        } else if (connection === 'open') {
            console.log(`[${bot.name}] Conexión abierta exitosamente!`);
        }
    });

    sock.ev.on('messages.upsert', async ({ messages }) => {
        const msg = messages[0];
        if (!msg.message || msg.key.fromMe) return;
        console.log(`[${bot.name}] Mensaje recibido de ${'$'}{msg.key.remoteJid}`);
    });
}

startBot();
"""
    }

    var codeContent by remember { mutableStateOf(initialCode) }
    var isSavedNotice by remember { mutableStateOf(false) }

    val lineCount = remember(codeContent) {
        codeContent.lines().size
    }

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PageBg,
        topBar = {
            // Header del editor (Inter para UI, JetBrains Mono para rutas)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .border(1.dp, DividerColor)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(36.dp).testTag("btn_back_from_editor")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Volver",
                                tint = TextPrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "EDITOR DE ARCHIVO // ",
                                    fontFamily = UiFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = AccentColor
                                )
                                Text(
                                    text = bot.name,
                                    fontFamily = TechnicalFont,
                                    fontSize = 12.sp,
                                    color = TextPrimaryColor
                                )
                            }
                            Text(
                                text = "~/${bot.name}/$filePath",
                                fontFamily = TechnicalFont,
                                fontSize = 11.sp,
                                color = TextSecondaryColor
                            )
                        }
                    }

                    // Acciones: Reset y Guardar (SFTP)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                codeContent = initialCode
                                isSavedNotice = false
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Recargar original",
                                tint = TextSecondaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Botón GUARDAR en acento ámbar (#F0A868)
                        Row(
                            modifier = Modifier
                                .background(
                                    if (isSavedNotice) CardBg else AccentBg,
                                    RoundedCornerShape(2.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSavedNotice) StatusRunningColor else AccentColor,
                                    RoundedCornerShape(2.dp)
                                )
                                .clickable {
                                    onSaveFile(filePath, codeContent)
                                    isSavedNotice = true
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("btn_save_index_file"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSavedNotice) Icons.Outlined.Check else Icons.Outlined.Save,
                                contentDescription = "Guardar",
                                tint = if (isSavedNotice) StatusRunningColor else AccentColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isSavedNotice) "GUARDADO SFTP" else "GUARDAR",
                                fontFamily = UiFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isSavedNotice) StatusRunningColor else AccentColor
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Barra de estado del editor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .border(1.dp, DividerColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "JavaScript (Node.js ES6/CommonJS) • UTF-8",
                        fontFamily = UiFont,
                        fontSize = 10.sp,
                        color = TextSecondaryColor
                    )
                    Text(
                        text = "Líneas: $lineCount | Caracteres: ${codeContent.length}",
                        fontFamily = TechnicalFont,
                        fontSize = 10.sp,
                        color = AccentColor
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PageBg)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState)
            ) {
                // Columna de números de línea (JetBrains Mono)
                Column(
                    modifier = Modifier
                        .background(CodeBg)
                        .border(width = 1.dp, color = DividerColor)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in 1..lineCount) {
                        Text(
                            text = "$i",
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = TextSecondaryColor,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Área de código editable con scroll horizontal y vertical
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    BasicTextField(
                        value = codeContent,
                        onValueChange = {
                            codeContent = it
                            isSavedNotice = false
                        },
                        textStyle = TextStyle(
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = TextPrimaryColor,
                            lineHeight = 18.sp
                        ),
                        cursorBrush = SolidColor(AccentColor),
                        modifier = Modifier.testTag("code_editor_textarea")
                    )
                }
            }
        }
    }
}
