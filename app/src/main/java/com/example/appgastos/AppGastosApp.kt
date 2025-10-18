package com.example.appgastos

import android.app.Application
import androidx.room.Room
import com.example.appgastos.data.AppDatabase

class AppGastosApp : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "appgastos-db"
        ).build()
    }
}
