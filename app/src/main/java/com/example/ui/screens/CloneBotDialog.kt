package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BotItem
import com.example.ui.theme.AccentBg
import com.example.ui.theme.AccentColor
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.StatusErrorColor
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont

@Composable
fun CloneBotDialog(
    availableBots: List<BotItem>,
    defaultTemplate: BotItem?,
    onDismiss: () -> Unit,
    onConfirmClone: (templateName: String, newBotName: String) -> Unit
) {
    var selectedTemplate by remember {
        mutableStateOf(defaultTemplate ?: availableBots.firstOrNull() ?: BotItem(
            id = "whatsapp-bot",
            name = "whatsapp-bot",
            status = com.example.model.BotStatus.RUNNING,
            pid = null,
            tmuxSession = "whatsapp-bot",
            remotePath = "/data/data/com.termux/files/home/whatsapp-bot",
            uptime = "",
            memoryRss = "",
            cpuPercent = "",
            watchdogScript = "",
            lastLogSnippet = ""
        ))
    }

    var dropdownExpanded by remember { mutableStateOf(false) }

    val suggestedNextName = remember(availableBots) {
        val nextNumber = availableBots.size + 1
        "whatsapp-bot-$nextNumber"
    }

    var newBotNameInput by remember { mutableStateOf(suggestedNextName) }
    var confirmInput by remember { mutableStateOf("") }
    val isConfirmed = confirmInput.trim().equals("SI", ignoreCase = false)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(2.dp))
                .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                .padding(18.dp)
        ) {
            Column {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(AccentBg, RoundedCornerShape(2.dp))
                            .border(1.dp, AccentColor, RoundedCornerShape(2.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountTree,
                            contentDescription = "Clonar",
                            tint = AccentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "// PROVISIÓN DE BOT (OPCIÓN 10)",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = AccentColor
                        )
                        Text(
                            text = "Clonar bot existente",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = TextPrimaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selector de plantilla
                Text(
                    text = "1. Elige bot plantilla (copia código y node_modules):",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondaryColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CodeBg, RoundedCornerShape(2.dp))
                            .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedTemplate.name,
                            fontFamily = TechnicalFont,
                            fontSize = 13.sp,
                            color = TextPrimaryColor
                        )
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "Desplegar",
                            tint = AccentColor
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.background(CardBg).border(1.dp, DividerColor)
                    ) {
                        availableBots.forEach { bot ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = bot.name,
                                        fontFamily = TechnicalFont,
                                        fontSize = 13.sp,
                                        color = TextPrimaryColor
                                    )
                                },
                                onClick = {
                                    selectedTemplate = bot
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nombre de carpeta destino
                Text(
                    text = "2. Nombre de la nueva carpeta en Termux:",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondaryColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = newBotNameInput,
                    onValueChange = { newBotNameInput = it },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = TechnicalFont,
                        fontSize = 13.sp,
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
                    modifier = Modifier.fillMaxWidth().testTag("new_bot_name_field")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Explicación técnica de la limpieza remota
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeBg, RoundedCornerShape(2.dp))
                        .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Acción remota en Termux:",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            color = AccentColor
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "cp -r ~/${selectedTemplate.name} ~/$newBotNameInput\nrm -rf ~/$newBotNameInput/auth_info ~/$newBotNameInput/data ~/$newBotNameInput/botpath.json",
                            fontFamily = TechnicalFont,
                            fontSize = 10.sp,
                            color = TextSecondaryColor,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Confirmación explícita
                Text(
                    text = "Escribe \"SI\" para confirmar el clonado:",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimaryColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = confirmInput,
                    onValueChange = { confirmInput = it },
                    placeholder = {
                        Text(
                            text = "Escribe SI",
                            fontFamily = UiFont,
                            fontSize = 12.sp,
                            color = TextSecondaryColor
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = TechnicalFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isConfirmed) AccentColor else TextPrimaryColor
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CodeBg,
                        unfocusedContainerColor = CodeBg,
                        focusedBorderColor = if (isConfirmed) AccentColor else DividerColor,
                        unfocusedBorderColor = DividerColor
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("clone_confirm_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondaryColor
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            fontFamily = UiFont,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (isConfirmed && newBotNameInput.isNotBlank()) {
                                onConfirmClone(selectedTemplate.name, newBotNameInput.trim())
                            }
                        },
                        enabled = isConfirmed && newBotNameInput.isNotBlank(),
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentColor,
                            disabledContainerColor = DividerColor,
                            contentColor = CardBg,
                            disabledContentColor = TextSecondaryColor
                        ),
                        modifier = Modifier.testTag("submit_clone_bot_btn")
                    ) {
                        Text(
                            text = "CLONAR Y AGREGAR",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
