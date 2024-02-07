package com.luigguidev.todo_list.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luigguidev.todo_list.model.database.category.CategoryDao
import com.luigguidev.todo_list.model.database.category.CategoryEntity
import com.luigguidev.todo_list.model.database.task.TaskDao
import com.luigguidev.todo_list.model.database.task.TaskEntity


@Database(
    entities = [
        CategoryEntity::class,
        TaskEntity::class
    ],
    version = 1
)
abstract class AppDatabase():RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
}
