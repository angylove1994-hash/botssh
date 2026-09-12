package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BotItem
import com.example.model.BotStatus
import com.example.ui.theme.AccentBg
import com.example.ui.theme.AccentColor
import com.example.ui.theme.BadgeBg
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DangerBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.StatusErrorColor
import com.example.ui.theme.StatusRunningColor
import com.example.ui.theme.StatusStoppedColor
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BotRowCard(
    bot: BotItem,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onRestartClick: () -> Unit,
    onStopClick: () -> Unit,
    onLiveLogClick: () -> Unit,
    onEditIndexClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onCloneClick: () -> Unit,
    onClearCredentialsClick: () -> Unit,
    onFilesClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Tarjeta sin radio redondeado excesivo para estética de infraestructura pura
    // Cuando está expandida, se resalta con un borde sutil y acento ámbar
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBg)
            .border(
                width = 1.dp,
                color = if (isExpanded) AccentColor.copy(alpha = 0.5f) else DividerColor
            )
            .height(IntrinsicSize.Min)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // REGLA OBLIGATORIA: Barra vertical de 3px a la izquierda con el color de estado
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(bot.status.color)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
            ) {
                // FILA PRINCIPAL (Siempre visible, colapsada o expandida)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleExpand() }
                        .padding(vertical = 2.dp)
                        .testTag("bot_row_${bot.id}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        // REGLA OBLIGATORIA: Punto de color de estado (mismo lenguaje)
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(bot.status.color)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Nombre de sesión / bot en JetBrains Mono (TechnicalFont)
                        Text(
                            text = bot.name,
                            fontFamily = TechnicalFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimaryColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Etiqueta de estado técnica con texto en Inter (UiFont)
                        StatusBadge(status = bot.status)

                        // Badges técnicos en la fila colapsada (TechnicalFont para PID numérico)
                        if (bot.pid != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            TechTag(text = "PID ${bot.pid}")
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Uptime en texto secundario monospace (TechnicalFont)
                        Text(
                            text = bot.uptime,
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = TextSecondaryColor
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Icono expandir / colapsar con acento ámbar (#F0A868) activo
                        Icon(
                            imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                            contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                            tint = if (isExpanded) AccentColor else TextSecondaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Subtítulo de ruta técnica en estado colapsado
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleExpand() }
                        .padding(start = 15.dp, top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bot.remotePath,
                        fontFamily = TechnicalFont,
                        fontSize = 11.sp,
                        color = TextSecondaryColor
                    )

                    if (bot.status == BotStatus.RUNNING) {
                        Text(
                            text = "${bot.memoryRss} | ${bot.cpuPercent}",
                            fontFamily = TechnicalFont,
                            fontSize = 10.sp,
                            color = TextSecondaryColor
                        )
                    }
                }

                // PANEL DE PROPIEDADES EXPANDIDO (FASE 1)
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = DividerColor
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Encabezado de sección técnica con Inter (UiFont) y acento ámbar
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "// ",
                                fontFamily = TechnicalFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = AccentColor
                            )
                            Text(
                                text = "PROPIEDADES DE LA INSTANCIA",
                                fontFamily = UiFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = TextSecondaryColor,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Bloque de propiedades técnicas en grid
                        // Etiquetas en Inter (UiFont), valores en JetBrains Mono (TechnicalFont)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CodeBg)
                                .border(1.dp, DividerColor)
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                PropertyLine(label = "Sesión tmux", value = bot.tmuxSession)
                                PropertyLine(label = "Directorio", value = bot.remotePath)
                                PropertyLine(
                                    label = "Proceso Node",
                                    value = if (bot.pid != null) "node index.js (PID ${bot.pid})" else "Inactivo (sin proceso node)"
                                )
                                PropertyLine(label = "Credenciales", value = "${bot.remotePath}/${bot.authFolder}")
                                PropertyLine(label = "Script watchdog", value = bot.watchdogScript)
                                PropertyLine(label = "Runtime Node", value = bot.nodeVersion)
                                PropertyLine(label = "Recursos", value = "RAM RSS: ${bot.memoryRss} | CPU: ${bot.cpuPercent}")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Último snippet de log tipo terminal: prompt en ámbar (#F0A868), log en JetBrains Mono
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CodeBg)
                                .border(1.dp, DividerColor)
                                .padding(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "❯ ",
                                    fontFamily = TechnicalFont,
                                    fontSize = 11.sp,
                                    color = AccentColor
                                )
                                Text(
                                    text = bot.lastLogSnippet,
                                    fontFamily = TechnicalFont,
                                    fontSize = 11.sp,
                                    color = if (bot.status == BotStatus.ERROR) StatusErrorColor else TextPrimaryColor,
                                    maxLines = 2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // BOTONERA DE ACCIONES MOCK: Título en Inter (UiFont) con acento ámbar
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "// ",
                                fontFamily = TechnicalFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = AccentColor
                            )
                            Text(
                                text = "ACCIONES DE CONTROL",
                                fontFamily = UiFont,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondaryColor,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 1. Ver en vivo (ACCIÓN PRIMARIA DE TELEMETRÍA) -> ACENTO ÁMBAR #F0A868
                            ActionChip(
                                label = "Ver en vivo",
                                icon = Icons.Outlined.Terminal,
                                tint = AccentColor,
                                isPrimaryAccent = true,
                                onClick = onLiveLogClick,
                                tag = "btn_live_log_${bot.id}"
                            )

                            // 2. Reiniciar (Acción de control) -> ACENTO ÁMBAR #F0A868
                            ActionChip(
                                label = "Reiniciar",
                                icon = Icons.Outlined.PlayArrow,
                                tint = AccentColor,
                                isPrimaryAccent = true,
                                onClick = onRestartClick,
                                tag = "btn_restart_${bot.id}"
                            )

                            // 3. Detener (mata tmux)
                            ActionChip(
                                label = "Detener",
                                icon = Icons.Outlined.Stop,
                                tint = StatusStoppedColor,
                                onClick = onStopClick,
                                tag = "btn_stop_${bot.id}"
                            )

                            // 4. Editar index.js (SFTP editor)
                            ActionChip(
                                label = "Editar index.js",
                                icon = Icons.Outlined.Edit,
                                tint = TextPrimaryColor,
                                onClick = onEditIndexClick,
                                tag = "btn_edit_index_${bot.id}"
                            )

                            // 5. Archivos / SFTP (Bajar/Subir/rutas.txt)
                            ActionChip(
                                label = "Archivos / SFTP",
                                icon = Icons.Outlined.Folder,
                                tint = TextPrimaryColor,
                                onClick = onFilesClick,
                                tag = "btn_files_${bot.id}"
                            )

                            // 6. Actualizar (./actualizar.sh)
                            ActionChip(
                                label = "Actualizar",
                                icon = Icons.Outlined.Refresh,
                                tint = TextPrimaryColor,
                                onClick = onUpdateClick,
                                tag = "btn_update_${bot.id}"
                            )

                            // 7. Clonar bot (duplicar instancia)
                            ActionChip(
                                label = "Clonar",
                                icon = Icons.Outlined.AccountTree,
                                tint = TextPrimaryColor,
                                onClick = onCloneClick,
                                tag = "btn_clone_${bot.id}"
                            )

                            // 8. Borrar credenciales (Destructivo - pide confirmación "SI")
                            ActionChip(
                                label = "Borrar auth",
                                icon = Icons.Outlined.DeleteOutline,
                                tint = StatusErrorColor,
                                isDestructive = true,
                                onClick = onClearCredentialsClick,
                                tag = "btn_clear_auth_${bot.id}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: BotStatus) {
    Box(
        modifier = Modifier
            .background(
                color = when (status) {
                    BotStatus.RUNNING -> com.example.ui.theme.SuccessBg
                    BotStatus.ERROR -> DangerBg
                    BotStatus.STOPPED -> BadgeBg
                },
                shape = RoundedCornerShape(2.dp)
            )
            .border(
                width = 1.dp,
                color = status.color,
                shape = RoundedCornerShape(2.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        // Texto de estado en Inter (UiFont), no monoespaciado
        Text(
            text = status.label,
            fontFamily = UiFont,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            color = status.color
        )
    }
}

@Composable
fun TechTag(text: String) {
    Box(
        modifier = Modifier
            .background(BadgeBg, RoundedCornerShape(2.dp))
            .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
            .padding(horizontal = 5.dp, vertical = 1.dp)
    ) {
        // Valores técnicos o IDs numéricos en JetBrains Mono
        Text(
            text = text,
            fontFamily = TechnicalFont,
            fontSize = 9.sp,
            color = TextSecondaryColor
        )
    }
}

@Composable
fun PropertyLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Etiqueta en Inter (UiFont)
        Text(
            text = "$label:",
            fontFamily = UiFont,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = TextSecondaryColor,
            modifier = Modifier.width(115.dp)
        )
        // Valor técnico en JetBrains Mono (TechnicalFont)
        Text(
            text = value,
            fontFamily = TechnicalFont,
            fontSize = 11.sp,
            color = TextPrimaryColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActionChip(
    label: String,
    icon: ImageVector,
    tint: Color,
    isPrimaryAccent: Boolean = false,
    isDestructive: Boolean = false,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .background(
                color = when {
                    isPrimaryAccent -> AccentBg
                    isDestructive -> DangerBg
                    else -> BadgeBg
                },
                shape = RoundedCornerShape(2.dp)
            )
            .border(
                width = 1.dp,
                color = when {
                    isPrimaryAccent -> AccentColor
                    isDestructive -> StatusErrorColor
                    else -> DividerColor
                },
                shape = RoundedCornerShape(2.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        // Etiqueta del botón en Inter (UiFont) para legibilidad de interfaz humana
        Text(
            text = label,
            fontFamily = UiFont,
            fontWeight = if (isPrimaryAccent) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 11.sp,
            color = tint
        )
    }
}
