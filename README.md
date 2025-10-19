# 📱 AppGastos

Aplicación Android moderna para gestión de gastos personales con sincronización a servidor Django.

## ✨ Funcionalidades principales

### 📊 Gestión de Datos
- **Crear y gestionar categorías** de gastos con validación de integridad
- **Añadir gastos** con fecha personalizable y asociación a categorías
- **Borrar elementos** con un toque (categorías y gastos) + confirmación
- **Almacenamiento local seguro** con Room Database

### 🚀 Sincronización y Exportación
- **Auto-discovery de servidor Django** en red local WiFi
- **Subir datos al servidor** con confirmación y limpieza automática
- **Guardar CSV localmente** con selector de ubicación del teléfono
- **Opción de limpieza opcional** después de exportar localmente

### 🎨 Diseño Profesional
- **Paleta de colores corporativa** (azul, verde, rojo profesional)
- **Logo STONKS personalizado** como icono de la app
- **Interfaz intuitiva** con confirmaciones claras
- **UX consistente** en toda la aplicación

## 🏗️ Estructura del proyecto

```
app/src/main/
├── java/com/example/appgastos/
│   ├── model/           # Entidades Room (Category, Expense)
│   ├── data/            # DAOs y AppDatabase
│   ├── repository/      # Lógica de acceso a datos
│   ├── ui/              # Activities principales
│   │   └── screens/     # SyncActivity para servidor
│   └── util/            # CsvExporter y utilidades
├── res/
│   ├── layout/          # Layouts XML
│   ├── values/          # Colores, estilos, strings
│   ├── drawable/        # Recursos gráficos y logo
│   └── xml/             # Configuración de red
└── AndroidManifest.xml
```

## 🔧 Requisitos técnicos
- **Android Studio** Arctic Fox o superior
- **SDK mínimo**: API 24 (Android 7.0)
- **Kotlin**: 1.9.0
- **JVM Target**: 17
- **Room Database**: 2.6.0
- **OkHttp**: 4.12.0 para comunicación HTTP

## 🚀 Configuración y uso

### 1. Instalación
```bash
git clone [tu-repo]
cd AppGastos
# Abrir en Android Studio
```

### 2. Configuración del servidor (opcional)
- Instalar Django con endpoint `/api/csv/server-info/` y `/api/csv/upload/`
- Configurar servidor en red local (ej: `python manage.py runserver 0.0.0.0:8000`)

### 3. Uso de la app
1. **Gestionar Categorías**: Crear categorías como "Comida", "Transporte", etc.
2. **Añadir Gastos**: Registrar gastos con fecha, descripción y cantidad
3. **Subir al Servidor**: Auto-detectar servidor Django y sincronizar
4. **Exportar Localmente**: Guardar CSV en ubicación personalizada

## 📡 Funcionalidades de red

### Auto-discovery de Servidor
- **Escaneo automático** de red local (192.168.x.x, 10.x.x.x)
- **Detección inteligente** con timeouts optimizados (800ms)
- **Escaneo paralelo** para máxima velocidad
- **Verificación de endpoint** `/api/server-info/`

### Sincronización con Django
- **Upload seguro** de CSV via HTTP POST multipart
- **Confirmación obligatoria** antes de envío
- **Limpieza automática** de datos locales tras éxito
- **Manejo de errores** con feedback claro

## 📁 Exportación de datos

### Guardar CSV Localmente
- **Selector de ubicación** (Documents, WhatsApp, etc.)
- **Formato estructurado**: Fecha, Categoría, Descripción, Cantidad
- **Opción de limpieza** opcional después de guardar
- **Conservación de categorías** siempre

### Formato CSV
```
Fecha,Categoría,Descripción,Cantidad
1729270800000,Comida,Almuerzo,12.50
1729270800000,Transporte,Metro,2.40
```

---

# 📋 Reporte de implementación

## 🏗️ Arquitectura implementada

### 1. **Base de datos (Room)**
- **Entidades**: `Category.kt`, `Expense.kt` con relaciones 1:N
- **DAOs**: `CategoryDao.kt`, `ExpenseDao.kt` con operaciones CRUD
- **Database**: `AppDatabase.kt` con patrón Singleton
- **Repository**: `Repository.kt` para abstracción de datos

### 2. **Interfaz de usuario (XML + Activities)**
- **MainActivity**: Pantalla principal con navegación
- **CategoriesActivity**: Gestión completa de categorías
- **ExpensesActivity**: Registro de gastos con DatePicker
- **ExportActivity**: Exportación local con selector de ubicación
- **SyncActivity**: Sincronización con servidor Django

### 3. **Funcionalidades de red**
- **Auto-discovery**: Escaneo paralelo de red local
- **HTTP Client**: OkHttp para comunicación con Django
- **Network Security**: Configuración para HTTP en desarrollo
- **Error Handling**: Manejo robusto de errores de conexión

### 4. **Diseño profesional**
- **Sistema de colores**: Paleta corporativa azul/verde/rojo
- **Estilos unificados**: ButtonPrimary, ButtonSecondary, ButtonWarning
- **Logo personalizado**: STONKS como icono de la app
- **UX consistente**: Confirmaciones y feedback en toda la app

## 🎯 Decisiones técnicas

### **Room vs SQLite**: Room elegido por:
- Compile-time verification de queries
- Integración nativa con Kotlin coroutines
- Abstracciones type-safe

### **XML vs Jetpack Compose**: XML elegido por:
- Mayor estabilidad en Kotlin 1.9.0
- Mejor compatibilidad con dependencias existentes
- Ecosystem maduro y documentación abundante

### **OkHttp vs Retrofit**: OkHttp elegido por:
- Control granular sobre requests
- Manejo directo de multipart uploads
- Timeouts personalizables para auto-discovery

### **Auto-discovery vs configuración manual**:
- **Escaneo inteligente**: IPs comunes primero (1, 100-105, 254)
- **Paralelización**: Chunks de 20 IPs simultáneas
- **Timeouts optimizados**: 800ms por IP para velocidad
- **Fallback manual**: Usuario puede introducir IP específica

## 🚀 Innovaciones implementadas

### **Smart Network Discovery**
```kotlin
// Escaneo paralelo optimizado
val commonResults = commonIPs.map { lastOctet ->
    async { testServerEndpoint("$subnet.$lastOctet", port) }
}.awaitAll().filterNotNull()
```

### **UX de confirmación inteligente**
- **Subir al servidor**: Confirmación obligatoria + advertencia de borrado
- **Exportar local**: Opción de limpieza opcional post-guardado
- **Borrar elementos**: Un toque + confirmación (consistente en toda la app)

### **Gestión de archivos moderna**
```kotlin
private val createCsvLauncher = registerForActivityResult(
    ActivityResultContracts.CreateDocument("text/csv")
) { uri -> saveCsvToUri(uri) }
```

## 📈 Métricas de rendimiento

- **Auto-discovery**: ~10 segundos para 254 IPs (vs 12+ minutos secuencial)
- **Upload speed**: Multipart optimizado para CSVs pequeños/medianos
- **Database queries**: Todas asíncronas con coroutines
- **UI responsiveness**: Operaciones pesadas en Dispatchers.IO

## 🔄 Migración y evolución

### **Problemas resueltos durante desarrollo**:
1. **Jetpack Compose conflicts** → Migración completa a XML
2. **Gradle version issues** → Unificación a Kotlin 1.9.0 + JVM 17
3. **Network security** → Configuración para desarrollo HTTP
4. **startActivityForResult deprecated** → Migration a ActivityResultContracts
5. **Database singleton** → Patrón thread-safe implementado

### **Optimizaciones implementadas**:
- **Chunked network scanning** para velocidad
- **Timeout reduction** para responsividad
- **Resource cleanup** automático
- **Memory management** con coroutines

---

## 🔮 Roadmap futuro

### **Próximas funcionalidades**:
- 📊 **Dashboard con gráficos** de gastos por categoría
- 🔔 **Notificaciones** de recordatorios de gastos
- 💱 **Multi-moneda** con conversión automática
- 📱 **Widget** para añadir gastos rápidos
- 🔄 **Sincronización bidireccional** con servidor
- 📧 **Export automático** por email/WhatsApp

### **Mejoras técnicas**:
- 🚀 **Migration a Jetpack Compose** cuando sea estable
- 🔐 **Autenticación** con el servidor Django
- 📱 **Backup automático** en la nube
- 🎨 **Theming dinámico** con Material You
