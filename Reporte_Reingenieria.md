# REPORTE DE REINGENIERÍA DE SOFTWARE CON IA

**Materia:** Gestión de Calidad  
**Tarea:** 14 - Reingeniería con AI  
**Alumno/Equipo:** [Tu Nombre / Tu Equipo]  
**Fecha:** 26 de Abril de 2026  

---

*(Nota: Agrega tu portada oficial del formato de la universidad en la primera hoja antes de exportar a PDF)*

## 1. Introducción
El presente reporte documenta el proceso de reingeniería de un Sistema de Inventario legado. La versión original del software, construida sobre Java Swing y consultas SQL incrustadas de manera cruda, presentaba un alto grado de acoplamiento (Código Espagueti), vulnerabilidades de seguridad y un diseño visual deficiente. 

El objetivo principal de esta reingeniería fue elevar los estándares de calidad del software mediante la separación de preocupaciones, migración a tecnologías modernas (JavaFX y ORMLite), implementación de patrones de diseño rigurosos (MVC y DAO), y la inclusión de pruebas automatizadas (JUnit 5). El resultado es un producto robusto, seguro, visualmente atractivo y fácilmente mantenible.

---

## 2. Arquitectura del Proyecto

Se abandonó la estructura monolítica a favor de una arquitectura basada en **Modelo-Vista-Controlador (MVC)**, apoyada en el **Patrón Data Access Object (DAO)** para abstraer la persistencia.

### Diagrama Arquitectónico (UML)

```mermaid

<img width="975" height="1292" alt="image" src="https://github.com/user-attachments/assets/519cd49e-d84f-4a9d-aa54-87ec5c83d015" />


classDiagram
    %% Capa de Persistencia y DAOs
    class DatabaseHelper {
        -ConnectionSource connectionSource
        +init(String dbUrl)
        +getUsuarioDao() Dao
        +getAlmacenDao() Dao
        +getProductoDao() Dao
    }

    %% Modelos / Entidades
    class Producto {
        -int id
        -String nombre
        -int cantidad
        -double precio
        -Almacen almacen
        +getters()
        +setters()
    }
    class Almacen {
        -int id
        -String nombre
        -String ubicacion
        +getters()
        +setters()
    }

    %% Controladores (Logica de Vista)
    class ProductosController {
        -TableView tablaProductos
        -Usuario usuarioActual
        +guardarProducto()
        +eliminarProducto()
        +limpiarFormulario()
    }
    
    class BaseController {
        <<abstract>>
        #Usuario usuarioActual
        +setUsuario(Usuario)
    }

    ProductosController --|> BaseController : Extiende
    ProductosController ..> DatabaseHelper : Consume DAOs
    ProductosController ..> Producto : Manipula Vista
    DatabaseHelper --> Producto : Administra
    DatabaseHelper --> Almacen : Administra
    Producto "N" *-- "1" Almacen : Relación FK
```



---

## 3. Desglose de Mejoras

### 3.1 Mejoras en la interfaz de usuario y la navegación
*   **Migración a JavaFX:** Se erradicó Java Swing. Las vistas ahora se diseñan mediante lenguajes de marcado (`.fxml`), permitiendo independencia total del diseño sobre el código funcional.
*   **Diseño Único (Light Mode Moderno):** A través de hojas de estilo (`styles.css`), se implementó un diseño limpio con fondo claro, tarjetas blancas con bordes redondeados y ligeras sombras (Glassmorphism sutil). Se utilizaron tipografías modernas y botones con gradientes azules que mejoran radicalmente la experiencia de usuario (UX).
*   **Navegación Dinámica:** En lugar de apilar y ocultar paneles manualmente, se implementó un `BorderPane` dinámico (MainLayout) que inyecta las pantallas de Productos y Almacenes de manera eficiente en su región central sin recargar el menú lateral.

### 3.2 Mejoras en el manejo de datos
*   **Eliminación de JDBC Crudo:** La clase `Database.java` original escribía consultas SQL puras propensas a errores tipográficos. Se incorporó el framework **ORMLite**, el cual mapea automáticamente los objetos a tablas y maneja la creación del esquema y las migraciones internamente.
*   **Prevención de Inyecciones SQL:** Al delegar la escritura a ORMLite, todos los parámetros de entrada son sanitizados (escapados) automáticamente, cerrando la puerta principal a ataques de SQL Injection.

### 3.3 Mejoras en los modelos
*   **De Estructuras a Entidades:** Los modelos antiguos tenían variables `public` sin encapsulamiento. Hoy, los modelos son verdaderos POJOs (Plain Old Java Objects) con atributos `private`, getters/setters, y están decorados con anotaciones precisas (`@DatabaseTable`, `@DatabaseField`) que definen claves foráneas, valores nulos y auto-incrementos.

### 3.4 Mejoras en los controladores
*   **Separación Estricta:** Anteriormente la UI (Botones, Cajas de texto) y la lógica (Consultas SQL, Validaciones) convivían en el mismo archivo. Se implementaron controladores dedicados (`AlmacenesController`, `LoginController`). Ahora, el archivo `FXML` solo define "dónde está el botón" y el Controlador define "qué hace el botón", facilitando que el código sea testeable.

---

## 4. Análisis Comparativo

### 4.1 Comparación de Seguridad
| Aspecto | Versión Anterior (Swing) | Versión Nueva (JavaFX + ORMLite) |
| :--- | :--- | :--- |
| **Persistencia** | `PreparedStatement` manual con alto riesgo de olvidar inyectar parámetros adecuadamente. | ORM automatizado. Sanitización forzosa en cada transacción y escape de caracteres. |
| **Autenticación** | Criptografía mezclada con la lógica de UI. | Clase de utilería independiente (`CryptoUtils`). Mayor acoplamiento de seguridad. |
| **Estado y Datos** | Variables de modelo públicas (riesgo de mutación accidental). | Encapsulación estricta. Getters y setters protegen la integridad de la memoria temporal. |

### 4.2 Comparación de Pruebas
| Aspecto | Versión Anterior (Swing) | Versión Nueva (JavaFX + ORMLite) |
| :--- | :--- | :--- |
| **Cobertura** | Inexistente. Las pruebas se realizaban de manera manual directamente afectando la base de datos de producción (`InventarioV4.db`). | **Pruebas Exhaustivas con JUnit 5**. Automatización de pruebas para DAOs y lógicas de validación. |
| **Aislamiento** | Nulo. Un error de código en desarrollo corrompía la información del cliente. | **Aislamiento Total**. Las pruebas de JUnit levantan una base de datos temporal en RAM (`jdbc:sqlite::memory:`) que desaparece al instante, asegurando que el ambiente de producción jamás se vea afectado. |

---

## 5. Evidencia Visual (Aplicación Ejecutándose)

PANTALLA DE INICIO DE SESION
<img width="834" height="498" alt="image" src="https://github.com/user-attachments/assets/5f63ceb9-88ef-4892-bd5b-892683350bdd" />

Vista de MENU, aquí se puede cerrar sesión o irte a la ventana de Productos o almacenes
 <img width="975" height="647" alt="image" src="https://github.com/user-attachments/assets/768e487f-6cf9-44f8-806f-76b76f353895" />


Vista de administración de Productos con la tabla y formulario activos.
<img width="975" height="635" alt="image" src="https://github.com/user-attachments/assets/071867ce-e854-4e02-9002-e35eadaf6341" />

 
Vista de administración de Almacenes con datos reales cargados.
 <img width="975" height="485" alt="image" src="https://github.com/user-attachments/assets/ec2ce42a-7cf2-4158-b503-3c2d17b05a46" />


Mensaje de alerta por datos rellenados invalidos
 <img width="470" height="725" alt="image" src="https://github.com/user-attachments/assets/b966c69c-e532-4d62-a665-da7c7f1bb8e1" />




---
**Fin del reporte.**
