# Reporte de implementación: AppGastos

## 1. Estructura base del proyecto
- Se creó un proyecto Android en la carpeta actual, usando Kotlin, Jetpack Compose y Room.
- Archivos principales: `build.gradle`, `settings.gradle`, estructura de carpetas para código fuente y recursos.

## 2. Modelos de datos
- `Category.kt`: Entidad Room para categorías, con id autogenerado y nombre.
- `Expense.kt`: Entidad Room para gastos, con id autogenerado, id de categoría, fecha (timestamp), nombre y cantidad en euros.

## 3. Almacenamiento local (Room)
- DAOs (`CategoryDao`, `ExpenseDao`) para acceso a datos.
- `AppDatabase.kt`: Clase principal de la base de datos, con método singleton para acceso global.
- `Repository.kt`: Abstracción para acceder a los DAOs desde la interfaz.

## 4. Interfaz de usuario (Jetpack Compose)
- `MainActivity.kt`: Entrada principal de la app.
- `MainScreen.kt`: Navegación por pestañas (Categorías, Gastos, Exportar).
- `CategoryScreen.kt`: Formulario para crear categorías y listado de existentes.
- `ExpenseScreen.kt`: Formulario para añadir gastos y listado de existentes.
- `ExportScreen.kt`: Botón para exportar datos y borrarlos.

## 5. Exportación y borrado de datos
- `CsvExporter.kt`: Utilidad para exportar categorías y gastos a un archivo CSV en almacenamiento interno.
- Tras exportar, los gastos se eliminan de la base de datos.

## 6. Documentación y configuración
- `README.md`: Explica funcionalidades, estructura y uso.
- `AndroidManifest.xml`: Configuración de la app y actividad principal.

## Decisiones técnicas
- Se eligió Room por su integración nativa y seguridad para datos locales.
- Jetpack Compose permite una interfaz moderna y reactiva.
- El formato CSV es óptimo para exportar y analizar datos en PC.
- El borrado tras exportar ahorra espacio y cumple el requisito de no duplicar datos.

## Siguientes pasos
- Personalizar estilos visuales y mejorar la experiencia de usuario.
- Añadir validaciones y feedback visual en formularios.
- Implementar DatePicker para la fecha del gasto.
- Mejorar la gestión de archivos exportados (compartir, mover, etc).

---

Para cualquier duda o mejora, puedes consultar el código fuente y el README adjunto.