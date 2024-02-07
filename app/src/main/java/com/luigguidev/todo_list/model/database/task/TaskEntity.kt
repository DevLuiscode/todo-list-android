package com.luigguidev.todo_list.model.database.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.luigguidev.todo_list.model.database.category.CategoryEntity

@Entity(
    tableName = "task_table",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["idCategory"],
            childColumns = ["idOwnerCategory"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val idTask: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "limitDate")
    val limitDate: String,
    @ColumnInfo(name = "state")
    val state: Boolean = false,
    @ColumnInfo(name = "idOwnerCategory")
    val idOwnerCategory: Int
)