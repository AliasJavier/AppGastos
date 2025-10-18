package com.example.appgastos.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appgastos.R
import com.example.appgastos.data.AppDatabase
import com.example.appgastos.model.Category
import com.example.appgastos.model.Expense
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog

class ExpensesActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var categorySpinner: Spinner
    private lateinit var expenseAdapter: ArrayAdapter<String>
    private val categories = mutableListOf<Category>()
    private val expenses = mutableListOf<Expense>()
    private var selectedDate: Long = System.currentTimeMillis()
    private lateinit var tvSelectedDate: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        database = AppDatabase.getInstance(this)
        setupViews()
        loadCategories()
        loadExpenses()
    }

    private fun setupViews() {
        val etExpenseName = findViewById<android.widget.EditText>(R.id.etExpenseName)
        val etExpenseAmount = findViewById<android.widget.EditText>(R.id.etExpenseAmount)
        val btnAddExpense = findViewById<android.widget.Button>(R.id.btnAddExpense)
        val lvExpenses = findViewById<ListView>(R.id.lvExpenses)
        val btnBackToMain = findViewById<android.widget.Button>(R.id.btnBackToMain)
        val btnSelectDate = findViewById<android.widget.Button>(R.id.btnSelectDate)
        categorySpinner = findViewById(R.id.spCategories)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)

        expenseAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        lvExpenses.adapter = expenseAdapter
        
        // Mostrar fecha inicial
        updateDateDisplay()

        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        btnAddExpense.setOnClickListener {
            val name = etExpenseName.text.toString().trim()
            val amountText = etExpenseAmount.text.toString().trim()
            
            if (name.isNotEmpty() && amountText.isNotEmpty() && categories.isNotEmpty()) {
                try {
                    val amount = amountText.toDouble()
                    val selectedCategory = categories[categorySpinner.selectedItemPosition]
                    addExpense(selectedCategory.id, name, amount)
                    etExpenseName.text.clear()
                    etExpenseAmount.text.clear()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnBackToMain.setOnClickListener {
            finish()
        }
    }

    private fun addExpense(categoryId: Int, name: String, amount: Double) {
        lifecycleScope.launch {
            try {
                val expense = Expense(
                    categoryId = categoryId,
                    date = selectedDate,
                    name = name,
                    amount = amount
                )
                database.expenseDao().insert(expense)
                loadExpenses()
                Toast.makeText(this@ExpensesActivity, "Gasto añadido", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ExpensesActivity, "Error al añadir gasto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                categories.clear()
                categories.addAll(database.categoryDao().getAll())
                val categoryNames = categories.map { it.name }
                val spinnerAdapter = ArrayAdapter(this@ExpensesActivity, android.R.layout.simple_spinner_item, categoryNames)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = spinnerAdapter
            } catch (e: Exception) {
                Toast.makeText(this@ExpensesActivity, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            try {
                expenses.clear()
                expenses.addAll(database.expenseDao().getAll())
                val expenseStrings = expenses.map { expense ->
                    val category = categories.find { it.id == expense.categoryId }?.name ?: "Sin categoría"
                    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(expense.date))
                    "${expense.name} - €${expense.amount} ($category) - $date"
                }
                expenseAdapter.clear()
                expenseAdapter.addAll(expenseStrings)
                expenseAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@ExpensesActivity, "Error al cargar gastos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate
        
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(year, month, dayOfMonth)
                selectedDate = newCalendar.timeInMillis
                updateDateDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        tvSelectedDate.text = "Fecha: ${dateFormat.format(Date(selectedDate))}"
    }
}