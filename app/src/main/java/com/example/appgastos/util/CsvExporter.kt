package com.example.appgastos.util

import android.content.Context
import java.io.File
import java.io.FileWriter
import com.example.appgastos.model.Category
import com.example.appgastos.model.Expense

object CsvExporter {
    fun export(context: Context, categories: List<Category>, expenses: List<Expense>): File {
        val file = File(context.filesDir, "gastos_export.csv")
        val writer = FileWriter(file)
        writer.append("ID Categoría,Nombre Categoría\n")
        categories.forEach {
            writer.append("${it.id},${it.name}\n")
        }
        writer.append("\nID Gasto,ID Categoría,Fecha,Nombre,Importe\n")
        expenses.forEach {
            writer.append("${it.id},${it.categoryId},${it.date},${it.name},${it.amount}\n")
        }
        writer.flush()
        writer.close()
        return file
    }
}
