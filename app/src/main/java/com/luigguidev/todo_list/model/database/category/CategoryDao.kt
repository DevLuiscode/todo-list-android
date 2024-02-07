package com.luigguidev.todo_list.model.database.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.luigguidev.todo_list.model.database.relations.CategoryWithTask
import com.luigguidev.todo_list.model.database.task.TaskEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_table")
    fun getAllCategories():Flow<List<CategoryWithTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM category_table WHERE idCategory = :id")
    suspend fun getById(id: Int?): CategoryEntity

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)

}