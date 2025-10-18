# 📱 AppGastos - Documentación de Pantallas

## 🏠 Pantalla Principal (MainActivity)

### Descripción
La pantalla de inicio de la aplicación que da la bienvenida al usuario y proporciona acceso directo a todas las funcionalidades principales.

### Funcionalidades
- **Mensaje de bienvenida**: Saludo personalizado "¡Bienvenido a AppGastos!"
- **Navegación principal**: Tres botones principales para acceder a las diferentes secciones
- **Interfaz limpia**: Diseño centrado y minimalista

### Elementos de UI
- `TextView`: Título de bienvenida
- `Button "Gestionar Categorías"`: Navega a la pantalla de categorías
- `Button "Añadir Gastos"`: Navega a la pantalla de gastos
- `Button "Exportar Datos"`: Navega a la pantalla de exportación

---

## 📁 Pantalla de Categorías (CategoriesActivity)

### Descripción
Permite crear, visualizar y eliminar categorías de gastos. Las categorías son la base para organizar los gastos posteriores.

### Funcionalidades
- **Crear categorías**: Añadir nuevas categorías con nombre personalizado
- **Listar categorías**: Visualizar todas las categorías existentes
- **Eliminar categorías**: Borrar categorías con confirmación (solo si no tienen gastos asociados)
- **Validación**: No permite crear categorías con nombres vacíos

### Elementos de UI
- `EditText`: Campo para escribir el nombre de la nueva categoría
- `Button "Añadir Categoría"`: Guarda la nueva categoría en la base de datos
- `ListView`: Lista de todas las categorías existentes
- `TextView`: Instrucciones para eliminar categorías
- `Button "Volver al Menú Principal"`: Regresa a la pantalla principal

### Interacciones
- **Tocar una categoría**: Muestra diálogo de confirmación para eliminarla
- **Protección de datos**: No permite eliminar categorías que tienen gastos asociados
- **Feedback**: Mensajes Toast para confirmar acciones o mostrar errores

---

## 💰 Pantalla de Gastos (ExpensesActivity)

### Descripción
Permite registrar nuevos gastos asociándolos a categorías existentes, con fecha personalizable y visualización del historial completo.

### Funcionalidades
- **Seleccionar categoría**: Dropdown con todas las categorías disponibles
- **Añadir descripción**: Campo libre para describir el gasto
- **Seleccionar fecha**: DatePicker para elegir la fecha del gasto (por defecto: hoy)
- **Ingresar cantidad**: Campo numérico para el importe en euros
- **Historial completo**: Lista de todos los gastos registrados con detalles

### Elementos de UI
- `Spinner`: Selector de categoría
- `EditText "Descripción"`: Campo para el nombre/descripción del gasto
- `Button "Seleccionar Fecha"`: Abre DatePicker para elegir fecha
- `TextView`: Muestra la fecha seleccionada en formato dd/MM/yyyy
- `EditText "Cantidad"`: Campo numérico para el importe
- `Button "Añadir Gasto"`: Guarda el gasto en la base de datos
- `ListView`: Historial de gastos con formato detallado
- `Button "Volver al Menú Principal"`: Regresa a la pantalla principal

### Formato de Visualización
Cada gasto se muestra como: `Descripción - €Cantidad (Categoría) - Fecha`

### Validaciones
- Campos obligatorios: descripción, cantidad y categoría
- Validación numérica para el campo cantidad
- Fecha por defecto: día actual, personalizable via DatePicker

---

## 📤 Pantalla de Exportación (ExportActivity)

### Descripción
Permite exportar todos los datos a un archivo CSV y gestionar la limpieza selectiva de datos del dispositivo.

### Funcionalidades
- **Exportación completa**: Genera archivo CSV con categorías y gastos
- **Limpieza selectiva**: Elimina solo los gastos, conserva las categorías
- **Ubicación del archivo**: Guarda en almacenamiento interno de la app
- **Feedback detallado**: Información completa sobre el proceso

### Elementos de UI
- `TextView`: Título de la sección
- `TextView`: Explicación del proceso de exportación
- `Button "Exportar y Limpiar Datos"`: Ejecuta el proceso completo
- `TextView "Estado"`: Muestra el progreso y resultado de la operación
- `Button "Volver al Menú Principal"`: Regresa a la pantalla principal

### Proceso de Exportación
1. **Verificación**: Comprueba si hay datos para exportar
2. **Generación CSV**: Crea archivo con formato estructurado
3. **Limpieza selectiva**: Elimina solo los gastos de la base de datos
4. **Confirmación**: Muestra ruta del archivo y resumen de acciones

### Formato del Archivo CSV
```
ID Categoría,Nombre Categoría
1,Comida
2,Transporte

ID Gasto,ID Categoría,Fecha,Nombre,Importe
1,1,1729270800000,Almuerzo,12.50
2,2,1729270800000,Metro,2.40
```

### Comportamiento Post-Exportación
- ✅ **Categorías**: Se conservan para futuros gastos
- ❌ **Gastos**: Se eliminan completamente del dispositivo
- 📁 **Archivo**: Queda disponible en almacenamiento interno

---

## 🗄️ Base de Datos (Room)

### Entidades
- **Category**: `id (auto), name (String)`
- **Expense**: `id (auto), categoryId (Int), date (Long), name (String), amount (Double)`

### Relaciones
- Un gasto pertenece a una categoría (relación 1:N)
- Una categoría puede tener múltiples gastos
- Integridad referencial: no se pueden eliminar categorías con gastos asociados

---

## 🎨 Características de Diseño

### Estilo Visual
- **Tema**: AppCompat Light sin ActionBar
- **Colores**: Paleta estándar de Material Design
- **Tipografía**: Tamaños diferenciados para jerarquía visual
- **Espaciado**: Márgenes consistentes de 16-24dp

### Experiencia de Usuario
- **Navegación intuitiva**: Botones claros y descriptivos
- **Feedback inmediato**: Toast messages para todas las acciones
- **Validaciones en tiempo real**: Prevención de errores de entrada
- **Confirmaciones**: Diálogos para acciones destructivas
- **Consistencia**: Patrones de UI repetidos en todas las pantallas

### Responsive Design
- **Layouts flexibles**: LinearLayout con weights apropiados
- **Texto adaptativo**: Tamaños relativos (sp) para accesibilidad
- **Botones touch-friendly**: Tamaños mínimos recomendados
- **Campos de entrada**: Hints descriptivos y tipos de input apropiados