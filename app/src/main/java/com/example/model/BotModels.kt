package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.StatusErrorColor
import com.example.ui.theme.StatusRunningColor
import com.example.ui.theme.StatusStoppedColor

enum class BotStatus(
    val label: String,
    val color: Color
) {
    RUNNING("RUNNING", StatusRunningColor),
    STOPPED("STOPPED", StatusStoppedColor),
    ERROR("ERROR / CRASH", StatusErrorColor)
}

data class ServerConfig(
    val host: String = "100.91.23.52",
    val port: Int = 8022,
    val user: String = "u0_a112",
    val pass: String = "3623",
    val credFolder: String = "auth_info",
    val termuxHome: String = "/data/data/com.termux/files/home",
    val downloadRoot: String = "/storage/emulated/0/Download",
    val watchdogCommand: String = "bash ~/comandos/vigilante.sh"
)

data class BotItem(
    val id: String,
    val name: String,
    val status: BotStatus,
    val pid: Int?,
    val tmuxSession: String,
    val remotePath: String,
    val uptime: String,
    val memoryRss: String,
    val cpuPercent: String,
    val authFolder: String = "auth_info",
    val watchdogScript: String,
    val lastLogSnippet: String,
    val nodeVersion: String = "v20.11.1",
    val hasUnsavedChanges: Boolean = false
)

object MockData {
    val serverConfig = ServerConfig()

    // Configuración y rutas reales de Termux extraídas del script .bat de referencia
    val sampleBots = listOf(
        BotItem(
            id = "whatsapp-bot",
            name = "whatsapp-bot",
            status = BotStatus.RUNNING,
            pid = 14829,
            tmuxSession = "whatsapp-bot",
            remotePath = "/data/data/com.termux/files/home/whatsapp-bot",
            uptime = "4d 18h 32m",
            memoryRss = "142.4 MB",
            cpuPercent = "0.8%",
            authFolder = "auth_info",
            watchdogScript = "bash ~/comandos/vigilante.sh whatsapp-bot",
            lastLogSnippet = "[18:24:10] [baileys] Noise handshake OK. Conexión abierta en +54 9 11 2345-6789",
            nodeVersion = "v20.11.1"
        ),
        BotItem(
            id = "whatsapp-bot-2",
            name = "whatsapp-bot-2",
            status = BotStatus.RUNNING,
            pid = 14835,
            tmuxSession = "whatsapp-bot-2",
            remotePath = "/data/data/com.termux/files/home/whatsapp-bot-2",
            uptime = "12d 04h 10m",
            memoryRss = "189.1 MB",
            cpuPercent = "1.2%",
            authFolder = "auth_info",
            watchdogScript = "bash ~/comandos/vigilante.sh whatsapp-bot-2",
            lastLogSnippet = "[18:25:02] [baileys] Mensaje procesado [ID: 3EB0C89...]",
            nodeVersion = "v20.11.1"
        ),
        BotItem(
            id = "whatsapp-bot-3",
            name = "whatsapp-bot-3",
            status = BotStatus.STOPPED,
            pid = null,
            tmuxSession = "whatsapp-bot-3 (inactivo)",
            remotePath = "/data/data/com.termux/files/home/whatsapp-bot-3",
            uptime = "Detenido por operador",
            memoryRss = "0 MB",
            cpuPercent = "0.0%",
            authFolder = "auth_info",
            watchdogScript = "bash ~/comandos/vigilante.sh whatsapp-bot-3",
            lastLogSnippet = "[16:10:44] [tmux] Sesión finalizada vía tmux kill-session -t whatsapp-bot-3",
            nodeVersion = "v20.11.1"
        ),
        BotItem(
            id = "whatsapp-bot-4",
            name = "whatsapp-bot-4",
            status = BotStatus.RUNNING,
            pid = 16201,
            tmuxSession = "whatsapp-bot-4",
            remotePath = "/data/data/com.termux/files/home/whatsapp-bot-4",
            uptime = "1d 02h 45m",
            memoryRss = "118.0 MB",
            cpuPercent = "0.4%",
            authFolder = "auth_info",
            watchdogScript = "bash ~/comandos/vigilante.sh whatsapp-bot-4",
            lastLogSnippet = "[18:23:40] [baileys] Sesión vinculada exitosamente. Escuchando eventos.",
            nodeVersion = "v20.11.1"
        )
    )

    // Rutas guardadas frecuentes en la carpeta de un bot (del archivo rutas.txt del .bat)
    val savedFilePaths = listOf(
        "index.js",
        "data/ruleta/historico_general.json",
        "data/ruleta/sesiones_activas.json",
        "data/ruleta/estrategia.json",
        "botpath.json"
    )
}
