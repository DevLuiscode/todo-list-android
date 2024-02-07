package com.luigguidev.todo_list.viewmodel.categoryViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luigguidev.todo_list.model.database.category.CategoryDao
import com.luigguidev.todo_list.model.database.category.CategoryEntity
import com.luigguidev.todo_list.model.database.relations.CategoryWithTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryDao: CategoryDao
) : ViewModel() {
    private val _nameInit = ""
    private val _btnInit = false

    private val _categoryList = MutableStateFlow<List<CategoryWithTask>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    private val _name = MutableStateFlow(_nameInit)
    val nameCategory = _name.asStateFlow()

    private val _btn = MutableStateFlow(_btnInit)
    val btn = _btn.asStateFlow()

    private val _categoryId = MutableStateFlow(CategoryEntity(name = ""))
    val categoryId = _categoryId.asStateFlow()

    init {
        viewModelScope.launch {
            val existingCategories = categoryDao.getAllCategories().first()
            if (existingCategories.isEmpty()) {
                categoryAdd("Todos")
            }
            categoryDao.getAllCategories().collect {
                _categoryList.value = it
            }
        }
    }

    fun categoryAdd(name: String) {

        val category = CategoryEntity(name = capitalize(name.trim()))
        viewModelScope.launch {
            categoryDao.insertCategory(category)
            formClear()
        }
    }

    fun categoryForm(name: String) {
        _name.value = name
        _btn.value = isValidateName(_name.value)
    }

    private fun isValidateName(name: String): Boolean = name.isNotEmpty()
    fun formClear() {
        _name.value = _nameInit
        _btn.value = _btnInit
    }

    private fun capitalize(text: String): String {
        if (text.isEmpty()) {
            return text
        }
        val nowText = text[0].uppercaseChar()
        return nowText + text.substring(1)
    }

    fun setCategoryIdObject(objectCategory: CategoryEntity) {
        viewModelScope.launch {
            _categoryId.value = objectCategory
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            if (categoryEntity.idCategory != 1) categoryDao.deleteCategory(categoryEntity)
        }
    }

    fun getById(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            val objectCategory = categoryDao.getById(categoryEntity.idCategory)
            _name.value = objectCategory.name
        }
    }

    fun updateCategory(id: Int) {
        val newCategory = CategoryEntity(
            idCategory = id,
            name = _name.value
        )
        viewModelScope.launch {
            categoryDao.updateCategory(newCategory)
            formClear()
        }
    }

}