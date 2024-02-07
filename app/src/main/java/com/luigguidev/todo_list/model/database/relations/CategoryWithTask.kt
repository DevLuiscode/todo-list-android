package com.luigguidev.todo_list.model.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.luigguidev.todo_list.model.database.category.CategoryEntity
import com.luigguidev.todo_list.model.database.task.TaskEntity

data class CategoryWithTask(
    @Embedded val category : CategoryEntity,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idOwnerCategory"
    )
    val tasks : List<TaskEntity>? = emptyList()
)
