package com.luigguidev.todo_list.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.luigguidev.todo_list.model.database.AppDatabase
import com.luigguidev.todo_list.viewmodel.categoryViewModel.CategoryViewModel
import com.luigguidev.todo_list.viewmodel.taskViewmodel.TaskViewModel

class SharedViewModel(
    private val appDatabase: AppDatabase
):ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    val taskViewModel = TaskViewModel(appDatabase.taskDao(),appDatabase.categoryDao())
    val categoryViewModel = CategoryViewModel(appDatabase.categoryDao())
}