package com.example.appgastos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val date: Long, // Fecha en formato timestamp
    val name: String,
    val amount: Double // Euros
)
