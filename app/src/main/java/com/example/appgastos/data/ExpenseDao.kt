package com.example.appgastos.data

import androidx.room.*
import com.example.appgastos.model.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    suspend fun getAll(): List<Expense>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId")
    suspend fun getByCategory(categoryId: Int): List<Expense>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Delete
    suspend fun delete(expense: Expense)

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()
}
