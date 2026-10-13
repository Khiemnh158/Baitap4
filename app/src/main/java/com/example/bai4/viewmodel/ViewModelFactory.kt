package com.example.bai4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bai4.data.StudentDao

class ViewModelFactory(private val studentDao: StudentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(studentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
