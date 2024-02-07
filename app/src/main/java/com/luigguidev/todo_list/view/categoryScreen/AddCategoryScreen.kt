package com.luigguidev.todo_list.view.categoryScreen

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
import com.luigguidev.todo_list.viewmodel.SharedViewModel


@Composable
fun AddCategoryScreen(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
    sharedViewModel: SharedViewModel
) {
    val categoryViewModel = sharedViewModel.categoryViewModel

    val name by categoryViewModel.nameCategory.collectAsState()
    val btn by categoryViewModel.btn.collectAsState()
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "edit Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
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
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                enabled = btn,
                onClick = {
                    onConfirmation()
                    categoryViewModel.categoryAdd(name)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    categoryViewModel.formClear()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}