package com.luigguidev.todo_list.model.database.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_table",
    indices = [Index(
        value = ["name"],
        unique = true
    ),]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val idCategory: Int = 0,
    @ColumnInfo(name = "name")
    var name: String
)
