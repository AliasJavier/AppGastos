package com.example.appgastos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appgastos.model.Category
import com.example.appgastos.model.Expense

@Database(entities = [Category::class, Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_gastos_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
