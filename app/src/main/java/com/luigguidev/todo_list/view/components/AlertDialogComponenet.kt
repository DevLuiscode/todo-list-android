package com.luigguidev.todo_list.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.luigguidev.todo_list.model.database.category.CategoryEntity
import com.luigguidev.todo_list.viewmodel.SharedViewModel


@Composable
fun AlertDialogComponent(
    icon: ImageVector,
    dialogTitle: String,
    messageBody: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    sharedViewModel: SharedViewModel,
    categoryEntity: CategoryEntity,
    isEdit: Boolean
) {
    val categoryViewModel = sharedViewModel.categoryViewModel
    val name by categoryViewModel.nameCategory.collectAsState()


    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = "edit Icon",
                tint = Color.Red
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            if (isEdit) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    value = name,
                    onValueChange = {
                        categoryViewModel.categoryForm(it)
                    },
                    maxLines = 1,
                    label = {
                        Text(text = "Nombre")
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
            } else {
                Text(text = messageBody)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                    if (!isEdit) {
                        categoryViewModel.deleteCategory(categoryEntity)
                    }else{
                        categoryViewModel.updateCategory(categoryEntity.idCategory)
                    }

                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}