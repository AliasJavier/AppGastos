package com.example.appgastos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appgastos.model.Category
import com.example.appgastos.model.Expense

@Database(entities = [Category::class, Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
}
