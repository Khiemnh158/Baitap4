package com.example.bai4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bai4.data.Student
import com.example.bai4.data.StudentDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(private val studentDao: StudentDao) : ViewModel() {

    val allStudents: StateFlow<List<Student>> = studentDao.getAllStudents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertStudent(student: Student) {
        viewModelScope.launch {
            studentDao.insertStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            studentDao.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentDao.deleteStudent(student)
        }
    }

    suspend fun getStudentById(id: Int): Student? {
        return studentDao.getStudentById(id)
    }
}
