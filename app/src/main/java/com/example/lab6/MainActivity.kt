package com.example.lab6

import com.example.lab6.data.DataStorage
import com.example.lab6.logic.Assembly
import com.example.lab6.logic.Detail
import com.example.lab6.logic.Mechanism
import com.example.lab6.logic.Warehouse
import com.example.lab6.ui.theme.Lab6Theme
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.lab6.data.AppDatabase
import com.example.lab6.data.entities.AssemblyEntity
import com.example.lab6.data.entities.DetailEntity
import com.example.lab6.data.entities.MechanismEntity
import kotlinx.coroutines.launch


import com.example.lab6.data.repository.WarehouseRepository
import com.example.lab6.data.entities.WarehouseEntity
import com.example.lab6.data.repository.AssemblyRepository
import com.example.lab6.data.repository.DetailRepository
import com.example.lab6.data.repository.MechanismRepository
import com.example.lab6.data.repository.OfflineAssemblyRepository
import com.example.lab6.data.repository.OfflineDetailRepository
import com.example.lab6.data.repository.OfflineMechanismRepository
import com.example.lab6.data.repository.OfflineWarehouseRepository
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val warehouseRepo = OfflineWarehouseRepository(db.warehouseDao())
        val detailRepo = OfflineDetailRepository(db.detailDao())
        val assemblyRepo = OfflineAssemblyRepository(db.assemblyDao())
        val mechanismRepo = OfflineMechanismRepository(db.mechanismDao())

        setContent {
            Lab6Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App(warehouseRepo = warehouseRepo,
                        detailRepo = detailRepo,
                        assemblyRepo = assemblyRepo,
                        mechanismRepo = mechanismRepo)
                }
            }
        }

    }
}

@Composable
fun App(warehouseRepo: WarehouseRepository,
        detailRepo: DetailRepository,
        assemblyRepo: AssemblyRepository,
        mechanismRepo: MechanismRepository) {

    var screen by remember { mutableStateOf("main") }
    var currentWarehouse by remember { mutableStateOf<WarehouseEntity?>(null) }

    when (screen) {
        "main" -> MainScreen(
            loginClick = { screen = "login" },
            registerClick = { screen = "register" }
        )

        "login" -> LoginScreen(
            warehouseRepo = warehouseRepo,
            onBack = { screen = "main" },
            onLoginSuccess = {
                currentWarehouse = it
                screen = "menu"
            }
        )

        "register" -> RegisterScreen(
            warehouseRepo = warehouseRepo,
            onBack = { screen = "main" },
            onRegisterSuccess = {
                currentWarehouse = it
                screen = "menu"
            }
        )

        "menu" -> MenuScreen(
            warehouseRepo = warehouseRepo,
            currentWarehouse = currentWarehouse,
            detailRepo = detailRepo,
            assemblyRepo = assemblyRepo,
            mechanismRepo = mechanismRepo,
            onLogout = {
                currentWarehouse = null
                screen = "main"
            }
        )
    }
}

@Composable
fun MainScreen(loginClick: () -> Unit, registerClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = loginClick, modifier = Modifier.padding(4.dp)) {
            Text("Вхід", fontSize = 20.sp)
        }
        Button(onClick = registerClick, modifier = Modifier.padding(4.dp)) {
            Text("Реєстрація", fontSize = 20.sp)
        }
    }
}

@Composable
fun RegisterScreen(
    warehouseRepo: WarehouseRepository,
    onBack: () -> Unit,
    onRegisterSuccess: (WarehouseEntity) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Реєстрація", fontSize = 22.sp)

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логін*") },
            isError = showErrors && login.isBlank(),
        )
        if (showErrors && login.isBlank()) {
            Text(
                text = "Логін обов'язковий",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль*") },
            visualTransformation = PasswordVisualTransformation(),
            isError = showErrors && password.isBlank(),
        )
        if (showErrors && password.isBlank()) {
            Text(
                text = "Пароль обов'язковий",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showErrors = true
                if (login.isBlank() || password.isBlank()) return@Button

                // Запускаем корутину для работы с Room
                scope.launch {
                    val existing = warehouseRepo.getWarehouseByLogin(login, password)
                    if (existing != null) {
                        message = "Такий логін уже існує!"
                    } else {
                        val newWh = WarehouseEntity(name = login, password = password)
                        warehouseRepo.insertWarehouse(newWh)
                        onRegisterSuccess(newWh)
                    }
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Підтвердити")
        }

        Button(onClick = onBack, modifier = Modifier.padding(8.dp)) {
            Text("Назад")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}


@Composable
fun LoginScreen(
    warehouseRepo: WarehouseRepository,
    onBack: () -> Unit,
    onLoginSuccess: (WarehouseEntity) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope() // для работы с suspend функциями

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Вхід", fontSize = 22.sp)

        // Логін
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логін*") },
            isError = showErrors && login.isBlank()
        )
        if (showErrors && login.isBlank()) {
            Text(
                text = "Логін обов'язковий",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Пароль
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль*") },
            visualTransformation = PasswordVisualTransformation(),
            isError = showErrors && password.isBlank()
        )
        if (showErrors && password.isBlank()) {
            Text(
                text = "Пароль обов'язковий",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showErrors = true
                if (login.isBlank() || password.isBlank()) return@Button

                scope.launch {
                    val warehouse = warehouseRepo.getWarehouseByLogin(login, password)
                    if (warehouse != null) {
                        onLoginSuccess(warehouse)
                    } else {
                        message = "Неправильний логін або пароль!"
                    }
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Увійти")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Назад")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    warehouseRepo: WarehouseRepository,
    detailRepo: DetailRepository,
    assemblyRepo: AssemblyRepository,
    mechanismRepo: MechanismRepository,
    currentWarehouse: WarehouseEntity?,
    onLogout: () -> Unit
) {
    if (currentWarehouse == null) {
        Text("Помилка: склад не знайдено")
        return
    }

    var selectedTab by remember { mutableStateOf("Склад") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentWarehouse.name) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Вийти"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                NavigationBarItem(
                    selected = selectedTab == "Склад",
                    onClick = { selectedTab = "Склад" },
                    label = { Text("Склад") },
                    icon = { Icon(Icons.Filled.Home, "Склад") }
                )

                NavigationBarItem(
                    selected = selectedTab == "Додати",
                    onClick = { selectedTab = "Додати" },
                    label = { Text("Додати") },
                    icon = { Icon(Icons.Filled.Add, "Додати") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                "Склад" -> WarehouseTab(
                    detailRepo = detailRepo,
                    assemblyRepo = assemblyRepo,
                    mechanismRepo = mechanismRepo,
                    warehouse = currentWarehouse
                )

                "Додати" -> AddTab(
                    warehouseId = currentWarehouse.id,
                    detailRepo = detailRepo,
                    assemblyRepo = assemblyRepo,
                    mechanismRepo = mechanismRepo
                )
            }
        }
    }
}


@Composable
fun WarehouseTab(
    warehouse: WarehouseEntity,
    detailRepo: DetailRepository,
    assemblyRepo: AssemblyRepository,
    mechanismRepo: MechanismRepository
) {
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            "Склад",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton("Деталі", selectedTab == 0) { selectedTab = 0 }
            TabButton("Вузли", selectedTab == 1) { selectedTab = 1 }
            TabButton("Механізми", selectedTab == 2) { selectedTab = 2 }
        }

        Spacer(Modifier.height(16.dp))

        when (selectedTab) {
            0 -> DetailsList(
                warehouseId = warehouse.id,
                detailRepo = detailRepo,
                onDeleteDetail = { detail ->
                    scope.launch { detailRepo.deleteDetail(detail) }
                },
                onUpdateDetail = {detail ->
                    scope.launch { detailRepo.updateDetail(detail)}
                }
            )
            1 -> AssembliesList(
                warehouseId = warehouse.id,
                assemblyRepo = assemblyRepo,
                detailRepo = detailRepo,
                onDeleteAssembly = { assembly ->
                    scope.launch { assemblyRepo.deleteAssembly(assembly) }
                },
                onUpdateAssembly = {assembly ->
                    scope.launch { assemblyRepo.updateAssembly(assembly)}
                }
            )
            2 -> MechanismsList(
                warehouseId = warehouse.id,
                mechanismRepo = mechanismRepo,
                assemblyRepo = assemblyRepo,
                onDeleteMechanism = { mechanism ->
                    scope.launch { mechanismRepo.deleteMechanism(mechanism) }
                },
                onUpdateMechanism = {mechanism ->
                    scope.launch { mechanismRepo.updateMechanism(mechanism)}
                }
            )
        }
    }
}



@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {

    val bgColor = MaterialTheme.colorScheme.primaryContainer
    val textColor = Color.Black

    if (selected) {
        // ==== Выбранная - Outlined ====
        OutlinedButton(
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = textColor
            )
        ) {
            Text(text)
        }

    } else {
        // ==== Не выбранная ====
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = bgColor,
                contentColor = textColor
            )
        ) {
            Text(text)
        }
    }
}

@Composable
fun DetailsList(
    warehouseId: Int,
    detailRepo: DetailRepository,
    onDeleteDetail: (DetailEntity) -> Unit,
    onUpdateDetail: (DetailEntity) -> Unit
) {
    // Тягнемо деталі з БД по складу
    var details by remember { mutableStateOf<List<DetailEntity>>(emptyList()) }

    LaunchedEffect(warehouseId) {
        details = detailRepo.getDetailsByWarehouse(warehouseId)
    }

    LazyColumn {
        if (details.isEmpty()) {
            item {
                Text(
                    "Жодної деталі не додано",
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 15.sp
                )
            }
        } else {
            items(details) { detail ->

                var expanded by remember { mutableStateOf(false) }
                var isEditing by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                var manufacturer by remember { mutableStateOf(detail.manufacturer) }
                var year by remember { mutableStateOf(detail.year.toString()) }
                var price by remember { mutableStateOf(detail.price.toString()) }
                var material by remember { mutableStateOf(detail.material) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.clickable { expanded = !expanded }) {
                            Text(detail.name, fontSize = 20.sp)
                            Text(
                                if (expanded) " −" else " +",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }

                        IconButton(onClick = {
                            expanded = true
                            isEditing = !isEditing
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Редагувати"
                            )
                        }
                    }

                    if (expanded) {

                        if (!isEditing) {
                            Column(modifier = Modifier.padding(start = 26.dp)) {
                                Text("- Виробник: ${detail.manufacturer}")
                                Text("- Рік виготовлення: ${detail.year}")
                                Text("- Ціна: ${detail.price}")
                                Text("- Матеріал: ${detail.material.ifBlank { "не вказано" }}")
                            }

                        } else {

                            Column(modifier = Modifier.padding(start = 26.dp)) {

                                OutlinedTextField(
                                    value = manufacturer,
                                    onValueChange = { manufacturer = it },
                                    label = { Text("Виробник") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = year,
                                    onValueChange = { year = it },
                                    label = { Text("Рік") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Ціна") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = material,
                                    onValueChange = { material = it },
                                    label = { Text("Матеріал") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Button(onClick = {
                                        // Зберегти зміни в БД
                                        val updated = detail.copy(
                                            manufacturer = manufacturer,
                                            year = year.toIntOrNull() ?: detail.year,
                                            price = price.toDoubleOrNull() ?: detail.price,
                                            material = material
                                        )

                                        onUpdateDetail(updated)

                                        isEditing = false
                                    }) {
                                        Text("Зберегти")
                                    }

                                    OutlinedButton(onClick = {
                                        manufacturer = detail.manufacturer
                                        year = detail.year.toString()
                                        price = detail.price.toString()
                                        material = detail.material
                                        isEditing = false
                                    }) {
                                        Text("Скасувати")
                                    }
                                }

                                Spacer(Modifier.height(10.dp))

                                Button(
                                    onClick = { showDeleteDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Видалити", color = Color.White)
                                }

                                if (showDeleteDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDeleteDialog = false },
                                        title = { Text("Видалення") },
                                        text = { Text("Ви дійсно хочете видалити '${detail.name}'?") },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                onDeleteDetail(detail)
                                                showDeleteDialog = false
                                            }) {
                                                Text("Видалити", color = Color.Red)
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDeleteDialog = false }) {
                                                Text("Скасувати")
                                            }
                                        }
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


@Composable
fun AssembliesList(warehouseId: Int,
                   assemblyRepo: AssemblyRepository,
                   detailRepo: DetailRepository,
                   onDeleteAssembly: (AssemblyEntity) -> Unit,
                   onUpdateAssembly: (AssemblyEntity) -> Unit
) {

    var assemblies by remember { mutableStateOf<List<AssemblyEntity>>(emptyList()) }

    LaunchedEffect(warehouseId) {
        assemblies = assemblyRepo.getAssembliesByWarehouse(warehouseId)
    }

    LazyColumn {
        if (assemblies.isEmpty()) {
            item {
                Text("Жодного вузла не додано", modifier = Modifier.padding(start = 10.dp), fontSize = 15.sp)
            }
        } else {
            items(assemblies) { assembly ->

                var expanded by remember { mutableStateOf(false) }
                var isEditing by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                // локальные поля редактирования
                var manufacturer by remember { mutableStateOf(assembly.manufacturer) }
                var year by remember { mutableStateOf(assembly.year.toString()) }
                var price by remember { mutableStateOf(assembly.price.toString()) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    // ===== Заголовок =====
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.clickable { expanded = !expanded }
                        ) {
                            Text(assembly.name, fontSize = 20.sp)
                            Text(if (expanded) " −" else " +",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }

                        IconButton(onClick = {
                            expanded = true
                            isEditing = !isEditing
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit assembly")
                        }
                    }

                    // ===== Раскрытая часть =====
                    if (expanded) {

                        if (!isEditing) {
                            // Просмотр обычных данных
                            val assemblyDetails by produceState(initialValue = emptyList<DetailEntity>(), assembly) {
                                value = detailRepo.getDetailsByAssembly(assembly.id)
                            }

                            Column(modifier = Modifier.padding(start = 26.dp)) {
                                Text("- Виробник: ${assembly.manufacturer}")
                                Text("- Рік виготовлення: ${assembly.year}")
                                Text("- Ціна: ${assembly.price}")
                                Text(
                                    "- Деталі: ${
                                        if (assemblyDetails.isEmpty()) "не вказано"
                                        else assemblyDetails.joinToString(", ") { it.name }
                                    }"
                                )
                            }


                        } else {
                            // ===== Режим редактирования =====
                            Column(modifier = Modifier.padding(start = 26.dp)) {

                                OutlinedTextField(
                                    value = manufacturer,
                                    onValueChange = { manufacturer = it },
                                    label = { Text("Виробник") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = year,
                                    onValueChange = { year = it },
                                    label = { Text("Рік") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Ціна") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Button(onClick = {
                                        // Зберегти зміни в БД
                                        val updated = assembly.copy(
                                            manufacturer = manufacturer,
                                            year = year.toIntOrNull() ?: assembly.year,
                                            price = price.toDoubleOrNull() ?: assembly.price,
                                        )

                                        onUpdateAssembly(updated)

                                        isEditing = false
                                    }) {
                                        Text("Зберегти")
                                    }

                                    OutlinedButton(onClick = {
                                        manufacturer = assembly.manufacturer
                                        year = assembly.year.toString()
                                        price = assembly.price.toString()
                                        isEditing = false
                                    }) {
                                        Text("Скасувати")
                                    }
                                }

                                Spacer(Modifier.height(10.dp))

                                Button(
                                    onClick = { showDeleteDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Видалити", color = Color.White)
                                }

                                // ===== Диалог подтверждения удаления =====
                                if (showDeleteDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDeleteDialog = false },
                                        title = { Text("Видалення") },
                                        text = { Text("Ви дійсно хочете видалити '${assembly.name}'?") },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                onDeleteAssembly(assembly)
                                                showDeleteDialog = false
                                            },
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = Color.Red // колір тексту
                                                )
                                            ) {
                                                Text("Видалити")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDeleteDialog = false }) {
                                                Text("Скасувати")
                                            }
                                        }
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


@Composable
fun MechanismsList(
    warehouseId: Int,
    mechanismRepo: MechanismRepository,
    assemblyRepo: AssemblyRepository,
    onDeleteMechanism: (MechanismEntity) -> Unit,
    onUpdateMechanism: (MechanismEntity) -> Unit
) {

    var mechanisms by remember { mutableStateOf<List<MechanismEntity>>(emptyList()) }

    LaunchedEffect(warehouseId) {
        mechanisms = mechanismRepo.getMechanismsByWarehouse(warehouseId)
    }

    LazyColumn {
        if (mechanisms.isEmpty()) {
            item {
                Text("Жодного механізму не додано", modifier = Modifier.padding(start = 10.dp), fontSize = 15.sp)
            }
        } else {
            items(mechanisms) { mechanism ->

                var expanded by remember { mutableStateOf(false) }
                var isEditing by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                var manufacturer by remember { mutableStateOf(mechanism.manufacturer) }
                var year by remember { mutableStateOf(mechanism.year.toString()) }
                var price by remember { mutableStateOf(mechanism.price.toString()) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 4.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.clickable { expanded = !expanded }
                        ) {
                            Text(mechanism.name, fontSize = 20.sp)
                            Text(
                                if (expanded) " −" else " +",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }

                        IconButton(onClick = {
                            expanded = true
                            isEditing = !isEditing
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit mechanism")
                        }
                    }

                    if (expanded) {
                        if (!isEditing) {
                            // Просмотр обычных данных
                            val mechanismAssemblies by produceState(initialValue = emptyList<AssemblyEntity>(), mechanism) {
                                value = assemblyRepo.getAssembliesByMechanism(mechanism.id)
                            }

                            Column(modifier = Modifier.padding(start = 26.dp)) {
                                Text("- Виробник: ${mechanism.manufacturer}")
                                Text("- Рік виготовлення: ${mechanism.year}")
                                Text("- Ціна: ${mechanism.price}")
                                Text(
                                    "- Вузли: ${
                                        if (mechanismAssemblies.isEmpty()) "не вказано"
                                        else mechanismAssemblies.joinToString(", ") { it.name }
                                    }"
                                )
                            }



                        } else {
                            // ===== Режим редагування =====
                            Column(modifier = Modifier.padding(start = 26.dp)) {

                                OutlinedTextField(
                                    value = manufacturer,
                                    onValueChange = { manufacturer = it },
                                    label = { Text("Виробник") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = year,
                                    onValueChange = { year = it },
                                    label = { Text("Рік") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Ціна") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Button(onClick = {
                                        // Зберегти зміни в БД
                                        val updated = mechanism.copy(
                                            manufacturer = manufacturer,
                                            year = year.toIntOrNull() ?: mechanism.year,
                                            price = price.toDoubleOrNull() ?: mechanism.price,
                                        )

                                        onUpdateMechanism(updated)

                                        isEditing = false
                                    }) {
                                        Text("Зберегти")
                                    }

                                    OutlinedButton(onClick = {
                                        manufacturer = mechanism.manufacturer
                                        year = mechanism.year.toString()
                                        price = mechanism.price.toString()
                                        isEditing = false
                                    }) {
                                        Text("Скасувати")
                                    }
                                }

                                Spacer(Modifier.height(10.dp))

                                Button(
                                    onClick = { showDeleteDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Видалити", color = Color.White)
                                }

                                // ===== Диалог подтверждения удаления =====
                                if (showDeleteDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDeleteDialog = false },
                                        title = { Text("Видалення") },
                                        text = { Text("Ви дійсно хочете видалити '${mechanism.name}'?") },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                onDeleteMechanism(mechanism)
                                                showDeleteDialog = false
                                            },
                                                colors = ButtonDefaults.textButtonColors(
                                                    contentColor = Color.Red // колір тексту
                                                )
                                            ) {
                                                Text("Видалити")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showDeleteDialog = false }) {
                                                Text("Скасувати")
                                            }
                                        }
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





@Composable
fun AddTab(
    warehouseId: Int,
    mechanismRepo: MechanismRepository,
    assemblyRepo: AssemblyRepository,
    detailRepo: DetailRepository
) {
    val scope = rememberCoroutineScope()

    var category by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var manufacturer by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    var searchQueryDetails by remember { mutableStateOf("") }
    var searchResultsDetails by remember { mutableStateOf(listOf<DetailEntity>()) }
    var selectedDetailIds by remember { mutableStateOf(setOf<Int>()) }

    var searchQueryAssemblies by remember { mutableStateOf("") }
    var searchResultsAssemblies by remember { mutableStateOf(listOf<AssemblyEntity>()) }
    var selectedAssemblies by remember { mutableStateOf(setOf<AssemblyEntity>()) }

    // Загружаем все детали и узлы по складу для поиска
    var allDetails by remember { mutableStateOf(listOf<DetailEntity>()) }
    var allAssemblies by remember { mutableStateOf(listOf<AssemblyEntity>()) }

    LaunchedEffect(warehouseId) {
        allDetails = detailRepo.getDetailsByWarehouse(warehouseId)
        allAssemblies = assemblyRepo.getAssembliesByWarehouse(warehouseId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item { Text("Додати", fontSize = 30.sp, modifier = Modifier.padding(8.dp)) }

        if (category == null) {
            item {
                Text("Оберіть тип:", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Деталь", "Вузол", "Механізм").forEach { type ->
                        Button(onClick = {
                            category = type
                            name = ""; manufacturer = ""; year = ""; price = ""; material = ""
                            searchQueryDetails = ""; searchResultsDetails = emptyList(); selectedDetailIds = emptySet()
                            searchQueryAssemblies = ""; searchResultsAssemblies = emptyList(); selectedAssemblies = emptySet()
                        }) { Text(type) }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            item { Text("Тип: $category", fontSize = 16.sp); Spacer(modifier = Modifier.height(8.dp)) }

            item {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Назва*") },
                    isError = showErrors && name.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showErrors && name.isBlank())
                    Text("Це поле обов'язкове", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            item {
                OutlinedTextField(
                    value = manufacturer, onValueChange = { manufacturer = it },
                    label = { Text("Виробник*") },
                    isError = showErrors && manufacturer.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showErrors && manufacturer.isBlank())
                    Text("Це поле обов'язкове", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            item {
                OutlinedTextField(
                    value = year, onValueChange = { year = it },
                    label = { Text("Рік*") },
                    isError = showErrors && year.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showErrors && year.isBlank())
                    Text("Це поле обов'язкове", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            item {
                OutlinedTextField(
                    value = price, onValueChange = { price = it },
                    label = { Text("Ціна*") },
                    isError = showErrors && price.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (showErrors && price.isBlank())
                    Text("Це поле обов'язкове", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            if (category == "Деталь") {
                item {
                    OutlinedTextField(
                        value = material,
                        onValueChange = { material = it },
                        label = { Text("Матеріал") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // ---------- Вузол ----------
            if (category == "Вузол") {
                item {
                    OutlinedTextField(
                        value = searchQueryDetails,
                        onValueChange = { searchQueryDetails = it },
                        label = { Text("Додайте деталі до вузла") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            searchResultsDetails = allDetails.filter { it.name.contains(searchQueryDetails, ignoreCase = true) }

                            // Логируем ID и имена отфильтрованных деталей
                            Log.d("DEBUG", "Search results:")
                            searchResultsDetails.forEach { detail ->
                                Log.d("DEBUG", "Detail ID=${detail.id}, name=${detail.name}")
                            }
                        }) { Text("Пошук деталей") }

                        Spacer(modifier = Modifier.weight(1f))
                        Text("Вибрано: ${selectedDetailIds.size}", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
                if (searchResultsDetails.isNotEmpty()) {
                    items(searchResultsDetails) { detail ->
                        val checked = selectedDetailIds.contains(detail.id)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {

                            Checkbox(
                                checked = checked,
                                onCheckedChange = { now ->
                                    selectedDetailIds = if (now) selectedDetailIds + detail.id else selectedDetailIds - detail.id
                                    Log.d("DEBUG", "SelectedDetailIds now: $selectedDetailIds")
                                }
                            )
                            Text(detail.name, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                } else item { Text("(Пошук нічого не знайшов)", modifier = Modifier.padding(8.dp)) }
            }

            // ---------- Механізм ----------
            if (category == "Механізм") {
                item {
                    OutlinedTextField(
                        value = searchQueryAssemblies,
                        onValueChange = { searchQueryAssemblies = it },
                        label = { Text("Додайте вузли до механізму") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            searchResultsAssemblies = allAssemblies.filter { it.name.contains(searchQueryAssemblies, ignoreCase = true) }
                        }) { Text("Пошук вузлів") }
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Вибрано: ${selectedAssemblies.size}", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
                if (searchResultsAssemblies.isNotEmpty()) {
                    items(searchResultsAssemblies) { assembly ->
                        val checked = selectedAssemblies.contains(assembly)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { now ->
                                    selectedAssemblies = if (now) selectedAssemblies + assembly else selectedAssemblies - assembly
                                }
                            )
                            Text(assembly.name, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                } else item { Text("(Пошук нічого не знайшов)", modifier = Modifier.padding(8.dp)) }
            }

            // ---------- Кнопки ----------
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        showErrors = true
                        if (name.isBlank() || manufacturer.isBlank() || year.isBlank() || price.isBlank()) return@Button

                        val n = name.toString()
                        val man = manufacturer.toString()
                        val mat = material.toString()

                        val y = year.toIntOrNull() ?: 2025
                        val p = price.toDoubleOrNull() ?: 0.0

                        when (category) {
                            "Деталь" -> scope.launch {
                                val detail = DetailEntity(
                                    assemblyId = null,
                                    warehouseId = warehouseId,
                                    name = n,
                                    manufacturer = man,
                                    year = y,
                                    price = p,
                                    material = mat
                                )
                                detailRepo.insertDetail(detail)
                                allDetails = detailRepo.getDetailsByWarehouse(warehouseId)
                            }
                            "Вузол" -> scope.launch {
                                val assembly = AssemblyEntity(
                                    mechanismId = null,
                                    warehouseId = warehouseId,
                                    name = n,
                                    manufacturer = man,
                                    year = y,
                                    price = p
                                )

                                val newId = assemblyRepo.insertAssembly(assembly)
                                val assemblyId = newId.toInt()

                                Log.d("DEBUG", "Selected details: $selectedDetailIds")

                                selectedDetailIds.forEach { detailId ->
                                    Log.d("DEBUG", "Updating detail $detailId with assemblyId $assemblyId")
                                    detailRepo.updateAssemblyIdForDetail(detailId, assemblyId)
                                }


                                allAssemblies = assemblyRepo.getAssembliesByWarehouse(warehouseId)
                                allDetails = detailRepo.getDetailsByWarehouse(warehouseId)

                                // сброс полей
                                category = null; name = ""; manufacturer = ""; year = ""; price = ""; material = ""
                                selectedDetailIds = emptySet(); selectedAssemblies = emptySet()
                                searchQueryDetails = ""; searchResultsDetails = emptyList()
                                searchQueryAssemblies = ""; searchResultsAssemblies = emptyList()
                                showErrors = false
                            }
                            "Механізм" -> scope.launch {
                                val mechanism = MechanismEntity(0,
                                    warehouseId,
                                    n,
                                    man,
                                    y,
                                    p)

                                val newId = mechanismRepo.insertMechanism(mechanism)
                                val mechanismId = newId.toInt()

                                selectedAssemblies.forEach { assembly ->
                                    val updated = assembly.copy(
                                        mechanismId = mechanismId
                                    )

                                    assemblyRepo.updateAssembly(updated)
                                }
                                allAssemblies = assemblyRepo.getAssembliesByWarehouse(warehouseId)

                                category = null; name = ""; manufacturer = ""; year = ""; price = ""; material = ""
                                selectedDetailIds = emptySet(); selectedAssemblies = emptySet()
                                searchQueryDetails = ""; searchResultsDetails = emptyList()
                                searchQueryAssemblies = ""; searchResultsAssemblies = emptyList()
                                showErrors = false
                            }
                        }


                    }) { Text("Додати") }

                    Button(onClick = {
                        showErrors = false; category = null
                        name = ""; manufacturer = ""; year = ""; price = ""; material = ""
                        selectedDetailIds = emptySet(); selectedAssemblies = emptySet()
                        searchQueryDetails = ""; searchResultsDetails = emptyList()
                        searchQueryAssemblies = ""; searchResultsAssemblies = emptyList()
                    }) { Text("Назад") }
                }
            }
        }
    }
}

