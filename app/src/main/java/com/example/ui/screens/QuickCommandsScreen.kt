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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class QuickCmd(
    val label: String,
    val command: String,
    val description: String
)

@Composable
fun QuickCommandsScreen(
    onBackClick: () -> Unit,
    onExecuteCommand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickCmdList = remember {
        listOf(
            QuickCmd("Ver sesiones tmux", "tmux ls", "Lista todas las sesiones activas en Termux"),
            QuickCmd("Ver procesos Node", "ps aux | grep node", "Muestra PIDs y consumo de memoria"),
            QuickCmd("Memoria libre", "free -h", "Ver estado de memoria RAM y Swap"),
            QuickCmd("Uptime del host", "uptime", "Tiempo de actividad y carga promedio"),
            QuickCmd("Espacio en disco", "df -h /data", "Espacio disponible en partición Termux"),
            QuickCmd("Listar directorio raíz", "ls -la ~", "Ver carpetas de bots y scripts en ~/"),
            QuickCmd("Ver script vigilante", "cat ~/comandos/vigilante.sh", "Inspeccionar script watchdog")
        )
    }

    var customCmdInput by remember { mutableStateOf("") }
    val commandLogs = remember {
        mutableStateListOf(
            "u0_a112@100.91.23.52:8022 $ tmux ls",
            "whatsapp-bot: 1 windows (created Fri Sep 12 14:10:00 2026)",
            "whatsapp-bot-2: 1 windows (created Wed Sep 10 11:22:15 2026)",
            "whatsapp-bot-4: 1 windows (created Thu Sep 11 08:30:40 2026)",
            "----------------------------------------------------------------",
            "u0_a112@100.91.23.52:8022 $ ps aux | grep node",
            "u0_a112  14829  0.8  3.8 142400 45200 pts/1    S+   14:10   0:15 node index.js",
            "u0_a112  14835  1.2  4.2 189100 52300 pts/2    S+   Sep10   1:42 node index.js",
            "u0_a112  16201  0.4  3.1 118000 38900 pts/3    S+   Sep11   0:08 node index.js"
        )
    }

    fun execute(cmd: String) {
        if (cmd.isBlank()) return
        commandLogs.add("----------------------------------------------------------------")
        commandLogs.add("u0_a112@100.91.23.52:8022 $ $cmd")
        onExecuteCommand(cmd)

        // Mock output para comandos conocidos
        when {
            cmd.contains("free") -> {
                commandLogs.add("               total        used        free      shared  buff/cache   available")
                commandLogs.add("Mem:           5.7Gi       2.1Gi       1.8Gi       120Mi       1.8Gi       3.4Gi")
                commandLogs.add("Swap:          2.0Gi       256Mi       1.7Gi")
            }
            cmd.contains("uptime") -> {
                commandLogs.add(" 18:26:40 up 14 days,  3:12,  2 users,  load average: 0.42, 0.58, 0.65")
            }
            cmd.contains("df -h") -> {
                commandLogs.add("Filesystem      Size  Used Avail Use% Mounted on")
                commandLogs.add("/dev/block/dm-0 110G   45G   65G  41% /data")
            }
            cmd.contains("ls -la ~") -> {
                commandLogs.add("drwx------  7 u0_a112 u0_a112 4096 Sep 12 18:24 .")
                commandLogs.add("drwx------  4 u0_a112 u0_a112 4096 Sep 12 14:00 comandos")
                commandLogs.add("drwx------ 12 u0_a112 u0_a112 4096 Sep 12 18:20 whatsapp-bot")
                commandLogs.add("drwx------ 12 u0_a112 u0_a112 4096 Sep 10 11:20 whatsapp-bot-2")
                commandLogs.add("drwx------ 12 u0_a112 u0_a112 4096 Sep 08 09:15 whatsapp-bot-3")
                commandLogs.add("drwx------ 12 u0_a112 u0_a112 4096 Sep 11 08:30 whatsapp-bot-4")
            }
            cmd.contains("vigilante.sh") -> {
                commandLogs.add("#!/bin/bash")
                commandLogs.add("BOT=\"\$1\"")
                commandLogs.add("while true; do")
                commandLogs.add("  cd ~/\$BOT && node index.js")
                commandLogs.add("  echo \"Crash detectado en \$BOT. Reiniciando en 3s...\"")
                commandLogs.add("  sleep 3")
                commandLogs.add("done")
            }
            else -> {
                commandLogs.add("[ssh:output] Comando ejecutado con código de salida 0 (OK)")
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PageBg,
        topBar = {
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
                            modifier = Modifier.size(36.dp).testTag("btn_back_from_commands")
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
                            Text(
                                text = "// CONSOLA Y COMANDOS RÁPIDOS",
                                fontFamily = UiFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = AccentColor
                            )
                            Text(
                                text = "Ejecución SSH en Termux",
                                fontFamily = UiFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = TextPrimaryColor
                            )
                        }
                    }

                    IconButton(
                        onClick = { commandLogs.clear() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteSweep,
                            contentDescription = "Limpiar",
                            tint = TextSecondaryColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Input para comandos arbitrarios
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
                    Text(
                        text = "$",
                        fontFamily = TechnicalFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = AccentColor,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    OutlinedTextField(
                        value = customCmdInput,
                        onValueChange = { customCmdInput = it },
                        placeholder = {
                            Text(
                                text = "Comando bash remoto...",
                                fontFamily = TechnicalFont,
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
                        keyboardActions = KeyboardActions(onSend = {
                            val cmd = customCmdInput.trim()
                            customCmdInput = ""
                            execute(cmd)
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CodeBg,
                            unfocusedContainerColor = CodeBg,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor,
                            cursorColor = AccentColor
                        ),
                        modifier = Modifier.weight(1f).height(44.dp)
                    )

                    Row(
                        modifier = Modifier
                            .height(44.dp)
                            .background(AccentBg, RoundedCornerShape(2.dp))
                            .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                            .clickable {
                                val cmd = customCmdInput.trim()
                                customCmdInput = ""
                                execute(cmd)
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Ejecutar",
                            tint = AccentColor,
                            modifier = Modifier.size(14.dp)
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
                .background(PageBg)
        ) {
            // Carrusel de chips de comandos rápidos
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(160.dp).background(CardBg).border(1.dp, DividerColor),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(quickCmdList) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CodeBg, RoundedCornerShape(2.dp))
                            .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                            .clickable { execute(item.command) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.PlayArrow,
                                contentDescription = null,
                                tint = AccentColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.label,
                                fontFamily = UiFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = TextPrimaryColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.command,
                                fontFamily = TechnicalFont,
                                fontSize = 10.sp,
                                color = AccentColor
                            )
                        }

                        Text(
                            text = item.description,
                            fontFamily = UiFont,
                            fontSize = 10.sp,
                            color = TextSecondaryColor
                        )
                    }
                }
            }

            // Consola de salida de comandos
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 8.dp),
                contentPadding = PaddingValues(bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(commandLogs) { line ->
                    Text(
                        text = line,
                        fontFamily = TechnicalFont,
                        fontSize = 11.sp,
                        color = if (line.startsWith("u0_a112@")) AccentColor else TextPrimaryColor,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
