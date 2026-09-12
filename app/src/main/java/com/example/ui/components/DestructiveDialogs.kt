package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.CardBg
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DangerBg
import com.example.ui.theme.DividerColor
import com.example.ui.theme.StatusErrorColor
import com.example.ui.theme.TechnicalFont
import com.example.ui.theme.TextPrimaryColor
import com.example.ui.theme.TextSecondaryColor
import com.example.ui.theme.UiFont

@Composable
fun ClearCredentialsDialog(
    botName: String,
    authFolder: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var confirmInput by remember { mutableStateOf("") }
    val isConfirmed = confirmInput.trim().equals("SI", ignoreCase = false)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(2.dp))
                .border(1.dp, StatusErrorColor, RoundedCornerShape(2.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(DangerBg, RoundedCornerShape(2.dp))
                            .border(1.dp, StatusErrorColor, RoundedCornerShape(2.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Dangerous,
                            contentDescription = "Peligro",
                            tint = StatusErrorColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ACCION DESTRUCTIVA",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = StatusErrorColor
                        )
                        Text(
                            text = "Borrar credenciales de $botName",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = TextPrimaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Esta acción eliminará de forma permanente la carpeta de sesión:",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    color = TextSecondaryColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeBg, RoundedCornerShape(2.dp))
                        .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = authFolder,
                        fontFamily = TechnicalFont,
                        fontSize = 12.sp,
                        color = StatusErrorColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "El bot perderá el emparejamiento con WhatsApp y se reiniciará inmediatamente para solicitar un nuevo código de vinculación.",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    color = TextSecondaryColor,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Para confirmar, escribe exactamente \"SI\":",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimaryColor
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = confirmInput,
                    onValueChange = { confirmInput = it },
                    placeholder = {
                        Text(
                            text = "Escribe SI",
                            fontFamily = UiFont,
                            fontSize = 13.sp,
                            color = TextSecondaryColor
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = TechnicalFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isConfirmed) StatusErrorColor else TextPrimaryColor
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CodeBg,
                        unfocusedContainerColor = CodeBg,
                        focusedBorderColor = if (isConfirmed) StatusErrorColor else DividerColor,
                        unfocusedBorderColor = DividerColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirm_delete_credentials_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondaryColor
                        ),
                        modifier = Modifier.testTag("cancel_clear_credentials")
                    ) {
                        Text(
                            text = "Cancelar",
                            fontFamily = UiFont,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = onConfirm,
                        enabled = isConfirmed,
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusErrorColor,
                            disabledContainerColor = DividerColor,
                            contentColor = TextPrimaryColor,
                            disabledContentColor = TextSecondaryColor
                        ),
                        modifier = Modifier.testTag("confirm_clear_credentials_btn")
                    ) {
                        Text(
                            text = "BORRAR Y REINICIAR",
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

@Composable
fun KillAllProcessesDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(2.dp))
                .border(1.dp, StatusErrorColor, RoundedCornerShape(2.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(DangerBg, RoundedCornerShape(2.dp))
                            .border(1.dp, StatusErrorColor, RoundedCornerShape(2.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WarningAmber,
                            contentDescription = "Advertencia Global",
                            tint = StatusErrorColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ADVERTENCIA DEL SERVIDOR",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = StatusErrorColor
                        )
                        Text(
                            text = "Matar procesos colgados",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = TextPrimaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Comando a ejecutar en el servidor:",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    color = TextSecondaryColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeBg, RoundedCornerShape(2.dp))
                        .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "pkill -9 -f 'node index.js'",
                        fontFamily = TechnicalFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = StatusErrorColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¡ATENCIÓN! Esta acción afectará a TODOS los bots y procesos Node del host de forma inmediata. Las conexiones activas serán forzadas a terminar.",
                    fontFamily = UiFont,
                    fontSize = 12.sp,
                    color = TextSecondaryColor,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondaryColor
                        ),
                        modifier = Modifier.testTag("cancel_kill_processes")
                    ) {
                        Text(
                            text = "Cancelar",
                            fontFamily = UiFont,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusErrorColor,
                            contentColor = TextPrimaryColor
                        ),
                        modifier = Modifier.testTag("confirm_kill_processes_btn")
                    ) {
                        Text(
                            text = "MATAR TODOS",
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
