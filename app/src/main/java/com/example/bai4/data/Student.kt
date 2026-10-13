package com.example.bai4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentId: String,
    val name: String,
    val email: String,
    val avatarUrl: String
)
