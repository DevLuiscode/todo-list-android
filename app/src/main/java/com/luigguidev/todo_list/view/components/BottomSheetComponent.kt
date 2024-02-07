package com.luigguidev.todo_list.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.luigguidev.todo_list.model.database.category.CategoryEntity
import com.luigguidev.todo_list.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetComponent(
    showBottomSheet: Boolean,
    onShowBottomSheet: () -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    category: CategoryEntity,
    sharedViewModel: SharedViewModel,
    navController: NavHostController,
) {
    val categoryViewModel = sharedViewModel.categoryViewModel
    var openAlertDialogDelete by remember { mutableStateOf(false) }
    var openAlertDialogEdit by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier,
            onDismissRequest = {
                onDismissRequest()
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
                        openAlertDialogEdit = true
                        categoryViewModel.getById(category)
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
                        openAlertDialogDelete = true
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
                            text = "Eliminar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
    if (openAlertDialogDelete) {
        AlertDialogComponent(
            icon = Icons.Default.Clear,
            dialogTitle = "Deseas eliminar la categoría de ${category.name}",
            messageBody = "Si eliminas la categoría, se eliminarán todas las tareas asociadas a esta categoría",
            onDismissRequest = {
                openAlertDialogDelete = false
            },
            onConfirmation = {
                openAlertDialogDelete = false
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onShowBottomSheet()
                    }
                }

            },
            sharedViewModel = sharedViewModel,
            categoryEntity = category,
            isEdit = false
        )
    }
    if (openAlertDialogEdit) {

        AlertDialogComponent(
            icon = Icons.Default.Clear,
            dialogTitle = "Editar categoría ${category.name}",
            messageBody = "Nombre",
            onDismissRequest = {
                openAlertDialogEdit = false
            },
            onConfirmation = {
                openAlertDialogEdit = false
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onShowBottomSheet()
                    }
                }

            },
            sharedViewModel = sharedViewModel,
            categoryEntity = category,
            isEdit = true
        )
    }


}