package com.luigguidev.todo_list.config.routes

sealed class AppRoutes(val route: String) {
    data object TaskScreen : AppRoutes(route = "TaskScreen")
    data object AddScreen : AppRoutes(route = "AddScreen")
}
