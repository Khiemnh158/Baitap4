package com.example.bai4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bai4.data.AppDatabase
import com.example.bai4.ui.StudentDetailScreen
import com.example.bai4.ui.StudentFormScreen
import com.example.bai4.ui.StudentListScreen
import com.example.bai4.ui.theme.Bai4Theme
import com.example.bai4.viewmodel.StudentViewModel
import com.example.bai4.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val viewModelFactory = ViewModelFactory(database.studentDao())

        enableEdgeToEdge()
        setContent {
            Bai4Theme {
                val navController = rememberNavController()
                val studentViewModel: StudentViewModel = viewModel(factory = viewModelFactory)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "student_list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("student_list") {
                            StudentListScreen(
                                viewModel = studentViewModel,
                                onNavigateToDetail = { id ->
                                    navController.navigate("student_detail/$id")
                                },
                                onNavigateToAdd = {
                                    navController.navigate("student_form")
                                }
                            )
                        }
                        
                        composable(
                            route = "student_detail/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 0
                            StudentDetailScreen(
                                studentId = id,
                                viewModel = studentViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToEdit = { editId ->
                                    navController.navigate("student_form?id=$editId")
                                }
                            )
                        }
                        
                        composable(
                            route = "student_form?id={id}",
                            arguments = listOf(navArgument("id") { 
                                type = NavType.StringType
                                nullable = true 
                            })
                        ) { backStackEntry ->
                            val idString = backStackEntry.arguments?.getString("id")
                            val id = idString?.toIntOrNull()
                            StudentFormScreen(
                                studentId = id,
                                viewModel = studentViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
