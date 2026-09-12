package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentBg
import com.example.ui.theme.AccentColor
import com.example.ui.theme.BadgeBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DangerBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.StatusErrorColor
import com.example.ui.theme.StatusRunningColor
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont

@Composable
fun ServerHeader(
    hostString: String = "root@192.168.1.150:22",
    latency: String = "18ms",
    activeCount: Int = 3,
    totalCount: Int = 5,
    filterText: String,
    onFilterChange: (String) -> Unit,
    onNewBotClick: () -> Unit,
    onKillAllProcessesClick: () -> Unit,
    onScanClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .border(width = 1.dp, color = DividerColor)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        // Fila 1: Host, Estado de conexión, Badge de acento ámbar y acciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                // Indicador de conexión verde (Lenguaje de estado)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(StatusRunningColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Host en JetBrains Mono (TechnicalFont)
                Text(
                    text = hostString,
                    fontFamily = TechnicalFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimaryColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Badge de protocolo / conexión con ACENTO ÁMBAR #F0A868
                Box(
                    modifier = Modifier
                        .background(AccentBg, RoundedCornerShape(2.dp))
                        .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "SSH DIRECT",
                        fontFamily = UiFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = AccentColor
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                // Latencia en JetBrains Mono
                Box(
                    modifier = Modifier
                        .background(BadgeBg, RoundedCornerShape(2.dp))
                        .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = latency,
                        fontFamily = TechnicalFont,
                        fontSize = 10.sp,
                        color = StatusRunningColor
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Botón escanear / tmux ls con toque ámbar
                IconButton(
                    onClick = onScanClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("scan_tmux_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "Escanear tmux",
                        tint = AccentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                // Botón Ajustes
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Ajustes del servidor",
                        tint = TextSecondaryColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Fila 2: Telemetría rápida del host (tipo monitor de infraestructura)
        // Etiquetas en Inter (UiFont), valores en JetBrains Mono (TechnicalFont)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CodeBg, RoundedCornerShape(2.dp))
                .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Bots activos: ",
                    fontFamily = UiFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = TextSecondaryColor
                )
                Text(
                    text = "$activeCount/$totalCount",
                    fontFamily = TechnicalFont,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activeCount > 0) StatusRunningColor else TextSecondaryColor
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Carga: ",
                    fontFamily = UiFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = TextSecondaryColor
                )
                Text(
                    text = "0.42 0.38 0.30",
                    fontFamily = TechnicalFont,
                    fontSize = 11.sp,
                    color = TextPrimaryColor
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Memoria: ",
                    fontFamily = UiFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = TextSecondaryColor
                )
                Text(
                    text = "1.8G / 4.0G",
                    fontFamily = TechnicalFont,
                    fontSize = 11.sp,
                    color = TextPrimaryColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Fila 3: Botón de foco primario ámbar (+ NUEVO BOT), Filtro y botón crítico pkill
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Buscador / Filtro de patrón: placeholder en Inter, texto ingresado en JetBrains Mono
            OutlinedTextField(
                value = filterText,
                onValueChange = onFilterChange,
                placeholder = {
                    Text(
                        text = "Filtrar por patrón o ruta...",
                        fontFamily = UiFont,
                        fontSize = 12.sp,
                        color = TextSecondaryColor
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar bot",
                        tint = if (filterText.isNotEmpty()) AccentColor else TextSecondaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = TechnicalFont,
                    fontSize = 12.sp,
                    color = TextPrimaryColor
                ),
                singleLine = true,
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
                    .testTag("filter_input")
            )

            // BOTÓN DE ACCIÓN PRIMARIA CON ACENTO ÁMBAR #F0A868 (+ NUEVO BOT)
            Row(
                modifier = Modifier
                    .height(44.dp)
                    .background(AccentBg, RoundedCornerShape(2.dp))
                    .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                    .clickable { onNewBotClick() }
                    .padding(horizontal = 10.dp)
                    .testTag("new_bot_primary_btn"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Crear nuevo bot",
                    tint = AccentColor,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Nuevo bot",
                    fontFamily = UiFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = AccentColor
                )
            }

            // Botón destructivo global: pkill -9 -f 'node index.js'
            Row(
                modifier = Modifier
                    .height(44.dp)
                    .background(DangerBg, RoundedCornerShape(2.dp))
                    .border(1.dp, StatusErrorColor, RoundedCornerShape(2.dp))
                    .clickable { onKillAllProcessesClick() }
                    .padding(horizontal = 10.dp)
                    .testTag("kill_all_hung_processes_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Dangerous,
                    contentDescription = "Matar procesos colgados",
                    tint = StatusErrorColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "pkill node",
                    fontFamily = TechnicalFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = StatusErrorColor
                )
            }
        }
    }
}
