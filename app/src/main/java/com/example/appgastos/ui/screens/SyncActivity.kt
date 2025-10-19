package com.example.appgastos.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appgastos.R
import com.example.appgastos.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class SyncActivity : AppCompatActivity() {
    
    private lateinit var database: AppDatabase
    private lateinit var editTextServerIP: EditText
    private lateinit var editTextServerPort: EditText
    private lateinit var btnDiscover: Button
    private lateinit var btnSync: Button
    private lateinit var btnBack: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewStatus: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)
        
        database = AppDatabase.getInstance(this)
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        editTextServerIP = findViewById(R.id.editTextServerIP)
        editTextServerPort = findViewById(R.id.editTextServerPort)
        btnDiscover = findViewById(R.id.btnDiscover)
        btnSync = findViewById(R.id.btnSync)
        btnBack = findViewById(R.id.btnBack)
        progressBar = findViewById(R.id.progressBar)
        textViewStatus = findViewById(R.id.textViewStatus)
        
        // Valores por defecto
        editTextServerIP.setText("127.0.0.1")
        editTextServerPort.setText("8000")
    }
    
    private fun setupClickListeners() {
        btnDiscover.setOnClickListener {
            discoverServer()
        }
        
        btnSync.setOnClickListener {
            val serverIP = editTextServerIP.text.toString().trim()
            val serverPort = editTextServerPort.text.toString().trim()
            
            if (serverIP.isEmpty() || serverPort.isEmpty()) {
                Toast.makeText(this, "Por favor, completa la IP y puerto del servidor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            showSyncConfirmationDialog(serverIP, serverPort.toIntOrNull() ?: 8000)
        }
        
        btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun syncWithServer(serverIP: String, serverPort: Int) {
        lifecycleScope.launch {
            try {
                // Mostrar progreso
                progressBar.visibility = android.view.View.VISIBLE
                btnSync.isEnabled = false
                textViewStatus.text = "Generando CSV..."
                
                // Generar CSV
                val csvFile = withContext(Dispatchers.IO) {
                    generateCsvFile()
                }
                
                textViewStatus.text = "Enviando al servidor..."
                
                // Enviar al servidor
                val success = withContext(Dispatchers.IO) {
                    uploadCsvToServer(csvFile, serverIP, serverPort)
                }
                
                if (success) {
                    textViewStatus.text = "✅ Sincronización exitosa"
                    Toast.makeText(this@SyncActivity, "Datos sincronizados correctamente", Toast.LENGTH_SHORT).show()
                    
                    // Limpiar gastos después de la sincronización exitosa
                    withContext(Dispatchers.IO) {
                        database.expenseDao().deleteAll()
                    }
                } else {
                    textViewStatus.text = "❌ Error en la sincronización"
                    Toast.makeText(this@SyncActivity, "Error al sincronizar datos", Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                textViewStatus.text = "❌ Error: ${e.message}"
                Toast.makeText(this@SyncActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
                btnSync.isEnabled = true
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
    
    private fun uploadCsvToServer(csvFile: File, serverIP: String, serverPort: Int): Boolean {
        return try {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            
            val requestBody = csvFile.asRequestBody("text/csv".toMediaType())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("csv_file", csvFile.name, requestBody)
                .build()
            
            val request = Request.Builder()
                .url("http://$serverIP:$serverPort/api/csv/upload/")
                .post(multipartBody)
                .build()
            
            val response = client.newCall(request).execute()
            val isSuccess = response.isSuccessful
            
            // Limpiar archivo temporal
            csvFile.delete()
            
            isSuccess
        } catch (e: Exception) {
            csvFile.delete()
            false
        }
    }
    
    private fun showSyncConfirmationDialog(serverIP: String, serverPort: Int) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Confirmar Sincronización")
            .setMessage("¿Estás seguro de que quieres enviar todos los datos a:\n\n" +
                       "🖥️ Servidor: $serverIP:$serverPort\n\n" +
                       "⚠️ ADVERTENCIA: Todos los gastos locales se BORRARÁN después de la sincronización exitosa.\n\n" +
                       "Esta acción no se puede deshacer.")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("✅ Sí, Sincronizar") { _, _ ->
                syncWithServer(serverIP, serverPort)
            }
            .setNegativeButton("❌ Cancelar", null)
            .show()
    }
    
    private fun discoverServer() {
        lifecycleScope.launch {
            try {
                // Mostrar progreso
                progressBar.visibility = android.view.View.VISIBLE
                btnDiscover.isEnabled = false
                btnSync.isEnabled = false
                textViewStatus.text = "🔍 Buscando servidor..."
                
                val discoveredIP = withContext(Dispatchers.IO) {
                    scanNetworkForServer()
                }
                
                if (discoveredIP != null) {
                    editTextServerIP.setText(discoveredIP)
                    textViewStatus.text = "✅ Servidor encontrado en $discoveredIP - Pulsa 'Sincronizar' para enviar datos"
                    Toast.makeText(this@SyncActivity, "Servidor encontrado: $discoveredIP\nAhora puedes sincronizar manualmente", Toast.LENGTH_LONG).show()
                } else {
                    textViewStatus.text = "❌ No se encontró servidor en la red"
                    Toast.makeText(this@SyncActivity, "No se encontró servidor en la red", Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                textViewStatus.text = "❌ Error en búsqueda: ${e.message}"
                Toast.makeText(this@SyncActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
                btnDiscover.isEnabled = true
                btnSync.isEnabled = true
            }
        }
    }
    
    private suspend fun scanNetworkForServer(): String? {
        val localIP = getLocalIP()
        if (localIP.isEmpty()) return null
        
        val subnet = localIP.substringBeforeLast(".")
        val port = editTextServerPort.text.toString().toIntOrNull() ?: 8000
        
        // IPs más comunes para probar primero (paralelo)
        val commonIPs = listOf(1, 100, 101, 102, 103, 104, 105, 254)
        
        withContext(Dispatchers.Main) {
            textViewStatus.text = "🔍 Escaneando IPs comunes..."
        }
        
        // Escaneo paralelo de IPs comunes (más rápido)
        val commonResults = withContext(Dispatchers.IO) {
            commonIPs.map { lastOctet ->
                async {
                    val testIP = "$subnet.$lastOctet"
                    if (testServerEndpoint(testIP, port)) testIP else null
                }
            }.awaitAll().filterNotNull()
        }
        
        if (commonResults.isNotEmpty()) {
            return commonResults.first()
        }
        
        withContext(Dispatchers.Main) {
            textViewStatus.text = "🔍 Escaneando red completa..."
        }
        
        // Si no encuentra en comunes, escanear en chunks paralelos
        val allIPs = (2..253).filter { !commonIPs.contains(it) }
        val chunkSize = 20 // Procesar 20 IPs en paralelo
        
        for (chunk in allIPs.chunked(chunkSize)) {
            val results = withContext(Dispatchers.IO) {
                chunk.map { i ->
                    async {
                        val testIP = "$subnet.$i"
                        if (testServerEndpoint(testIP, port)) testIP else null
                    }
                }.awaitAll().filterNotNull()
            }
            
            if (results.isNotEmpty()) {
                return results.first()
            }
        }
        
        return null
    }
    
    private fun testServerEndpoint(ip: String, port: Int): Boolean {
        return try {
            android.util.Log.d("SyncActivity", "Probando conexión a http://$ip:$port/api/csv/server-info/")
            
            val client = OkHttpClient.Builder()
                .connectTimeout(800, TimeUnit.MILLISECONDS)
                .readTimeout(800, TimeUnit.MILLISECONDS)
                .build()
            
            val request = Request.Builder()
                .url("http://$ip:$port/api/csv/server-info/")
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            
            android.util.Log.d("SyncActivity", "Respuesta de $ip: ${response.code} - $responseBody")
            
            // Verificar que la respuesta contiene la identificación de tu API
            val isSuccess = response.isSuccessful && responseBody.contains("AppGastos API")
            if (isSuccess) {
                android.util.Log.d("SyncActivity", "¡Servidor encontrado en $ip:$port!")
            }
            isSuccess
        } catch (e: Exception) {
            android.util.Log.d("SyncActivity", "Error conectando a $ip:$port - ${e.message}")
            false
        }
    }
    
    private fun getLocalIP(): String {
        return try {
            val networkInterfaces = java.net.NetworkInterface.getNetworkInterfaces()
            for (networkInterface in networkInterfaces) {
                if (!networkInterface.isLoopback && networkInterface.isUp) {
                    val addresses = networkInterface.inetAddresses
                    for (address in addresses) {
                        if (!address.isLoopbackAddress && address is java.net.Inet4Address) {
                            val ip = address.hostAddress ?: ""
                            android.util.Log.d("SyncActivity", "IP encontrada: $ip")
                            // Buscar redes privadas comunes
                            if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
                                android.util.Log.d("SyncActivity", "Usando IP local: $ip")
                                return ip
                            }
                        }
                    }
                }
            }
            android.util.Log.d("SyncActivity", "No se encontró IP local válida")
            ""
        } catch (e: Exception) {
            android.util.Log.e("SyncActivity", "Error obteniendo IP local: ${e.message}")
            ""
        }
    }
}