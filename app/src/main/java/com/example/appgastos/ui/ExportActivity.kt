package com.example.appgastos.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appgastos.R
import com.example.appgastos.data.AppDatabase
import com.example.appgastos.util.CsvExporter
import kotlinx.coroutines.launch

class ExportActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        database = AppDatabase.getInstance(this)
        setupViews()
    }

    private fun setupViews() {
        val btnExportData = findViewById<android.widget.Button>(R.id.btnExportData)
        val btnBackToMain = findViewById<android.widget.Button>(R.id.btnBackToMain)
        val tvExportStatus = findViewById<android.widget.TextView>(R.id.tvExportStatus)

        btnExportData.setOnClickListener {
            exportData(tvExportStatus)
        }

        btnBackToMain.setOnClickListener {
            finish()
        }
    }

    private fun exportData(statusTextView: android.widget.TextView) {
        lifecycleScope.launch {
            try {
                statusTextView.text = "Exportando datos..."
                
                val categories = database.categoryDao().getAll()
                val expenses = database.expenseDao().getAll()
                
                if (categories.isEmpty() && expenses.isEmpty()) {
                    statusTextView.text = "No hay datos para exportar"
                    Toast.makeText(this@ExportActivity, "No hay datos para exportar", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                val file = CsvExporter.export(this@ExportActivity, categories, expenses)
                
                // Limpiar solo los gastos después de exportar (conservar categorías)
                database.expenseDao().deleteAll()
                
                statusTextView.text = "Datos exportados exitosamente a: ${file.absolutePath}\nGastos eliminados del dispositivo (categorías conservadas)."
                Toast.makeText(this@ExportActivity, "Exportación completada", Toast.LENGTH_LONG).show()
                
            } catch (e: Exception) {
                statusTextView.text = "Error durante la exportación: ${e.message}"
                Toast.makeText(this@ExportActivity, "Error al exportar datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}