package com.example.appgastos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExportScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Exportar datos a CSV", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Exportar y borrar datos */ }) {
            Text("Exportar y borrar")
        }
        // Aquí se puede mostrar un mensaje de éxito/error
    }
}
