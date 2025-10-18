package com.example.appgastos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appgastos.R
import com.example.appgastos.data.AppDatabase
import com.example.appgastos.model.Category
import kotlinx.coroutines.launch

class CategoriesActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var adapter: ArrayAdapter<String>
    private val categories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        database = AppDatabase.getInstance(this)
        setupViews()
        loadCategories()
    }

    private fun setupViews() {
        val etCategoryName = findViewById<android.widget.EditText>(R.id.etCategoryName)
        val btnAddCategory = findViewById<android.widget.Button>(R.id.btnAddCategory)
        val lvCategories = findViewById<ListView>(R.id.lvCategories)
        val btnBackToMain = findViewById<android.widget.Button>(R.id.btnBackToMain)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        lvCategories.adapter = adapter

        lvCategories.setOnItemClickListener { _, _, position, _ ->
            if (position < categories.size) {
                val category = categories[position]
                showDeleteConfirmation(category)
            }
        }

        btnAddCategory.setOnClickListener {
            val name = etCategoryName.text.toString().trim()
            if (name.isNotEmpty()) {
                addCategory(name)
                etCategoryName.text.clear()
            } else {
                Toast.makeText(this, "Ingresa un nombre para la categoría", Toast.LENGTH_SHORT).show()
            }
        }

        btnBackToMain.setOnClickListener {
            finish()
        }
    }

    private fun addCategory(name: String) {
        lifecycleScope.launch {
            try {
                val category = Category(name = name)
                database.categoryDao().insert(category)
                loadCategories()
                Toast.makeText(this@CategoriesActivity, "Categoría añadida", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@CategoriesActivity, "Error al añadir categoría", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                categories.clear()
                categories.addAll(database.categoryDao().getAll())
                val categoryNames = categories.map { it.name }
                adapter.clear()
                adapter.addAll(categoryNames)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@CategoriesActivity, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmation(category: Category) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar Categoría")
            .setMessage("¿Estás seguro de que deseas eliminar la categoría '${category.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteCategory(category)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteCategory(category: Category) {
        lifecycleScope.launch {
            try {
                // Verificar si hay gastos asociados a esta categoría
                val expenses = database.expenseDao().getByCategory(category.id)
                if (expenses.isNotEmpty()) {
                    Toast.makeText(this@CategoriesActivity, 
                        "No se puede eliminar. Hay ${expenses.size} gasto(s) asociado(s) a esta categoría", 
                        Toast.LENGTH_LONG).show()
                    return@launch
                }
                
                database.categoryDao().delete(category)
                loadCategories()
                Toast.makeText(this@CategoriesActivity, "Categoría eliminada", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@CategoriesActivity, "Error al eliminar categoría", Toast.LENGTH_SHORT).show()
            }
        }
    }
}