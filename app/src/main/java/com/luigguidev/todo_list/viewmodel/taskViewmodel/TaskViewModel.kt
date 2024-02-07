package com.luigguidev.todo_list.viewmodel.taskViewmodel

import android.database.sqlite.SQLiteConstraintException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luigguidev.todo_list.model.database.category.CategoryDao
import com.luigguidev.todo_list.model.database.task.TaskDao
import com.luigguidev.todo_list.model.database.task.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModel(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) : ViewModel() {
    private val _initNoId = "noid"
    private val _initName = ""
    private val _initDescription = ""
    private val _initLimitDate = ""
    private val _initDate = ""
    private val _initBtn = false
    private val _initExpanded = false
    private var _initCategoryName = "Todos"
    private val _initIdCategory = 0


    private val _taskList = MutableStateFlow<List<TaskEntity>>(emptyList())
    val taskList = _taskList.asStateFlow()

    private val _name = MutableStateFlow(_initName)
    val name = _name.asStateFlow()

    private val _description = MutableStateFlow(_initDescription)
    val description = _description.asStateFlow()

    private val _limitDate = MutableStateFlow(_initLimitDate)
    val limitDate = _limitDate.asStateFlow()

    private val _addBtn = MutableStateFlow(_initBtn)
    val addBtn = _addBtn.asStateFlow()

    private val _dateNow = MutableStateFlow(_initDate)
    val dateNow = _dateNow.asStateFlow()

    //category
    private val _expanded = MutableStateFlow(_initExpanded)
    val expanded = _expanded.asStateFlow()

    private val _selectOption = MutableStateFlow(_initCategoryName)
    val selectOption = _selectOption.asStateFlow()

    private val _idCategory = MutableStateFlow(_initIdCategory)

    private val _noId = MutableStateFlow(_initNoId)
    val noId = _noId.asStateFlow()


    private val _taskObject = MutableStateFlow(
        TaskEntity(
            name = _initName,
            description = _initDescription,
            limitDate = _initLimitDate,
            idOwnerCategory = 0
        )
    )
    val taskObject = _taskObject.asStateFlow()

    init {
        viewModelScope.launch {
            dateApp()
            taskDao.getAllTasks().collect {
                _taskList.value = it
            }
        }

    }

    private fun dateApp() {
        val currentDate = LocalDate.now()
        val nameDay = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val monthDay = currentDate.dayOfMonth
        val monthName = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

        _dateNow.value = "${capitalize(nameDay)} - $monthDay ${capitalize(monthName)}"
    }

    private fun capitalize(text: String): String {
        if (text.isEmpty()) {
            return text
        }
        val nowText = text[0].uppercaseChar()
        return nowText + text.substring(1)
    }

    fun taskAdd() {
        val task = TaskEntity(
            name = capitalize(_name.value),
            description = capitalize(_description.value),
            limitDate = _limitDate.value,
            idOwnerCategory = _idCategory.value
        )
        viewModelScope.launch {
            try {
                taskDao.insertTask(task = task)
                formClear()
            } catch (e: SQLiteConstraintException) {
                Log.i("SQLiteException", "Error en la base de datos: $e")
            }
        }

    }

    fun formValidate(name: String, description: String, limitDate: String) {
        _name.value = name
        _description.value = description
        _limitDate.value = limitDate
        _addBtn.value =
            isValidName(_name.value) && isValidDescription(_description.value) && isValidDate(
                _limitDate.value
            )
    }

    private fun isValidDate(value: String): Boolean = value.isNotEmpty()

    private fun isValidDescription(description: String): Boolean = description.isNotEmpty()

    private fun isValidName(name: String): Boolean = name.isNotEmpty()

    fun formClear() {
        _selectOption.value = _initCategoryName
        _name.value = _initName
        _description.value = _initDescription
        _limitDate.value = _initLimitDate
        _addBtn.value = _initBtn
    }

    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }

    fun setSelectCategory(value: String) {
        _selectOption.value = value
    }

    fun setIdCategory(id: Int) {
        _idCategory.value = id
    }

    fun updateTaskState(taskEntity: TaskEntity) {
        val newTask = taskEntity.copy(state = true)
        viewModelScope.launch {
            taskDao.updateTaskState(newTask)
        }
    }

    fun updateTask(id: Int) {
        val taskEntity = TaskEntity(
            idTask = id,
            name = _name.value,
            description = _description.value,
            limitDate = _limitDate.value,
            idOwnerCategory = _idCategory.value
        )
        viewModelScope.launch {
            taskDao.updateTask(taskEntity)
            formClear()
        }
    }

    fun getById(id: Int?) {
        viewModelScope.launch {
            _taskObject.value = taskDao.getById(id)

            val idCategory = categoryDao.getById(id = id)

            _idCategory.value = _taskObject.value.idOwnerCategory
            _selectOption.value = idCategory.name
            _name.value = _taskObject.value.name
            _description.value = _taskObject.value.description
            _limitDate.value = _taskObject.value.limitDate
        }
    }

    fun getAllTaskByCategory(categoryId: Int) {

        viewModelScope.launch {
            if (categoryId != 1) {
                taskDao.getAllTaskByCategory(categoryId).collect {
                    _taskList.value = it

                }
            }else{
                taskDao.getAllTasks().collect{
                    _taskList.value = it
                }
            }
        }
    }
}