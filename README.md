# 📦 Sistema de Inventario IA - Reingeniería

![Java](https://img.shields.io/badge/Java-11-blue?style=flat-square&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-17.0.6-orange?style=flat-square)
![ORMLite](https://img.shields.io/badge/ORMLite-6.1-green?style=flat-square)
![SQLite](https://img.shields.io/badge/SQLite-3.45.1-lightgrey?style=flat-square)
![JUnit](https://img.shields.io/badge/JUnit-5-red?style=flat-square&logo=junit5)

Bienvenido al repositorio oficial del **Sistema de Inventario IA**. Este proyecto es el resultado de una reingeniería exhaustiva de un sistema legado basado en Java Swing. Se han aplicado estándares de calidad, principios de diseño moderno y herramientas actualizadas para garantizar su mantenibilidad, escalabilidad y seguridad.

---

## 🏗 Arquitectura del Proyecto

El proyecto ha sido rediseñado completamente bajo el patrón arquitectónico **MVC (Modelo-Vista-Controlador)** en combinación con el patrón **DAO (Data Access Object)**.

La estructura del código está fuertemente modularizada en los siguientes paquetes:

*   **`mx.unison.models`**: Contiene las entidades de la base de datos (`Usuario`, `Almacen`, `Producto`). Estas clases son "POJOs" limpios, anotados estrictamente para su mapeo objeto-relacional.
*   **`mx.unison.dao`**: Implementa el patrón DAO aislando toda la lógica de persistencia. El `DatabaseHelper` administra el ciclo de vida de la base de datos y provee las conexiones seguras.
*   **`mx.unison.controllers`**: Clases dedicadas a manejar la lógica de la vista y la interacción del usuario (`LoginController`, `ProductosController`, etc.), totalmente independientes del diseño visual.
*   **`mx.unison.util`**: Proveedores de servicios transversales como criptografía (`CryptoUtils`).
*   **`mx.unison.views`** *(en resources)*: Archivos `.fxml` (estructura de la UI) y `.css` (hoja de estilos), garantizando una estricta separación entre el diseño y el código fuente.

---

## ✨ Mejoras respecto a la versión anterior

El sistema heredado presentaba un alto acoplamiento y deficiencias visuales. Las mejoras implementadas en esta versión incluyen:

1.  **Reingeniería Visual (UI/UX):** Transición total de Java Swing a **JavaFX**. Se desarrolló una interfaz en "Light Mode" moderna, limpia y profesional. Se incorporaron efectos sutiles de *Glassmorphism*, sombras dinámicas y acentos en tonos celestes (`#00C6FF`) que hacen al sistema intuitivo y atractivo.
2.  **Abstracción de Datos:** Se erradicó el código SQL escrito a mano (hardcoded) en favor del framework **ORMLite**. Esto previene inyecciones SQL de forma nativa y automatiza la creación del esquema relacional.
3.  **Seguridad de Acceso:** La validación de credenciales ahora se hace en capas encapsuladas utilizando cifrado MD5 para proteger las contraseñas, en lugar de enviarlas en texto plano directamente a sentencias SQL inseguras.
4.  **Ejecución Universal:** Se implementó una clase `Launcher` que puentea las restricciones de módulos de JavaFX 11+, permitiendo ejecutar el proyecto desde cualquier IDE con un solo clic.

---

## 📸 Capturas de Pantalla del codigo ejecutandose y sus ventanas

Login- 
usuario:ADMIN 
contasena: admin23
<img width="834" height="498" alt="image" src="https://github.com/user-attachments/assets/98fe0bec-b9bb-4768-b8fa-cac25ba62331" />
<img width="975" height="635" alt="image" src="https://github.com/user-attachments/assets/5b314d0b-5781-4d65-b392-c7d64ddad8a2" />
<img width="975" height="485" alt="image" src="https://github.com/user-attachments/assets/a9904425-fd1f-4409-90d0-2288f30350b3" />
<img width="470" height="725" alt="image" src="https://github.com/user-attachments/assets/9bd9d71b-2bca-468f-a709-3444d73c9f5d" />

---

## 🚀 Guía de Ejecución

Este proyecto utiliza **Maven Wrapper**, por lo que no necesitas tener Maven instalado globalmente en tu equipo.

### Prerrequisitos
*   **Java JDK 11** o superior instalado y configurado en el `PATH` del sistema.

### Levantar la aplicación

Puedes abrir el proyecto en tu IDE (IntelliJ, VSCode, Eclipse) y ejecutar el archivo `src/main/java/mx/unison/Launcher.java`.
<img width="182" height="30" alt="image" src="https://github.com/user-attachments/assets/37480d18-2ed4-41f4-a65b-51494e997df0" />

o alternativamente 
Abre tu terminal en la raíz del proyecto y ejecuta:
```bash
.\mvnw.cmd clean javafx:run
```


### Ejecutar Pruebas (JUnit 5)
Para correr la suite de pruebas exhaustivas (las cuales se ejecutan de manera segura en una base de datos en memoria para no corromper la de producción):
```bash
.\mvnw.cmd test
```

### Generar Documentación (JavaDoc)
Para generar el sitio web con la documentación oficial del código:
```bash
.\mvnw.cmd javadoc:javadoc
```
*(Los archivos HTML se generarán dentro del directorio `target/site/apidocs`)*.
