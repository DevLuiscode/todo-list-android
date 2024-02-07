package com.luigguidev.todo_list.model.database.task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTaskState(task: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM task_table WHERE idTask = :id")
    suspend fun getById(id: Int?): TaskEntity

    @Query("SELECT * FROM task_table WHERE idOwnerCategory = :idOwnerCategory")
    fun getAllTaskByCategory(idOwnerCategory:Int) : Flow<List<TaskEntity>>

}