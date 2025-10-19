# 📱 AppGastos - Documentación de Pantallas

## 🏠 Pantalla Principal (MainActivity)

### Descripción
La pantalla de inicio de la aplicación con logo STONKS personalizado que da la bienvenida al usuario y proporciona acceso directo a todas las funcionalidades principales.

### Funcionalidades
- **Logo personalizado**: Icono STONKS como identidad visual de la app
- **Mensaje de bienvenida**: Saludo personalizado "¡Bienvenido a AppGastos!"
- **Navegación principal**: Cuatro botones principales para acceder a las diferentes secciones
- **Diseño profesional**: Interfaz con paleta de colores corporativa (azul/verde/rojo)

### Elementos de UI
- `ImageView`: Logo STONKS como cabecera
- `TextView`: Título de bienvenida con estilo profesional
- `Button "Gestionar Categorías"`: Navega a CategoriesActivity (estilo azul primario)
- `Button "Añadir Gastos"`: Navega a ExpensesActivity (estilo azul primario)
- `Button "Sincronizar con Servidor"`: Navega a SyncActivity (estilo verde secundario)
- `Button "Exportar Datos"`: Navega a ExportActivity (estilo verde secundario)

---

## 📁 Pantalla de Categorías (CategoriesActivity)

### Descripción
Permite crear, visualizar y eliminar categorías de gastos con interfaz profesional. Las categorías son la base para organizar los gastos posteriores.

### Funcionalidades
- **Crear categorías**: Añadir nuevas categorías con nombre personalizado
- **Listar categorías**: Visualizar todas las categorías existentes en lista scrolleable
- **Eliminar categorías**: Borrar categorías con confirmación por toque simple (solo si no tienen gastos asociados)
- **Validación robusta**: No permite crear categorías con nombres vacíos o duplicados

### Elementos de UI
- `EditText`: Campo para escribir el nombre de la nueva categoría
- `Button "Añadir Categoría"`: Guarda la nueva categoría (estilo azul primario)
- `ListView`: Lista de todas las categorías existentes con scroll automático
- `TextView`: Instrucciones claras "Toca una categoría para eliminarla"
- `Button "Volver al Menú Principal"`: Regresa a MainActivity (estilo secundario)

### Interacciones
- **Tocar una categoría**: Muestra diálogo de confirmación estilizado para eliminarla
- **Protección de datos**: No permite eliminar categorías que tienen gastos asociados
- **Feedback profesional**: Toast messages con colores corporativos para confirmar acciones

---

## 💰 Pantalla de Gastos (ExpensesActivity)

### Descripción
Permite registrar nuevos gastos con interfaz DatePicker integrado, asociándolos a categorías existentes, y gestionar el historial completo con funcionalidad de eliminación intuitiva.

### Funcionalidades
- **Seleccionar categoría**: Spinner desplegable con todas las categorías disponibles
- **Añadir descripción**: Campo libre para describir el gasto
- **DatePicker integrado**: Calendario visual para elegir fecha del gasto (por defecto: hoy)
- **Ingresar cantidad**: Campo numérico validado para el importe en euros
- **Historial completo**: Lista scrolleable de todos los gastos con detalles
- **Eliminación intuitiva**: Un toque sobre cualquier gasto + confirmación para eliminar

### Elementos de UI
- `Spinner`: Selector de categoría con estilo profesional
- `EditText "Descripción"`: Campo para el nombre/descripción del gasto
- `Button "Seleccionar Fecha"`: Abre DatePicker nativo de Android
- `TextView`: Muestra la fecha seleccionada en formato dd/MM/yyyy
- `EditText "Cantidad"`: Campo numérico decimal para el importe
- `Button "Añadir Gasto"`: Guarda el gasto (estilo azul primario)
- `ListView`: Historial de gastos con formato detallado y click listener
- `Button "Volver al Menú Principal"`: Regresa a MainActivity

### Formato de Visualización
Cada gasto se muestra como: `Descripción - €Cantidad (Categoría) - dd/MM/yyyy`

### Interacciones
- **Tocar un gasto**: Muestra diálogo de confirmación para eliminarlo
- **DatePicker**: Interfaz nativa de Android para selección de fecha
- **Validaciones en tiempo real**: Campos obligatorios y formato numérico

---

## 🌐 Pantalla de Sincronización (SyncActivity)

### Descripción
Nueva pantalla para sincronización automática con servidor Django mediante auto-discovery de red y upload de datos via HTTP multipart.

### Funcionalidades
- **Auto-discovery inteligente**: Escaneo paralelo de la red local para encontrar servidor Django
- **Algoritmo optimizado**: Prueba IPs comunes primero (1, 100-105, 254) luego escaneo completo
- **Upload automático**: Genera CSV y lo sube al servidor via multipart HTTP
- **Configuración manual**: Opción para introducir IP específica si auto-discovery falla
- **Feedback en tiempo real**: Indicadores de progreso y estado de conexión

### Elementos de UI
- `TextView`: Título "Sincronización con Servidor Django"
- `Button "Iniciar Auto-Discovery"`: Comienza búsqueda automática (estilo verde)
- `ProgressBar`: Indicador visual del progreso de escaneo
- `TextView "Estado"`: Información detallada del proceso en curso
- `EditText "IP Manual"`: Campo para IP específica si es necesario
- `Button "Conectar IP Manual"`: Prueba conexión con IP introducida
- `Button "Volver al Menú Principal"`: Regresa a MainActivity

### Algoritmo de Auto-Discovery
1. **Detección de red**: Obtiene subnet de la WiFi activa
2. **IPs comunes**: Prueba [1, 100, 101, 102, 103, 104, 105, 254] en paralelo
3. **Escaneo completo**: Si no encuentra, escanea 2-99, 106-253 en chunks de 20
4. **Timeout optimizado**: 800ms por IP para velocidad
5. **Validación endpoint**: Verifica `/api/upload/` en puerto 8000

### Protocolo de Comunicación
- **URL objetivo**: `http://{IP}:8000/api/upload/`
- **Método**: POST multipart/form-data
- **Campo**: `csv_file` con datos completos de categorías y gastos
- **Headers**: User-Agent personalizado para identificación
- **Confirmación**: Diálogo antes de upload con advertencia de borrado

---

## 📤 Pantalla de Exportación (ExportActivity)

### Descripción
Permite exportar todos los datos a un archivo CSV con selector de ubicación personalizado y opción de limpieza posterior opcional.

### Funcionalidades
- **Selector de ubicación**: Permite elegir dónde guardar el archivo CSV
- **Exportación completa**: Genera archivo CSV con categorías y gastos
- **Limpieza opcional**: Pregunta si eliminar gastos tras exportar (no obligatorio)
- **Gestión moderna de archivos**: Usa ActivityResultContracts para crear documentos
- **Feedback detallado**: Confirmación de ubicación y acciones realizadas

### Elementos de UI
- `TextView`: Título de la sección con icono
- `TextView`: Explicación del proceso de exportación mejorado
- `Button "Exportar Datos"`: Abre selector de ubicación (estilo verde)
- `TextView "Estado"`: Muestra el progreso y resultado de la operación
- `Button "Volver al Menú Principal"`: Regresa a MainActivity

### Proceso de Exportación Moderno
1. **Verificación**: Comprueba si hay datos para exportar
2. **Selector de archivo**: ActivityResultContracts.CreateDocument()
3. **Generación CSV**: Crea archivo con formato estructurado
4. **Guardado**: Escribe en la URI seleccionada por el usuario
5. **Opción de limpieza**: Diálogo preguntando si borrar gastos (opcional)
6. **Confirmación**: Muestra ubicación final y resumen de acciones

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
- ✅ **Categorías**: Se conservan siempre para futuros gastos
- ❓ **Gastos**: El usuario decide si eliminarlos o conservarlos
- 📁 **Archivo**: Queda disponible en la ubicación elegida por el usuario
- 🔄 **Flexibilidad**: No hay borrado automático, máximo control al usuario

---

## 🗄️ Base de Datos (Room)

### Entidades
- **Category**: `id (auto), name (String)`
- **Expense**: `id (auto), categoryId (Int), date (Long), name (String), amount (Double)`

### Relaciones
- Un gasto pertenece a una categoría (relación 1:N)
- Una categoría puede tener múltiples gastos
- Integridad referencial: no se pueden eliminar categorías con gastos asociados

### Operaciones Asíncronas
- Todas las operaciones de base de datos se ejecutan en `Dispatchers.IO`
- Uso de Kotlin coroutines para responsividad de UI
- Repository pattern para abstracción de datos

---

## 🎨 Características de Diseño Profesional

### Sistema de Colores Corporativo
- **Azul Primario** (`#1565C0`): Botones principales y elementos de navegación
- **Verde Secundario** (`#66BB6A`): Acciones de sincronización y exportación
- **Rojo de Advertencia** (`#EF5350`): Confirmaciones de eliminación y alertas
- **Grises Neutros**: Textos, fondos y elementos de soporte

### Estilos Unificados
- **ButtonPrimary**: Estilo azul para acciones principales
- **ButtonSecondary**: Estilo verde para acciones secundarias  
- **ButtonWarning**: Estilo rojo para acciones destructivas
- **Tipografía**: Tamaños jerárquicos con fuentes del sistema

### Logo e Identidad Visual
- **Logo STONKS**: Icono personalizado como identidad de la app
- **Consistencia**: Mismo logo en app icon y pantalla principal
- **Profesionalismo**: Diseño corporativo coherente en toda la aplicación

### Experiencia de Usuario (UX)
- **Navegación intuitiva**: Botones claros y descriptivos con iconografía
- **Feedback inmediato**: Toast messages estilizados para todas las acciones
- **Confirmaciones inteligentes**: Diálogos contextuales para acciones críticas
- **Eliminación consistente**: Un toque + confirmación en gastos y categorías
- **Progreso visual**: ProgressBar para operaciones de red

### Responsive Design
- **Layouts flexibles**: LinearLayout con weights apropiados
- **Texto adaptativo**: Tamaños relativos (sp) para accesibilidad
- **Botones touch-friendly**: Tamaños mínimos de 48dp
- **Campos de entrada**: Hints descriptivos y tipos de input apropiados
- **Scroll automático**: ListView con scroll suave en pantallas con muchos datos

---

## 🔗 Integración de Red

### Network Security Config
- **HTTP permitido**: Configuración para desarrollo local
- **Puerto estándar**: Django en puerto 8000
- **Timeouts**: 800ms para auto-discovery, 30s para uploads

### Gestión de Archivos Moderna
- **ActivityResultContracts**: Reemplazo moderno de startActivityForResult
- **Storage Access Framework**: Acceso seguro al almacenamiento del usuario
- **URI handling**: Gestión robusta de archivos en Android 11+

### Protocolos de Comunicación
- **OkHttp**: Cliente HTTP para comunicación con Django
- **Multipart uploads**: Para envío de archivos CSV
- **Error handling**: Manejo graceful de errores de red y timeouts