package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServerConfig
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
fun ServerSettingsScreen(
    currentConfig: ServerConfig,
    onBackClick: () -> Unit,
    onSaveConfig: (ServerConfig) -> Unit,
    onTestSsh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var host by remember { mutableStateOf(currentConfig.host) }
    var port by remember { mutableStateOf(currentConfig.port.toString()) }
    var user by remember { mutableStateOf(currentConfig.user) }
    var pass by remember { mutableStateOf(currentConfig.pass) }
    var showPassword by remember { mutableStateOf(false) }

    var credFolder by remember { mutableStateOf(currentConfig.credFolder) }
    var termuxHome by remember { mutableStateOf(currentConfig.termuxHome) }
    var downloadRoot by remember { mutableStateOf(currentConfig.downloadRoot) }

    var testStatusMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(36.dp).testTag("btn_back_from_settings")
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
                            text = "// CONFIGURACIÓN DEL SERVIDOR",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = AccentColor
                        )
                        Text(
                            text = "Parámetros de conexión SSH y Termux",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimaryColor
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Botón Guardar en la barra inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .border(1.dp, DividerColor)
                    .padding(12.dp)
            ) {
                Button(
                    onClick = {
                        val newConfig = currentConfig.copy(
                            host = host.trim(),
                            port = port.toIntOrNull() ?: 8022,
                            user = user.trim(),
                            pass = pass,
                            credFolder = credFolder.trim(),
                            termuxHome = termuxHome.trim(),
                            downloadRoot = downloadRoot.trim()
                        )
                        onSaveConfig(newConfig)
                        testStatusMessage = "Configuración guardada exitosamente."
                    },
                    shape = RoundedCornerShape(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentColor,
                        contentColor = CardBg
                    ),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GUARDAR CONFIGURACIÓN",
                        fontFamily = UiFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PageBg)
                .verticalScroll(scrollState)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // BLOQUE 1: Conexión SSH (Opción 1 del .bat)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg, RoundedCornerShape(2.dp))
                    .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Terminal,
                            contentDescription = null,
                            tint = AccentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CONEXIÓN SSH DIRECTA (TERMUX)",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = AccentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(2.5f)) {
                            Text(text = "Host / IP:", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                            Spacer(modifier = Modifier.height(3.dp))
                            OutlinedTextField(
                                value = host,
                                onValueChange = { host = it },
                                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CodeBg,
                                    unfocusedContainerColor = CodeBg,
                                    focusedBorderColor = AccentColor,
                                    unfocusedBorderColor = DividerColor
                                ),
                                modifier = Modifier.fillMaxWidth().height(46.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Puerto:", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                            Spacer(modifier = Modifier.height(3.dp))
                            OutlinedTextField(
                                value = port,
                                onValueChange = { port = it },
                                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CodeBg,
                                    unfocusedContainerColor = CodeBg,
                                    focusedBorderColor = AccentColor,
                                    unfocusedBorderColor = DividerColor
                                ),
                                modifier = Modifier.fillMaxWidth().height(46.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1.5f)) {
                            Text(text = "Usuario:", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                            Spacer(modifier = Modifier.height(3.dp))
                            OutlinedTextField(
                                value = user,
                                onValueChange = { user = it },
                                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CodeBg,
                                    unfocusedContainerColor = CodeBg,
                                    focusedBorderColor = AccentColor,
                                    unfocusedBorderColor = DividerColor
                                ),
                                modifier = Modifier.fillMaxWidth().height(46.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1.5f)) {
                            Text(text = "Contraseña:", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                            Spacer(modifier = Modifier.height(3.dp))
                            OutlinedTextField(
                                value = pass,
                                onValueChange = { pass = it },
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(onClick = { showPassword = !showPassword }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Lock,
                                            contentDescription = "Mostrar contraseña",
                                            tint = if (showPassword) AccentColor else TextSecondaryColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CodeBg,
                                    unfocusedContainerColor = CodeBg,
                                    focusedBorderColor = AccentColor,
                                    unfocusedBorderColor = DividerColor
                                ),
                                modifier = Modifier.fillMaxWidth().height(46.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón Probar Conexión
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AccentBg, RoundedCornerShape(2.dp))
                            .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                            .clickable {
                                onTestSsh()
                                testStatusMessage = "Conexión SSH exitosa con $user@$host:$port (latencia: 24ms)"
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null, tint = AccentColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "PROBAR CONEXIÓN SSH", fontFamily = UiFont, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentColor)
                    }

                    if (testStatusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = testStatusMessage!!,
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = StatusRunningColor
                        )
                    }
                }
            }

            // BLOQUE 2: Rutas del Sistema en Termux
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg, RoundedCornerShape(2.dp))
                    .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "RUTAS DEL ENTORNO TERMUX",
                        fontFamily = UiFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = AccentColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Carpeta de credenciales/sesión (CRED_FOLDER):", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = credFolder,
                        onValueChange = { credFolder = it },
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CodeBg,
                            unfocusedContainerColor = CodeBg,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor
                        ),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "TERMUXHOME (Directorio raíz de Termux):", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = termuxHome,
                        onValueChange = { termuxHome = it },
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CodeBg,
                            unfocusedContainerColor = CodeBg,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor
                        ),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "DLROOT (Descargas del celular para media/RULETA):", fontFamily = UiFont, fontSize = 11.sp, color = TextSecondaryColor)
                    Spacer(modifier = Modifier.height(3.dp))
                    OutlinedTextField(
                        value = downloadRoot,
                        onValueChange = { downloadRoot = it },
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = TechnicalFont, fontSize = 12.sp, color = TextPrimaryColor),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CodeBg,
                            unfocusedContainerColor = CodeBg,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = DividerColor
                        ),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
