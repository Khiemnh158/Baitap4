package com.example.bai4.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bai4.R
import com.example.bai4.data.Student
import com.example.bai4.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    studentId: Int? = null,
    viewModel: StudentViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var studentCode by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    
    var showConfirmDialog by remember { mutableStateOf(false) }
    var currentStudent by remember { mutableStateOf<Student?>(null) }
    
    val isEditMode = studentId != null
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            try {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            avatarUrl = uri.toString()
        }
    }

    LaunchedEffect(studentId) {
        if (studentId != null) {
            val s = viewModel.getStudentById(studentId)
            currentStudent = s
            s?.let {
                name = it.name
                studentCode = it.studentId
                email = it.email
                avatarUrl = it.avatarUrl
            }
        }
    }

    val isStudentCodeValid = studentCode.matches(Regex("^B[a-zA-Z]{2}(22|23|24|25|26)\\d{4}$"))
    val showCodeError = studentCode.isNotEmpty() && !isStudentCodeValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (isEditMode) stringResource(id = R.string.edit_student) 
                        else stringResource(id = R.string.add_student)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = studentCode,
                onValueChange = { studentCode = it },
                label = { Text(stringResource(id = R.string.student_id)) },
                isError = showCodeError,
                supportingText = {
                    if (showCodeError) {
                        Text(
                            text = "Mã SV phải có dạng: B + 2 chữ cái + (22-26) + 4 số",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = avatarUrl,
                    onValueChange = { avatarUrl = it },
                    label = { Text(stringResource(id = R.string.avatar_url)) },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { 
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) 
                }) {
                    Text("Chọn ảnh")
                }
            }
            
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && isStudentCodeValid
            ) {
                Text(stringResource(id = R.string.save))
            }
        }
        
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text(stringResource(id = R.string.confirm)) },
                text = { 
                    Text(
                        if (isEditMode) stringResource(id = R.string.confirm_edit)
                        else stringResource(id = R.string.confirm_add)
                    ) 
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val student = Student(
                                id = currentStudent?.id ?: 0,
                                studentId = studentCode,
                                name = name,
                                email = email,
                                avatarUrl = avatarUrl
                            )
                            if (isEditMode) {
                                viewModel.updateStudent(student)
                            } else {
                                viewModel.insertStudent(student)
                            }
                            showConfirmDialog = false
                            onNavigateBack()
                        }
                    ) {
                        Text(stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text(stringResource(id = R.string.no))
                    }
                }
            )
        }
    }
}
