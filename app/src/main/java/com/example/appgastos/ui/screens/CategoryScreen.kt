package com.example.appgastos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryScreen() {
    var categoryName by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Crear nueva categoría", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Nombre de la categoría") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Guardar categoría en la base de datos */ }) {
            Text("Guardar")
        }
        // Aquí se mostraría la lista de categorías existentes
    }
}
