package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BotItem
import com.example.model.BotStatus
import com.example.model.MockData
import com.example.model.ServerConfig
import com.example.ui.components.BotRowCard
import com.example.ui.components.ClearCredentialsDialog
import com.example.ui.components.KillAllProcessesDialog
import com.example.ui.components.ServerHeader
import com.example.ui.screens.CloneBotDialog
import com.example.ui.screens.FileManagerScreen
import com.example.ui.screens.IndexEditorScreen
import com.example.ui.screens.LiveLogScreen
import com.example.ui.screens.QuickCommandsScreen
import com.example.ui.screens.ServerSettingsScreen
import com.example.ui.theme.AccentBg
import com.example.ui.theme.AccentColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.PageBg
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont
import kotlinx.coroutines.launch

@Composable
fun BotMonitorScreen(
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Configuración activa del servidor Termux
    var serverConfig by remember { mutableStateOf(MockData.serverConfig) }

    // Lista de bots con estado real de Termux
    var botsList by remember { mutableStateOf(MockData.sampleBots) }
    var filterText by remember { mutableStateOf("") }

    // Estado expandido interactivo: por defecto "whatsapp-bot" está expandido
    val expandedStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            put("whatsapp-bot", true)
            put("whatsapp-bot-2", false)
            put("whatsapp-bot-3", false)
            put("whatsapp-bot-4", false)
        }
    }

    // Estados de navegación entre pantallas del sistema
    var selectedBotForLogs by remember { mutableStateOf<BotItem?>(null) }
    var selectedBotForEditor by remember { mutableStateOf<Pair<BotItem, String>?>(null) }
    var selectedBotForFiles by remember { mutableStateOf<BotItem?>(null) }
    var showServerSettings by remember { mutableStateOf(false) }
    var showQuickCommands by remember { mutableStateOf(false) }

    // Estados para diálogos
    var botToClearCredentials by remember { mutableStateOf<BotItem?>(null) }
    var showKillAllDialog by remember { mutableStateOf(false) }
    var showCloneDialogForBot by remember { mutableStateOf<BotItem?>(null) }
    var showCloneGeneralDialog by remember { mutableStateOf(false) }

    // Último comando ejecutado en la sesión SSH hacia Termux
    var lastTerminalFeedback by remember {
        mutableStateOf("SSH direct socket OK [100.91.23.52:8022 (u0_a112)]. Termux home: /data/data/com.termux/files/home")
    }

    fun showFeedback(msg: String) {
        lastTerminalFeedback = msg
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
        }
    }

    // 1. PANTALLA: Logs en vivo (Opción 9)
    if (selectedBotForLogs != null) {
        LiveLogScreen(
            bot = selectedBotForLogs!!,
            onBackClick = { selectedBotForLogs = null },
            modifier = modifier
        )
        return
    }

    // 2. PANTALLA: Editor de archivos / index.js (Opción 13)
    if (selectedBotForEditor != null) {
        val (bot, path) = selectedBotForEditor!!
        IndexEditorScreen(
            bot = bot,
            filePath = path,
            onBackClick = { selectedBotForEditor = null },
            onSaveFile = { fileName, _ ->
                showFeedback("SFTP: ~/${bot.name}/$fileName guardado exitosamente.")
            },
            modifier = modifier
        )
        return
    }

    // 3. PANTALLA: Gestor de Archivos / SFTP / Descargas (Opciones 11 y 12)
    if (selectedBotForFiles != null) {
        FileManagerScreen(
            currentBot = selectedBotForFiles!!,
            onBackClick = { selectedBotForFiles = null },
            onOpenFileInEditor = { relativePath ->
                selectedBotForEditor = Pair(selectedBotForFiles!!, relativePath)
            },
            onDownloadFile = { remotePath ->
                showFeedback("pscp -P 8022 u0_a112@100.91.23.52:$remotePath C:\\Downloads\\")
            },
            onUploadFile = { destinationPath, filename ->
                showFeedback("pscp -P 8022 $filename u0_a112@100.91.23.52:$destinationPath")
            },
            modifier = modifier
        )
        return
    }

    // 4. PANTALLA: Configuración del Servidor SSH Termux (Opción 1)
    if (showServerSettings) {
        ServerSettingsScreen(
            currentConfig = serverConfig,
            onBackClick = { showServerSettings = false },
            onSaveConfig = { newConfig ->
                serverConfig = newConfig
                showFeedback("Configuración actualizada: ${newConfig.user}@${newConfig.host}:${newConfig.port}")
            },
            onTestSsh = {
                showFeedback("plink -P ${serverConfig.port} -pw **** ${serverConfig.user}@${serverConfig.host} [CONEXIÓN OK]")
            },
            modifier = modifier
        )
        return
    }

    // 5. PANTALLA: Consola y Comandos Rápidos (tmux ls, free, ps, etc.)
    if (showQuickCommands) {
        QuickCommandsScreen(
            onBackClick = { showQuickCommands = false },
            onExecuteCommand = { cmd ->
                showFeedback("plink: $cmd")
            },
            modifier = modifier
        )
        return
    }

    val filteredBots = botsList.filter {
        if (filterText.isBlank()) true
        else it.name.contains(filterText.trim(), ignoreCase = true) ||
                it.remotePath.contains(filterText.trim(), ignoreCase = true)
    }

    val activeBotsCount = botsList.count { it.status == BotStatus.RUNNING }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PageBg,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(12.dp)
            ) { data ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBg, RoundedCornerShape(2.dp))
                        .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Terminal,
                            contentDescription = null,
                            tint = AccentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = data.visuals.message,
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = TextPrimaryColor
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Barra de estado de consola inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CodeBg)
                    .border(1.dp, DividerColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
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
                        Text(
                            text = "❯ CMD: ",
                            fontFamily = TechnicalFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = AccentColor
                        )
                        Text(
                            text = lastTerminalFeedback,
                            fontFamily = TechnicalFont,
                            fontSize = 10.sp,
                            color = TextSecondaryColor,
                            maxLines = 1
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(AccentBg, RoundedCornerShape(2.dp))
                            .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "TERMUX SSH :${serverConfig.port}",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = AccentColor
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Cabecera de infraestructura conectada a Termux
            ServerHeader(
                hostString = "${serverConfig.user}@${serverConfig.host}:${serverConfig.port}",
                latency = "24ms",
                activeCount = activeBotsCount,
                totalCount = botsList.size,
                filterText = filterText,
                onFilterChange = { filterText = it },
                onNewBotClick = {
                    showCloneGeneralDialog = true
                },
                onKillAllProcessesClick = { showKillAllDialog = true },
                onScanClick = {
                    showQuickCommands = true
                },
                onSettingsClick = {
                    showServerSettings = true
                }
            )

            // Sección de lista de bots
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("bots_list"),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SESIONES TMUX DETECTADAS (${filteredBots.size})",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = TextSecondaryColor,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "Toca para expandir/colapsar",
                            fontFamily = UiFont,
                            fontSize = 11.sp,
                            color = TextSecondaryColor
                        )
                    }
                }

                items(
                    items = filteredBots,
                    key = { it.id }
                ) { bot ->
                    val isExpanded = expandedStates[bot.id] ?: false

                    BotRowCard(
                        bot = bot,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedStates[bot.id] = !isExpanded
                        },
                        onRestartClick = {
                            showFeedback("tmux kill-session -t ${bot.name} 2>/dev/null; tmux new -s ${bot.name} -d bash ~/comandos/vigilante.sh ${bot.name}")
                        },
                        onStopClick = {
                            showFeedback("tmux kill-session -t ${bot.name} 2>/dev/null")
                        },
                        onLiveLogClick = {
                            selectedBotForLogs = bot
                        },
                        onEditIndexClick = {
                            selectedBotForEditor = Pair(bot, "index.js")
                        },
                        onFilesClick = {
                            selectedBotForFiles = bot
                        },
                        onUpdateClick = {
                            showFeedback("cd ~/${bot.name} && ./actualizar.sh")
                        },
                        onCloneClick = {
                            showCloneDialogForBot = bot
                        },
                        onClearCredentialsClick = {
                            botToClearCredentials = bot
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Diálogo destructivo: Borrar credenciales (auth_info configurable)
        botToClearCredentials?.let { bot ->
            ClearCredentialsDialog(
                botName = bot.name,
                authFolder = "${bot.remotePath}/${serverConfig.credFolder}",
                onDismiss = { botToClearCredentials = null },
                onConfirm = {
                    showFeedback("rm -rf ~/${bot.name}/${serverConfig.credFolder} && tmux kill-session -t ${bot.name}; tmux new -s ${bot.name} -d bash ~/comandos/vigilante.sh ${bot.name}")
                    botToClearCredentials = null
                }
            )
        }

        // Diálogo destructivo: Matar procesos colgados (pkill -9 -f 'node index.js')
        if (showKillAllDialog) {
            KillAllProcessesDialog(
                onDismiss = { showKillAllDialog = false },
                onConfirm = {
                    showFeedback("pkill -9 -f 'node index.js'; echo Procesos node eliminados.")
                    showKillAllDialog = false
                }
            )
        }

        // Diálogo para clonar bot (desde + Nuevo bot o desde el botón Clonar de un bot específico)
        if (showCloneGeneralDialog || showCloneDialogForBot != null) {
            CloneBotDialog(
                availableBots = botsList,
                defaultTemplate = showCloneDialogForBot ?: botsList.firstOrNull(),
                onDismiss = {
                    showCloneGeneralDialog = false
                    showCloneDialogForBot = null
                },
                onConfirmClone = { templateName, newName ->
                    val newBot = BotItem(
                        id = newName,
                        name = newName,
                        status = BotStatus.STOPPED,
                        pid = null,
                        tmuxSession = "$newName (listo para iniciar)",
                        remotePath = "${serverConfig.termuxHome}/$newName",
                        uptime = "Recién clonado",
                        memoryRss = "0 MB",
                        cpuPercent = "0.0%",
                        authFolder = serverConfig.credFolder,
                        watchdogScript = "bash ~/comandos/vigilante.sh $newName",
                        lastLogSnippet = "[sistema] Clonado desde $templateName. Credenciales limpias.",
                        nodeVersion = "v20.11.1"
                    )
                    botsList = botsList + newBot
                    expandedStates[newName] = true
                    showFeedback("Clonado exitoso: $newName creado en Termux. Listo para editar BOT_PATH o iniciar.")
                    showCloneGeneralDialog = false
                    showCloneDialogForBot = null
                }
            )
        }
    }
}
