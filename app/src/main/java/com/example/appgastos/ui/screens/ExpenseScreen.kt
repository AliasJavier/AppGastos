package com.example.appgastos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpenseScreen() {
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseDate by remember { mutableStateOf("") } // Aquí se puede usar un DatePicker
    var selectedCategory by remember { mutableStateOf(0) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Añadir gasto", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Nombre del gasto") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Cantidad (€)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = expenseDate,
            onValueChange = { expenseDate = it },
            label = { Text("Fecha (YYYY-MM-DD)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Aquí se mostraría un Dropdown con las categorías
        Button(onClick = { /* Guardar gasto en la base de datos */ }) {
            Text("Guardar")
        }
        // Aquí se mostraría la lista de gastos existentes
    }
}
