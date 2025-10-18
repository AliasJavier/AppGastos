package com.example.appgastos.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appgastos.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupViews()
    }
    
    private fun setupViews() {
        val btnCategories = findViewById<android.widget.Button>(R.id.btnCategories)
        val btnExpenses = findViewById<android.widget.Button>(R.id.btnExpenses)
        val btnSync = findViewById<android.widget.Button>(R.id.btnSync)
        val btnExport = findViewById<android.widget.Button>(R.id.btnExport)
        
        btnCategories.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }
        
        btnExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesActivity::class.java))
        }
        
        btnSync.setOnClickListener {
            startActivity(Intent(this, com.example.appgastos.ui.screens.SyncActivity::class.java))
        }
        
        btnExport.setOnClickListener {
            startActivity(Intent(this, ExportActivity::class.java))
        }
    }
}
