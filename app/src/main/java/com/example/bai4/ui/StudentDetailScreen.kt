package com.example.bai4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bai4.R
import com.example.bai4.data.Student
import com.example.bai4.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    studentId: Int,
    viewModel: StudentViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    var student by remember { mutableStateOf<Student?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(studentId) {
        student = viewModel.getStudentById(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.student_detail)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(studentId) }) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(id = R.string.edit))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        student?.let { currentStudent ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = currentStudent.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "${stringResource(id = R.string.name)}: ${currentStudent.name}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "${stringResource(id = R.string.student_id)}: ${currentStudent.studentId}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "${stringResource(id = R.string.email)}: ${currentStudent.email}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text(stringResource(id = R.string.confirm)) },
                    text = { Text(stringResource(id = R.string.confirm_delete)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteStudent(currentStudent)
                                showDeleteDialog = false
                                onNavigateBack()
                            }
                        ) {
                            Text(stringResource(id = R.string.yes))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(stringResource(id = R.string.no))
                        }
                    }
                )
            }
        }
    }
}
