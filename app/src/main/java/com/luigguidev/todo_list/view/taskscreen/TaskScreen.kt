package com.luigguidev.todo_list.view.taskscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.luigguidev.todo_list.config.routes.AppRoutes
import com.luigguidev.todo_list.model.database.task.TaskEntity
import com.luigguidev.todo_list.view.categoryScreen.AddCategoryScreen
import com.luigguidev.todo_list.view.components.ActionTopBar
import com.luigguidev.todo_list.view.components.BottomSheetComponent
import com.luigguidev.todo_list.viewmodel.SharedViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopBarSection(
                sharedViewModel = sharedViewModel
            )
        },
        floatingActionButton = {
            FloatingButton(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    ) {
        BodySection(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 12.dp),
            sharedViewModel = sharedViewModel,
            navController = navController
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FloatingButton(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val taskViewModel = sharedViewModel.taskViewModel
    FloatingActionButton(
        onClick = {
            navController.navigate(
                "${AppRoutes.AddScreen.route}/{id}".replace(
                    oldValue = "{id}",
                    newValue = "noid"
                )
            ) {
                popUpTo(AppRoutes.TaskScreen.route)
            }
            taskViewModel.formClear()
        },
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarSection(
    sharedViewModel: SharedViewModel
) {
    val taskViewModel = sharedViewModel.taskViewModel
    val dateNow by taskViewModel.dateNow.collectAsState()

    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            MaterialTheme.colorScheme.primary
        ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hoy",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    text = dateNow,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )


            }
        },
        actions = {
            ActionTopBar(
                cardColor = MaterialTheme.colorScheme.secondary,
                background = MaterialTheme.colorScheme.primary
            )
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BodySection(
    modifier: Modifier,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    var openAlertDialog by remember { mutableStateOf(false) }

    val taskScreenViewModel = sharedViewModel.taskViewModel
    val listTask by taskScreenViewModel.taskList.collectAsState()


    Column(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "Categorias",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                IconButton(
                    onClick = {
                        openAlertDialog = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CardItemCategory(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    items(listTask) { task ->
                        CardItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            task,
                            sharedViewModel = sharedViewModel,
                            navController = navController
                        )
                    }
                }
            )
        }
    }

    if (openAlertDialog) {
        AddCategoryScreen(
            onDismissRequest = { openAlertDialog = false },
            onConfirmation = {
                openAlertDialog = false
            },
            dialogTitle = "Agregar Categoria",
            icon = Icons.Default.Create,
            sharedViewModel
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CardItemCategory(
    modifier: Modifier,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    val categoryViewModel = sharedViewModel.categoryViewModel
    val taskViewModel = sharedViewModel.taskViewModel
    val listCategory by categoryViewModel.categoryList.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val objectCategory by categoryViewModel.categoryId.collectAsState()

    LazyRow(
        modifier = modifier,
        content = {

            items(listCategory.size) { index ->
                val category = listCategory[index]
                Card(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .padding(end = 12.dp)
                        .combinedClickable(
                            onClick = {
                                taskViewModel.getAllTaskByCategory(categoryId = category.category.idCategory)
                            },
                            onLongClick = {
                                showBottomSheet = true
                                categoryViewModel.setCategoryIdObject(category.category)
                            }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(12.dp),
                        text = category.category.name,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }


        }

    )
    if (showBottomSheet) {
        BottomSheetComponent(
            showBottomSheet = showBottomSheet,
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            scope = scope,
            onShowBottomSheet = {
                showBottomSheet = false
            },
            category = objectCategory,
            sharedViewModel = sharedViewModel,
            navController = navController,

            )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CardItem(
    modifier: Modifier,
    task: TaskEntity,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    val taskViewModel = sharedViewModel.taskViewModel
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showTextDescription by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    showTextDescription = !showTextDescription
                },
                onLongClick = {
                    showBottomSheet = true
                }
            ),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 12.dp)
                .fillMaxSize()
        ) {
            Text(
                text = task.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textDecoration = if (task.state) TextDecoration.LineThrough else {
                    null
                }
            )
            if (showTextDescription) {
                Text(
                    text = task.description,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.state) TextDecoration.LineThrough else {
                        null
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = task.limitDate)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier,
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.primary,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Card(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        navController.navigate(
                            "${AppRoutes.AddScreen.route}/{id}"
                                .replace(
                                    oldValue = "{id}",
                                    newValue = "${task.idTask}"
                                )
                        ) {
                            popUpTo(AppRoutes.TaskScreen.route)
                        }
                        taskViewModel.getById(task.idTask)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        text = "Editar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Card(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }

                        taskViewModel.updateTaskState(task)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hecho",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}
