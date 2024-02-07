package com.luigguidev.todo_list.view.taskscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.luigguidev.todo_list.config.routes.AppRoutes
import com.luigguidev.todo_list.model.database.task.TaskEntity
import com.luigguidev.todo_list.view.components.ActionTopBar
import com.luigguidev.todo_list.viewmodel.SharedViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddScreen(
    sharedViewModel: SharedViewModel,
    navController: NavHostController,
    id: String?
) {
    val taskViewModel = sharedViewModel.taskViewModel
    val taskObject by taskViewModel.taskObject.collectAsState()

    Scaffold(
        topBar = {
            TopBarSection(
                navController = navController,
                id = id,
                sharedViewModel
            )
        },
        containerColor = MaterialTheme.colorScheme.secondary
    ) {

        BodySection(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            sharedViewModel = sharedViewModel,
            id = id,
            taskObject = taskObject

        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarSection(
    navController: NavHostController,
    id: String?,
    sharedViewModel: SharedViewModel
) {
    val taskViewModel = sharedViewModel.taskViewModel
    val noId by taskViewModel.noId.collectAsState()
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        title =
        {
            if (id == noId) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Agregar Tarea",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Editar Tarea",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(AppRoutes.TaskScreen.route) {
                        popUpTo(AppRoutes.TaskScreen.route) {
                            inclusive = true
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        actions = {
            ActionTopBar(
                background = MaterialTheme.colorScheme.secondary,
                cardColor = MaterialTheme.colorScheme.primary
            )
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BodySection(
    modifier: Modifier,
    sharedViewModel: SharedViewModel,
    id: String?,
    taskObject: TaskEntity
) {
    val taskViewModel = sharedViewModel.taskViewModel
    val categoryViewModel = sharedViewModel.categoryViewModel

    val name by taskViewModel.name.collectAsState()
    val description by taskViewModel.description.collectAsState()
    val limitDate by taskViewModel.limitDate.collectAsState()
    val btn by taskViewModel.addBtn.collectAsState()


    val expanded by taskViewModel.expanded.collectAsState()
    val categoryList by categoryViewModel.categoryList.collectAsState()
    val selectedOptionText by taskViewModel.selectOption.collectAsState()

    val noId by taskViewModel.noId.collectAsState()

    var isOpenDatePicker by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
    ) {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                taskViewModel.setExpanded(it)
            },
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),

                value = selectedOptionText,
                readOnly = true,
                onValueChange = {},
                label = {
                    Text(text = "Categoria")
                },
                trailingIcon = {

                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = CircleShape,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    taskViewModel.setExpanded(false)
                },
                modifier = Modifier.exposedDropdownSize()
            ) {
                categoryList.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.category.name) },
                        onClick = {
                            val select = selectionOption.category.name
                            val idCategory = selectionOption.category.idCategory
                            taskViewModel.setIdCategory(idCategory)
                            taskViewModel.setSelectCategory(select)
                            taskViewModel.setExpanded(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )


                }

            }

        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            value = name,
            onValueChange = {
                taskViewModel.formValidate(it, description, limitDate)
            },
            maxLines = 1,
            label = {
                Text(text = "nombre")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Create, contentDescription = null)
            },
            shape = CircleShape,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = description,
            onValueChange = {
                taskViewModel.formValidate(name, it, limitDate)
            },
            maxLines = 1,
            label = {
                Text(text = "descripción")
            },

            trailingIcon = {
                Icon(imageVector = Icons.Default.Create, contentDescription = null)
            },
            shape = CircleShape,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            value = limitDate,
            onValueChange = {
                taskViewModel.formValidate(name, description, it)
            },
            maxLines = 1,
            label = {
                Text(text = "fecha limite")
            },
            placeholder = {
                Text(text = "dd/MM/yyyy")
            },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { isOpenDatePicker = true }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                }
            },
            shape = CircleShape,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {

                if (id != noId) {
                    taskViewModel.updateTask(id = id!!.toInt())
                } else {
                    taskViewModel.taskAdd()
                }

            },
            enabled = btn,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            if (id != noId) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = "Editar",
                )
            } else {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = "Agregar",
                )
            }
        }
    }
    if (isOpenDatePicker) {
        MyDatePickerDialogScreen(
            pickerState = isOpenDatePicker,
            onChangedDate = {
                taskViewModel.formValidate(name = name, description, it)
            },
            onChangedPickerState = {
                isOpenDatePicker = it
            },
            sharedViewModel = sharedViewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyDatePickerDialogScreen(
    pickerState: Boolean,
    onChangedPickerState: (Boolean) -> Unit,
    onChangedDate: (String) -> Unit,
    sharedViewModel: SharedViewModel
) {
    val categoryViewModel = sharedViewModel.categoryViewModel
    val state = rememberDatePickerState()

    if (pickerState) {
        DatePickerDialog(
            onDismissRequest = {
                onChangedPickerState(false)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val fechaSelect = state.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                        }
                        val formattedDate = fechaSelect?.let {
                            DateTimeFormatter.ofPattern("dd-MM-yyyy").format(it)
                        }
                        onChangedDate(formattedDate.toString())
                        onChangedPickerState(false)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        onChangedPickerState(false)
                        categoryViewModel.formClear()
                    }
                ) {
                    Text("CANCEL")
                }
            }
        ) {
            DatePicker(
                state = state
            )
        }
    }
}
