package com.example.appgastos.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.activity.result.contract.ActivityResultContracts
import com.example.appgastos.R
import com.example.appgastos.data.AppDatabase
import com.example.appgastos.util.CsvExporter
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

class ExportActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private var tempCsvFile: File? = null
    
    private val createCsvLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {
            saveCsvToUri(uri)
        } else {
            // Usuario canceló, limpiar archivo temporal
            tempCsvFile?.delete()
            tempCsvFile = null
            findViewById<android.widget.TextView>(R.id.tvExportStatus).text = "Operación cancelada"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        database = AppDatabase.getInstance(this)
        setupViews()
    }

    private fun setupViews() {
        val btnSaveLocal = findViewById<android.widget.Button>(R.id.btnSaveLocal)
        val btnBackToMain = findViewById<android.widget.Button>(R.id.btnBackToMain)
        val tvExportStatus = findViewById<android.widget.TextView>(R.id.tvExportStatus)

        btnSaveLocal.setOnClickListener {
            saveLocalCsv(tvExportStatus)
        }

        btnBackToMain.setOnClickListener {
            finish()
        }
    }

    private fun saveLocalCsv(statusTextView: android.widget.TextView) {
        lifecycleScope.launch {
            try {
                statusTextView.text = "📄 Generando CSV..."
                
                val csvFile = withContext(Dispatchers.IO) {
                    generateCsvFile()
                }
                
                statusTextView.text = "💾 Elige dónde guardar..."
                
                // Guardar referencia del archivo temporal
                tempCsvFile = csvFile
                
                // Lanzar selector de archivos con el nuevo sistema
                val fileName = "gastos_${java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())}.csv"
                createCsvLauncher.launch(fileName)
                
            } catch (e: Exception) {
                statusTextView.text = "❌ Error: ${e.message}"
                Toast.makeText(this@ExportActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private suspend fun generateCsvFile(): File {
        val expenses = database.expenseDao().getAll()
        val categories = database.categoryDao().getAll()
        
        val categoryMap = categories.associateBy { it.id }
        
        val csvFile = File(cacheDir, "expenses_${System.currentTimeMillis()}.csv")
        val fileWriter = FileWriter(csvFile)
        
        // Escribir encabezados
        fileWriter.append("Fecha,Categoría,Descripción,Cantidad\n")
        
        // Escribir datos
        expenses.forEach { expense ->
            val categoryName = categoryMap[expense.categoryId]?.name ?: "Sin categoría"
            fileWriter.append("${expense.date},${categoryName},${expense.name},${expense.amount}\n")
        }
        
        fileWriter.close()
        return csvFile
    }
    
    private fun saveCsvToUri(uri: android.net.Uri) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    tempCsvFile?.let { csvFile ->
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            csvFile.inputStream().use { inputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        csvFile.delete() // Limpiar archivo temporal
                    }
                }
                
                val statusTextView = findViewById<android.widget.TextView>(R.id.tvExportStatus)
                statusTextView.text = "✅ CSV guardado exitosamente"
                Toast.makeText(this@ExportActivity, "CSV guardado exitosamente", Toast.LENGTH_SHORT).show()
                
                // Preguntar si quiere limpiar los datos
                showCleanupDialog()
                
            } catch (e: Exception) {
                val statusTextView = findViewById<android.widget.TextView>(R.id.tvExportStatus)
                statusTextView.text = "❌ Error guardando: ${e.message}"
                Toast.makeText(this@ExportActivity, "Error guardando archivo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showCleanupDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("🧹 ¿Limpiar Datos?")
            .setMessage("CSV guardado exitosamente ✅\n\n" +
                       "¿Quieres eliminar los gastos del móvil para liberar espacio?\n\n" +
                       "📝 Se borrarán: Todos los gastos\n" +
                       "✅ Se conservarán: Las categorías\n\n" +
                       "Puedes elegir 'No' si quieres mantener los datos en el móvil.")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("🗑️ Sí, Limpiar") { _, _ ->
                cleanupExpenses()
            }
            .setNegativeButton("📱 No, Mantener", null)
            .show()
    }
    
    private fun cleanupExpenses() {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    database.expenseDao().deleteAll()
                }
                
                val statusTextView = findViewById<android.widget.TextView>(R.id.tvExportStatus)
                statusTextView.text = "✅ CSV guardado y gastos eliminados (categorías conservadas)"
                Toast.makeText(this@ExportActivity, "Datos limpiados correctamente", Toast.LENGTH_SHORT).show()
                
            } catch (e: Exception) {
                Toast.makeText(this@ExportActivity, "Error al limpiar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}