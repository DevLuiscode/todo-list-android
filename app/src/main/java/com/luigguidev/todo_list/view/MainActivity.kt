package com.luigguidev.todo_list.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.luigguidev.todo_list.config.routes.AppRoutes
import com.luigguidev.todo_list.config.ui.theme.TodolistTheme
import com.luigguidev.todo_list.model.database.AppDatabase
import com.luigguidev.todo_list.model.database.task.TaskEntity
import com.luigguidev.todo_list.view.taskscreen.AddScreen
import com.luigguidev.todo_list.view.taskscreen.TaskScreen
import com.luigguidev.todo_list.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            name = "todolist_db"
        ).build()

        setContent {
            val navController = rememberNavController()
            val sharedViewModel = remember { SharedViewModel(database) }
            TodolistTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.TaskScreen.route,
                    ) {
                        composable(AppRoutes.TaskScreen.route) {
                            TaskScreen(
                                sharedViewModel = sharedViewModel,
                                navController = navController
                            )
                        }
                        composable("${AppRoutes.AddScreen.route}/{id}") { navBackStackEntry->

                            val task = navBackStackEntry.arguments?.getString("id")
                            task?.let {id ->
                                AddScreen(
                                    sharedViewModel = sharedViewModel,
                                    navController = navController,
                                    id = id
                                )
                            }

                        }
                    }

                }
            }
        }
    }
}
