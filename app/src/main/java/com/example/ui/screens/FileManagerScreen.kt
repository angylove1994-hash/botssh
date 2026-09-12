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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BotItem
import com.example.model.MockData
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
fun FileManagerScreen(
    currentBot: BotItem,
    onBackClick: () -> Unit,
    onOpenFileInEditor: (filePath: String) -> Unit,
    onDownloadFile: (remotePath: String) -> Unit,
    onUploadFile: (destinationPath: String, filename: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Pestañas: 0 = Rutas de Bot (Descargar/Editar), 1 = Descargas Celular (Media / RULETA)
    var selectedTab by remember { mutableIntStateOf(0) }

    // Rutas guardadas (memoria rutas.txt)
    val savedRoutes = remember {
        mutableStateListOf<String>().apply {
            addAll(MockData.savedFilePaths)
        }
    }

    var customRouteInput by remember { mutableStateOf("") }
    var saveCustomRouteCheck by remember { mutableStateOf(true) }

    // Carpetas detectadas dentro de /storage/emulated/0/Download
    val downloadFolders = remember {
        mutableStateListOf(
            "RULETA",
            "RULETA/audios",
            "RULETA/videos",
            "MEMES_STICKERS",
            "BACKUPS_WHATSAPP"
        )
    }
    var newFolderNameInput by remember { mutableStateOf("") }

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
                        modifier = Modifier.size(36.dp).testTag("btn_back_from_files")
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
                                text = "// SFTP & GESTOR DE ARCHIVOS: ",
                                fontFamily = UiFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = AccentColor
                            )
                            Text(
                                text = currentBot.name,
                                fontFamily = TechnicalFont,
                                fontSize = 12.sp,
                                color = TextPrimaryColor
                            )
                        }
                        Text(
                            text = "pscp.exe :8022 (u0_a112@100.91.23.52)",
                            fontFamily = TechnicalFont,
                            fontSize = 11.sp,
                            color = TextSecondaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Selector de pestañas
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = CodeBg,
                    contentColor = AccentColor,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = AccentColor,
                            height = 2.dp
                        )
                    },
                    divider = { Box(modifier = Modifier.height(1.dp).background(DividerColor)) }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Storage,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (selectedTab == 0) AccentColor else TextSecondaryColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Archivos del Bot (~/${currentBot.name})",
                                    fontFamily = UiFont,
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (selectedTab == 0) AccentColor else TextSecondaryColor
                                )
                            }
                        }
                    )

                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Folder,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (selectedTab == 1) AccentColor else TextSecondaryColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Descargas Celular (/Download)",
                                    fontFamily = UiFont,
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (selectedTab == 1) AccentColor else TextSecondaryColor
                                )
                            }
                        }
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
        ) {
            if (selectedTab == 0) {
                // PESTAÑA 0: Archivos del Bot (Opciones 11, 12b y 13)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "RUTAS FRECUENTES (rutas.txt):",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = TextSecondaryColor
                        )
                    }

                    items(savedRoutes) { route ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBg, RoundedCornerShape(2.dp))
                                .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Description,
                                        contentDescription = null,
                                        tint = AccentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = route,
                                            fontFamily = TechnicalFont,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 12.sp,
                                            color = TextPrimaryColor
                                        )
                                        Text(
                                            text = "~/${currentBot.name}/$route",
                                            fontFamily = TechnicalFont,
                                            fontSize = 10.sp,
                                            color = TextSecondaryColor
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Si es index.js o json, botón Editar directo
                                    if (route.endsWith(".js") || route.endsWith(".json")) {
                                        Row(
                                            modifier = Modifier
                                                .background(AccentBg, RoundedCornerShape(2.dp))
                                                .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                                                .clickable { onOpenFileInEditor(route) }
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Edit,
                                                contentDescription = "Editar",
                                                tint = AccentColor,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "EDITAR",
                                                fontFamily = UiFont,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                color = AccentColor
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(6.dp))
                                    }

                                    // Botón Bajar a PC (Opción 11)
                                    Row(
                                        modifier = Modifier
                                            .background(CodeBg, RoundedCornerShape(2.dp))
                                            .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                                            .clickable { onDownloadFile("~/${currentBot.name}/$route") }
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.CloudDownload,
                                            contentDescription = "Bajar",
                                            tint = TextPrimaryColor,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "BAJAR",
                                            fontFamily = UiFont,
                                            fontSize = 10.sp,
                                            color = TextPrimaryColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        // Formulario para escribir otra ruta relativa y guardarla
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBg, RoundedCornerShape(2.dp))
                                .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Escribir otra ruta relativa a ~/${currentBot.name}:",
                                    fontFamily = UiFont,
                                    fontSize = 12.sp,
                                    color = TextPrimaryColor
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = customRouteInput,
                                    onValueChange = { customRouteInput = it },
                                    placeholder = {
                                        Text(
                                            text = "ej: package.json o data/config.json",
                                            fontFamily = TechnicalFont,
                                            fontSize = 11.sp,
                                            color = TextSecondaryColor
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
                                    modifier = Modifier.fillMaxWidth().height(46.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.clickable { saveCustomRouteCheck = !saveCustomRouteCheck },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .background(if (saveCustomRouteCheck) AccentColor else CodeBg)
                                                .border(1.dp, if (saveCustomRouteCheck) AccentColor else DividerColor)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Guardar en rutas.txt",
                                            fontFamily = UiFont,
                                            fontSize = 11.sp,
                                            color = TextSecondaryColor
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            if (customRouteInput.isNotBlank()) {
                                                val routeToAdd = customRouteInput.trim()
                                                if (saveCustomRouteCheck && !savedRoutes.contains(routeToAdd)) {
                                                    savedRoutes.add(routeToAdd)
                                                }
                                                onOpenFileInEditor(routeToAdd)
                                            }
                                        },
                                        enabled = customRouteInput.isNotBlank(),
                                        shape = RoundedCornerShape(2.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = AccentColor,
                                            contentColor = CardBg
                                        )
                                    ) {
                                        Text(
                                            text = "ABRIR / PROCESAR",
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
            } else {
                // PESTAÑA 1: Descargas Celular (Opción 12a - /storage/emulated/0/Download)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "CARPETAS EN /storage/emulated/0/Download (DLROOT):",
                            fontFamily = UiFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = TextSecondaryColor
                        )
                    }

                    items(downloadFolders) { folder ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBg, RoundedCornerShape(2.dp))
                                .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Folder,
                                        contentDescription = null,
                                        tint = AccentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = folder,
                                            fontFamily = TechnicalFont,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 12.sp,
                                            color = TextPrimaryColor
                                        )
                                        Text(
                                            text = "/storage/emulated/0/Download/$folder",
                                            fontFamily = TechnicalFont,
                                            fontSize = 10.sp,
                                            color = TextSecondaryColor
                                        )
                                    }
                                }

                                // Botón Subir archivos a esta carpeta
                                Row(
                                    modifier = Modifier
                                        .background(AccentBg, RoundedCornerShape(2.dp))
                                        .border(1.dp, AccentColor, RoundedCornerShape(2.dp))
                                        .clickable {
                                            onUploadFile("/storage/emulated/0/Download/$folder", "imagen_ruleta.jpg")
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.CloudUpload,
                                        contentDescription = "Subir archivos",
                                        tint = AccentColor,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "SUBIR ARCHIVO",
                                        fontFamily = UiFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = AccentColor
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        // Crear nueva carpeta en Download
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBg, RoundedCornerShape(2.dp))
                                .border(1.dp, DividerColor, RoundedCornerShape(2.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Crear nueva carpeta dentro de /Download:",
                                    fontFamily = UiFont,
                                    fontSize = 12.sp,
                                    color = TextPrimaryColor
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = newFolderNameInput,
                                        onValueChange = { newFolderNameInput = it },
                                        placeholder = {
                                            Text(
                                                text = "Nombre carpeta (ej: RULETA/nuevos)",
                                                fontFamily = TechnicalFont,
                                                fontSize = 11.sp,
                                                color = TextSecondaryColor
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
                                        modifier = Modifier.weight(1f).height(46.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            if (newFolderNameInput.isNotBlank()) {
                                                downloadFolders.add(newFolderNameInput.trim())
                                                newFolderNameInput = ""
                                            }
                                        },
                                        enabled = newFolderNameInput.isNotBlank(),
                                        shape = RoundedCornerShape(2.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = AccentColor,
                                            contentColor = CardBg
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.CreateNewFolder,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "CREAR",
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
            }
        }
    }
}
