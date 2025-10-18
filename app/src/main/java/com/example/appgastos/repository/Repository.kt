package com.example.appgastos.repository

import android.content.Context
import com.example.appgastos.data.AppDatabase
import com.example.appgastos.model.Category
import com.example.appgastos.model.Expense

class Repository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val categoryDao = db.categoryDao()
    private val expenseDao = db.expenseDao()

    suspend fun getCategories(): List<Category> = categoryDao.getAll()
    suspend fun addCategory(category: Category) = categoryDao.insert(category)
    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    suspend fun getExpenses(): List<Expense> = expenseDao.getAll()
    suspend fun getExpensesByCategory(categoryId: Int): List<Expense> = expenseDao.getByCategory(categoryId)
    suspend fun addExpense(expense: Expense) = expenseDao.insert(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)
    suspend fun deleteAllExpenses() = expenseDao.deleteAll()
}
