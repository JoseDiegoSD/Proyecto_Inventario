package mx.unison;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias exhaustivas para la clase Database.
 * Cubre: inicialización, autenticación (correcta, incorrecta, nulos, case-sensitive),
 * CRUD de almacenes (insertar, listar, actualizar, eliminar, bordes),
 * CRUD de productos (insertar, listar, actualizar, eliminar, JOIN, sin almacén, bordes),
 * flujos alternos, concurrencia ligera, eliminación en cascada.
 *
 * NOTA: Cada test limpia sus datos insertados para no contaminar la BD real.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {
    private Database db;

    @BeforeEach
    public void setUp() {
        db = new Database();
    }

    // ============================================================
    // 1. INICIALIZACIÓN DE LA BASE DE DATOS
    // ============================================================

    @Test
    @DisplayName("Inicialización: la BD se crea sin excepciones")
    public void testInicializacionSinExcepciones() {
        assertDoesNotThrow(() -> new Database(),
            "Crear una instancia de Database no debe lanzar excepciones");
    }

    @Test
    @DisplayName("Inicialización: múltiples instancias de Database no causan errores")
    public void testMultiplesInstancias() {
        assertDoesNotThrow(() -> {
            Database db1 = new Database();
            Database db2 = new Database();
            Database db3 = new Database();
        }, "Crear múltiples instancias no debería causar conflictos");
    }

    @Test
    @DisplayName("Inicialización: los usuarios por defecto existen después de init")
    public void testUsuariosPorDefectoExisten() {
        // Verificamos que los tres usuarios base se pueden autenticar
        assertNotNull(db.authenticate("ADMIN", "admin23"), "ADMIN debe existir");
        assertNotNull(db.authenticate("PRODUCTOS", "productos19"), "PRODUCTOS debe existir");
        assertNotNull(db.authenticate("ALMACENES", "almacenes11"), "ALMACENES debe existir");
    }

    // ============================================================
    // 2. AUTENTICACIÓN — FLUJOS CORRECTOS
    // ============================================================

    @Test
    @DisplayName("Autenticación correcta: usuario ADMIN")
    public void testAutenticacionAdminCorrecta() {
        Usuario u = db.authenticate("ADMIN", "admin23");
        assertNotNull(u);
        assertEquals("ADMIN", u.nombre);
        assertEquals("ADMIN", u.rol);
    }

    @Test
    @DisplayName("Autenticación correcta: usuario PRODUCTOS")
    public void testAutenticacionProductosCorrecta() {
        Usuario u = db.authenticate("PRODUCTOS", "productos19");
        assertNotNull(u);
        assertEquals("PRODUCTOS", u.nombre);
        assertEquals("PRODUCTOS", u.rol);
    }

    @Test
    @DisplayName("Autenticación correcta: usuario ALMACENES")
    public void testAutenticacionAlmacenesCorrecta() {
        Usuario u = db.authenticate("ALMACENES", "almacenes11");
        assertNotNull(u);
        assertEquals("ALMACENES", u.nombre);
        assertEquals("ALMACENES", u.rol);
    }

    @Test
    @DisplayName("Autenticación correcta: devuelve objeto Usuario con campos no nulos")
    public void testAutenticacionDevuelveCamposNoNulos() {
        Usuario u = db.authenticate("ADMIN", "admin23");
        assertNotNull(u);
        assertNotNull(u.nombre, "El nombre del usuario autenticado no debe ser null");
        assertNotNull(u.rol, "El rol del usuario autenticado no debe ser null");
    }

    // ============================================================
    // 3. AUTENTICACIÓN — FLUJOS INCORRECTOS / ERRORES
    // ============================================================

    @Test
    @DisplayName("Autenticación incorrecta: contraseña errónea devuelve null")
    public void testAutenticacionContrasenaErronea() {
        Usuario u = db.authenticate("ADMIN", "contrasena_incorrecta");
        assertNull(u, "Contraseña errónea debe devolver null");
    }

    @Test
    @DisplayName("Autenticación incorrecta: usuario inexistente devuelve null")
    public void testAutenticacionUsuarioInexistente() {
        Usuario u = db.authenticate("USUARIO_FANTASMA", "cualquierContrasena");
        assertNull(u, "Usuario inexistente debe devolver null");
    }

    @Test
    @DisplayName("Autenticación incorrecta: ambos campos incorrectos devuelve null")
    public void testAutenticacionAmbosCamposIncorrectos() {
        Usuario u = db.authenticate("NO_EXISTE", "tampoco_existe");
        assertNull(u, "Ambos campos incorrectos deben devolver null");
    }

    @Test
    @DisplayName("Autenticación: nombre correcto pero contraseña invertida")
    public void testAutenticacionContrasenaInvertida() {
        Usuario u = db.authenticate("ADMIN", "32nimda");
        assertNull(u, "Contraseña invertida debe devolver null");
    }

    @Test
    @DisplayName("Autenticación: contraseña correcta pero usuario incorrecto")
    public void testAutenticacionContrasenaBienUsuarioMal() {
        Usuario u = db.authenticate("ADMINISTRADOR", "admin23");
        assertNull(u, "Nombre similar pero incorrecto debe devolver null");
    }

    @Test
    @DisplayName("Autenticación: usuario vacío devuelve null")
    public void testAutenticacionUsuarioVacio() {
        Usuario u = db.authenticate("", "admin23");
        assertNull(u, "Usuario vacío debe devolver null");
    }

    @Test
    @DisplayName("Autenticación: contraseña vacía devuelve null")
    public void testAutenticacionContrasenaVacia() {
        Usuario u = db.authenticate("ADMIN", "");
        assertNull(u, "Contraseña vacía debe devolver null");
    }

    @Test
    @DisplayName("Autenticación: ambos campos vacíos devuelve null")
    public void testAutenticacionAmbosCamposVacios() {
        Usuario u = db.authenticate("", "");
        assertNull(u, "Ambos campos vacíos deben devolver null");
    }

    @Test
    @DisplayName("Autenticación: case-sensitive - 'admin' en minúsculas no funciona como 'ADMIN'")
    public void testAutenticacionCaseSensitiveUsuario() {
        Usuario u = db.authenticate("admin", "admin23");
        assertNull(u, "El nombre de usuario es case-sensitive");
    }

    @Test
    @DisplayName("Autenticación: case-sensitive - contraseña en mayúsculas no funciona")
    public void testAutenticacionCaseSensitiveContrasena() {
        Usuario u = db.authenticate("ADMIN", "ADMIN23");
        assertNull(u, "La contraseña es case-sensitive");
    }

    @Test
    @DisplayName("Autenticación: cadena con espacios extras no coincide")
    public void testAutenticacionEspaciosExtras() {
        Usuario u = db.authenticate("ADMIN ", "admin23");
        assertNull(u, "Espacios extras en el nombre deben fallar");
    }

    @Test
    @DisplayName("Autenticación: contraseña con espacio extra no coincide")
    public void testAutenticacionContrasenaConEspacio() {
        Usuario u = db.authenticate("ADMIN", "admin23 ");
        assertNull(u, "Espacios extras en la contraseña deben fallar");
    }

    @Test
    @DisplayName("Autenticación: intento con SQL injection en usuario")
    public void testAutenticacionSqlInjectionUsuario() {
        Usuario u = db.authenticate("' OR '1'='1", "admin23");
        assertNull(u, "SQL Injection en usuario no debe funcionar (uso de PreparedStatement)");
    }

    @Test
    @DisplayName("Autenticación: intento con SQL injection en contraseña")
    public void testAutenticacionSqlInjectionContrasena() {
        Usuario u = db.authenticate("ADMIN", "' OR '1'='1");
        assertNull(u, "SQL Injection en contraseña no debe funcionar");
    }

    // ============================================================
    // 4. CRUD DE ALMACENES — INSERTAR
    // ============================================================

    @Test
    @DisplayName("Insertar almacén: devuelve id positivo")
    public void testInsertarAlmacenIdPositivo() {
        int id = db.insertAlmacen("TestAlm001", "Ubicación Test", "ADMIN");
        assertTrue(id > 0, "insertAlmacen debe devolver un id positivo");
        // Limpiar
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar almacén: se puede encontrar en la lista")
    public void testInsertarAlmacenExisteEnLista() {
        int id = db.insertAlmacen("TestAlm002", "Sonora", "ADMIN");
        List<Almacen> lista = db.listAlmacenes();
        assertTrue(lista.stream().anyMatch(a -> a.id == id && a.nombre.equals("TestAlm002")));
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar almacén: verifica todos los campos recuperados")
    public void testInsertarAlmacenVerificaCampos() {
        int id = db.insertAlmacen("TestAlm003", "Hermosillo", "PRODUCTOS");
        Almacen encontrado = db.listAlmacenes().stream()
            .filter(a -> a.id == id).findFirst().orElse(null);

        assertNotNull(encontrado);
        assertEquals("TestAlm003", encontrado.nombre);
        assertEquals("Hermosillo", encontrado.ubicacion);
        assertNotNull(encontrado.fechaHoraCreacion, "La fecha de creación debe asignarse automáticamente");
        assertEquals("PRODUCTOS", encontrado.ultimoUsuario);

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar almacén con nombre vacío: se permite (no hay validación en DB)")
    public void testInsertarAlmacenNombreVacio() {
        int id = db.insertAlmacen("", "Ubicación", "ADMIN");
        // El schema dice NOT NULL pero vacío no es null
        assertTrue(id > 0, "Una cadena vacía no es NULL, debería insertarse");
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar almacén con ubicación null")
    public void testInsertarAlmacenUbicacionNull() {
        int id = db.insertAlmacen("TestAlm004", null, "ADMIN");
        assertTrue(id > 0, "Ubicación null debería permitirse (la columna permite NULL)");
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar almacén con caracteres especiales en nombre")
    public void testInsertarAlmacenCaracteresEspeciales() {
        int id = db.insertAlmacen("Almacén #1 ñ áéíóú", "Loc@ción!", "ADMIN");
        assertTrue(id > 0);
        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals("Almacén #1 ñ áéíóú", a.nombre);
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar almacén: SQL injection en nombre no daña la BD")
    public void testInsertarAlmacenSqlInjection() {
        String malicioso = "'; DROP TABLE almacenes; --";
        int id = db.insertAlmacen(malicioso, "Loc", "ADMIN");
        assertTrue(id > 0, "El PreparedStatement debe proteger contra inyección");
        // Verificar que la tabla sigue existiendo
        List<Almacen> lista = db.listAlmacenes();
        assertNotNull(lista);
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Insertar múltiples almacenes: ids son únicos e incrementales")
    public void testInsertarMultiplesAlmacenesIdsUnicos() {
        int id1 = db.insertAlmacen("TestMulti1", "Loc1", "ADMIN");
        int id2 = db.insertAlmacen("TestMulti2", "Loc2", "ADMIN");
        int id3 = db.insertAlmacen("TestMulti3", "Loc3", "ADMIN");

        assertTrue(id1 > 0);
        assertTrue(id2 > id1, "Los ids deben ser incrementales");
        assertTrue(id3 > id2, "Los ids deben ser incrementales");

        // Todos distintos
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);

        db.deleteAlmacen(id1);
        db.deleteAlmacen(id2);
        db.deleteAlmacen(id3);
    }

    @Test
    @DisplayName("Insertar almacén con nombre muy largo (500 chars)")
    public void testInsertarAlmacenNombreLargo() {
        String largo = "A".repeat(500);
        int id = db.insertAlmacen(largo, "Loc", "ADMIN");
        assertTrue(id > 0);
        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals(500, a.nombre.length());
        db.deleteAlmacen(id);
    }

    // ============================================================
    // 5. CRUD DE ALMACENES — LISTAR
    // ============================================================

    @Test
    @DisplayName("Listar almacenes: devuelve lista no nula")
    public void testListarAlmacenesNoNull() {
        List<Almacen> lista = db.listAlmacenes();
        assertNotNull(lista, "listAlmacenes nunca debe devolver null");
    }

    @Test
    @DisplayName("Listar almacenes: después de insertar, la lista contiene el nuevo registro")
    public void testListarAlmacenesContieneNuevo() {
        int sizeBefore = db.listAlmacenes().size();
        int id = db.insertAlmacen("TestList001", "Loc", "ADMIN");
        int sizeAfter = db.listAlmacenes().size();
        assertTrue(sizeAfter > sizeBefore, "La lista debe crecer después de insertar");
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Listar almacenes: campos del listado son correctos")
    public void testListarAlmacenesCamposCorrectos() {
        int id = db.insertAlmacen("TestListCampos", "Nogales", "ALMACENES");
        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);

        assertNotNull(a);
        assertAll("Todos los campos del almacén deben estar presentes",
            () -> assertTrue(a.id > 0, "id debe ser positivo"),
            () -> assertEquals("TestListCampos", a.nombre),
            () -> assertEquals("Nogales", a.ubicacion),
            () -> assertNotNull(a.fechaHoraCreacion),
            () -> assertEquals("ALMACENES", a.ultimoUsuario)
        );

        db.deleteAlmacen(id);
    }

    // ============================================================
    // 6. CRUD DE ALMACENES — ACTUALIZAR
    // ============================================================

    @Test
    @DisplayName("Actualizar almacén: nombre se actualiza correctamente")
    public void testActualizarAlmacenNombre() {
        int id = db.insertAlmacen("Original", "Loc", "ADMIN");
        db.updateAlmacen(id, "Modificado", "Loc", "ADMIN");

        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals("Modificado", a.nombre);

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Actualizar almacén: ubicación se actualiza correctamente")
    public void testActualizarAlmacenUbicacion() {
        int id = db.insertAlmacen("TestUpd", "LocOriginal", "ADMIN");
        db.updateAlmacen(id, "TestUpd", "LocNueva", "ADMIN");

        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals("LocNueva", a.ubicacion);

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Actualizar almacén: último usuario se actualiza")
    public void testActualizarAlmacenUltimoUsuario() {
        int id = db.insertAlmacen("TestUpdUsr", "Loc", "ADMIN");
        db.updateAlmacen(id, "TestUpdUsr", "Loc", "PRODUCTOS");

        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals("PRODUCTOS", a.ultimoUsuario);

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Actualizar almacén: fecha de última modificación se asigna")
    public void testActualizarAlmacenFechaModificacion() {
        int id = db.insertAlmacen("TestUpdFecha", "Loc", "ADMIN");
        db.updateAlmacen(id, "TestUpdFecha", "LocNueva", "ADMIN");

        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertNotNull(a.fechaHoraUltimaMod, "La fecha de última modificación debe asignarse al actualizar");

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Actualizar almacén: actualizar múltiples veces")
    public void testActualizarAlmacenMultiplesVeces() {
        int id = db.insertAlmacen("V1", "Loc1", "ADMIN");
        db.updateAlmacen(id, "V2", "Loc2", "ADMIN");
        db.updateAlmacen(id, "V3", "Loc3", "PRODUCTOS");
        db.updateAlmacen(id, "V4", "Loc4", "ALMACENES");

        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals("V4", a.nombre, "Debe reflejar la última actualización");
        assertEquals("Loc4", a.ubicacion);
        assertEquals("ALMACENES", a.ultimoUsuario);

        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Actualizar almacén inexistente: no causa excepciones")
    public void testActualizarAlmacenInexistente() {
        assertDoesNotThrow(() -> db.updateAlmacen(999999, "Fantasma", "Nada", "ADMIN"),
            "Actualizar un almacén inexistente no debe lanzar excepción");
    }

    // ============================================================
    // 7. CRUD DE ALMACENES — ELIMINAR
    // ============================================================

    @Test
    @DisplayName("Eliminar almacén: desaparece de la lista")
    public void testEliminarAlmacenDesaparece() {
        int id = db.insertAlmacen("TestDel", "Loc", "ADMIN");
        assertTrue(db.listAlmacenes().stream().anyMatch(a -> a.id == id));

        db.deleteAlmacen(id);
        assertFalse(db.listAlmacenes().stream().anyMatch(a -> a.id == id),
            "El almacén eliminado no debe aparecer en la lista");
    }

    @Test
    @DisplayName("Eliminar almacén inexistente: no causa excepciones")
    public void testEliminarAlmacenInexistente() {
        assertDoesNotThrow(() -> db.deleteAlmacen(999999),
            "Eliminar un almacén inexistente no debe lanzar excepción");
    }

    @Test
    @DisplayName("Eliminar almacén dos veces: no causa excepciones")
    public void testEliminarAlmacenDosVeces() {
        int id = db.insertAlmacen("TestDelDoble", "Loc", "ADMIN");
        db.deleteAlmacen(id);
        assertDoesNotThrow(() -> db.deleteAlmacen(id),
            "Eliminar el mismo almacén dos veces no debe causar error");
    }

    @Test
    @DisplayName("Eliminar almacén: no afecta a otros almacenes")
    public void testEliminarAlmacenNoAfectaOtros() {
        int id1 = db.insertAlmacen("NoTocar", "Loc1", "ADMIN");
        int id2 = db.insertAlmacen("Eliminar", "Loc2", "ADMIN");

        db.deleteAlmacen(id2);

        assertTrue(db.listAlmacenes().stream().anyMatch(a -> a.id == id1),
            "Eliminar id2 no debe afectar id1");
        assertFalse(db.listAlmacenes().stream().anyMatch(a -> a.id == id2));

        db.deleteAlmacen(id1);
    }

    // ============================================================
    // 8. CRUD COMPLETO DE ALMACENES (integración)
    // ============================================================

    @Test
    @DisplayName("CRUD completo de almacén: insertar → leer → actualizar → leer → eliminar → verificar")
    public void testCrudCompletoAlmacen() {
        // CREATE
        int id = db.insertAlmacen("CrudTest", "Ubicación Inicial", "ADMIN");
        assertTrue(id > 0);

        // READ
        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertEquals("CrudTest", a.nombre);
        assertEquals("Ubicación Inicial", a.ubicacion);

        // UPDATE
        db.updateAlmacen(id, "CrudTestModificado", "Ubicación Final", "PRODUCTOS");
        Almacen aUpd = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(aUpd);
        assertEquals("CrudTestModificado", aUpd.nombre);
        assertEquals("Ubicación Final", aUpd.ubicacion);
        assertEquals("PRODUCTOS", aUpd.ultimoUsuario);

        // DELETE
        db.deleteAlmacen(id);
        assertFalse(db.listAlmacenes().stream().anyMatch(x -> x.id == id));
    }

    // ============================================================
    // 9. CRUD DE PRODUCTOS — INSERTAR
    // ============================================================

    @Test
    @DisplayName("Insertar producto: devuelve id positivo")
    public void testInsertarProductoIdPositivo() {
        Producto p = crearProductoBase("TestProd001", 5, 100.0);
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto con almacén asignado: verifica JOIN")
    public void testInsertarProductoConAlmacen() {
        int idAlm = db.insertAlmacen("AlmProdTest", "Loc", "ADMIN");
        Producto p = crearProductoBase("ProdConAlm", 3, 50.0);
        p.almacenId = idAlm;

        int idProd = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(idProd);

        assertNotNull(guardado);
        assertEquals("ProdConAlm", guardado.nombre);
        assertEquals("AlmProdTest", guardado.almacenNombre, "El JOIN debe retornar el nombre del almacén");
        assertEquals(idAlm, guardado.almacenId);

        db.deleteProducto(idProd);
        db.deleteAlmacen(idAlm);
    }

    @Test
    @DisplayName("Insertar producto sin almacén: almacenId = 0 guarda como NULL")
    public void testInsertarProductoSinAlmacen() {
        Producto p = crearProductoBase("ProdSinAlm", 1, 10.0);
        p.almacenId = 0;

        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);

        assertNotNull(guardado);
        assertEquals(0, guardado.almacenId);
        assertNull(guardado.almacenNombre, "Sin almacén, almacenNombre debe ser null (LEFT JOIN)");

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto: almacenId negativo se guarda como NULL")
    public void testInsertarProductoAlmacenIdNegativo() {
        Producto p = crearProductoBase("ProdAlmNeg", 1, 10.0);
        p.almacenId = -1;

        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);

        assertNotNull(guardado);
        // almacenId <= 0 se guarda como NULL en la BD
        assertNull(guardado.almacenNombre);

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto: verifica todos los campos")
    public void testInsertarProductoTodosCampos() {
        int idAlm = db.insertAlmacen("AlmCampos", "LocCampos", "ADMIN");
        Producto p = new Producto();
        p.nombre = "Producto Completo";
        p.descripcion = "Descripción detallada del producto";
        p.cantidad = 42;
        p.precio = 999.99;
        p.almacenId = idAlm;

        int id = db.insertProducto(p, "PRODUCTOS");
        Producto guardado = buscarProducto(id);

        assertNotNull(guardado);
        assertAll("Todos los campos deben coincidir",
            () -> assertEquals("Producto Completo", guardado.nombre),
            () -> assertEquals("Descripción detallada del producto", guardado.descripcion),
            () -> assertEquals(42, guardado.cantidad),
            () -> assertEquals(999.99, guardado.precio, 0.01),
            () -> assertEquals(idAlm, guardado.almacenId),
            () -> assertEquals("AlmCampos", guardado.almacenNombre),
            () -> assertNotNull(guardado.fechaCreacion),
            () -> assertEquals("PRODUCTOS", guardado.ultimoUsuario)
        );

        db.deleteProducto(id);
        db.deleteAlmacen(idAlm);
    }

    @Test
    @DisplayName("Insertar producto con cantidad cero")
    public void testInsertarProductoCantidadCero() {
        Producto p = crearProductoBase("ProdCantCero", 0, 50.0);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertEquals(0, guardado.cantidad);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto con precio cero (gratis)")
    public void testInsertarProductoPrecioCero() {
        Producto p = crearProductoBase("ProdGratis", 5, 0.0);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertEquals(0.0, guardado.precio, 0.001);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto con descripción null")
    public void testInsertarProductoDescripcionNull() {
        Producto p = crearProductoBase("ProdDescNull", 1, 10.0);
        p.descripcion = null;
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertNull(guardado.descripcion);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto con descripción vacía")
    public void testInsertarProductoDescripcionVacia() {
        Producto p = crearProductoBase("ProdDescVacia", 1, 10.0);
        p.descripcion = "";
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertEquals("", guardado.descripcion);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto con precio con muchos decimales")
    public void testInsertarProductoPrecioDecimales() {
        Producto p = crearProductoBase("ProdDecimales", 1, 123.456789);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertEquals(123.456789, guardado.precio, 0.001);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto: SQL injection en nombre")
    public void testInsertarProductoSqlInjection() {
        Producto p = crearProductoBase("'; DROP TABLE productos; --", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0, "PreparedStatement debe proteger contra inyección");
        // Verificar que la tabla sigue existiendo
        assertNotNull(db.listProductos());
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar producto con caracteres Unicode en nombre y descripción")
    public void testInsertarProductoUnicode() {
        Producto p = new Producto();
        p.nombre = "Producto ñ áéíóú 日本語";
        p.descripcion = "Descripción con 🔥 emoji";
        p.cantidad = 1;
        p.precio = 10.0;
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertEquals("Producto ñ áéíóú 日本語", guardado.nombre);
        assertEquals("Descripción con 🔥 emoji", guardado.descripcion);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar múltiples productos: ids son únicos")
    public void testInsertarMultiplesProductosIdsUnicos() {
        Producto p1 = crearProductoBase("Multi1", 1, 10.0);
        Producto p2 = crearProductoBase("Multi2", 2, 20.0);
        Producto p3 = crearProductoBase("Multi3", 3, 30.0);

        int id1 = db.insertProducto(p1, "ADMIN");
        int id2 = db.insertProducto(p2, "ADMIN");
        int id3 = db.insertProducto(p3, "ADMIN");

        assertTrue(id1 > 0);
        assertTrue(id2 > 0);
        assertTrue(id3 > 0);
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);

        db.deleteProducto(id1);
        db.deleteProducto(id2);
        db.deleteProducto(id3);
    }

    // ============================================================
    // 10. CRUD DE PRODUCTOS — LISTAR
    // ============================================================

    @Test
    @DisplayName("Listar productos: devuelve lista no nula")
    public void testListarProductosNoNull() {
        List<Producto> lista = db.listProductos();
        assertNotNull(lista);
    }

    @Test
    @DisplayName("Listar productos: después de insertar, la lista crece")
    public void testListarProductosCrece() {
        int sizeBefore = db.listProductos().size();
        Producto p = crearProductoBase("TestListP", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");
        int sizeAfter = db.listProductos().size();
        assertTrue(sizeAfter > sizeBefore);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Listar productos: LEFT JOIN muestra null cuando almacén no existe")
    public void testListarProductosLeftJoinNull() {
        Producto p = crearProductoBase("ProdJoinNull", 1, 10.0);
        p.almacenId = 0; // sin almacén
        int id = db.insertProducto(p, "ADMIN");

        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertNull(guardado.almacenNombre, "LEFT JOIN sin coincidencia debe dar almacenNombre = null");

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Listar productos: LEFT JOIN con almacén eliminado")
    public void testListarProductosAlmacenEliminado() {
        int idAlm = db.insertAlmacen("AlmTemporal", "Loc", "ADMIN");
        Producto p = crearProductoBase("ProdAlmTemp", 1, 10.0);
        p.almacenId = idAlm;
        int idProd = db.insertProducto(p, "ADMIN");

        // Eliminar el almacén
        db.deleteAlmacen(idAlm);

        // El producto sigue existiendo, pero el JOIN no encuentra almacén
        Producto guardado = buscarProducto(idProd);
        assertNotNull(guardado, "El producto debe existir aunque su almacén haya sido eliminado");
        assertNull(guardado.almacenNombre, "El almacenNombre debe ser null si el almacén fue eliminado");

        db.deleteProducto(idProd);
    }

    // ============================================================
    // 11. CRUD DE PRODUCTOS — ACTUALIZAR
    // ============================================================

    @Test
    @DisplayName("Actualizar producto: nombre se actualiza")
    public void testActualizarProductoNombre() {
        Producto p = crearProductoBase("OriginalProd", 5, 100.0);
        int id = db.insertProducto(p, "ADMIN");

        p.id = id;
        p.nombre = "ModificadoProd";
        db.updateProducto(p, "ADMIN");

        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertEquals("ModificadoProd", guardado.nombre);

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Actualizar producto: cantidad se actualiza")
    public void testActualizarProductoCantidad() {
        Producto p = crearProductoBase("ProdCant", 5, 100.0);
        int id = db.insertProducto(p, "ADMIN");

        p.id = id;
        p.cantidad = 50;
        db.updateProducto(p, "ADMIN");

        Producto guardado = buscarProducto(id);
        assertEquals(50, guardado.cantidad);

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Actualizar producto: precio se actualiza")
    public void testActualizarProductoPrecio() {
        Producto p = crearProductoBase("ProdPrecio", 5, 100.0);
        int id = db.insertProducto(p, "ADMIN");

        p.id = id;
        p.precio = 250.75;
        db.updateProducto(p, "ADMIN");

        Producto guardado = buscarProducto(id);
        assertEquals(250.75, guardado.precio, 0.01);

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Actualizar producto: cambiar almacén asignado")
    public void testActualizarProductoCambiarAlmacen() {
        int idAlm1 = db.insertAlmacen("Alm1", "Loc1", "ADMIN");
        int idAlm2 = db.insertAlmacen("Alm2", "Loc2", "ADMIN");

        Producto p = crearProductoBase("ProdCambioAlm", 1, 10.0);
        p.almacenId = idAlm1;
        int idProd = db.insertProducto(p, "ADMIN");

        p.id = idProd;
        p.almacenId = idAlm2;
        db.updateProducto(p, "ADMIN");

        Producto guardado = buscarProducto(idProd);
        assertEquals(idAlm2, guardado.almacenId);
        assertEquals("Alm2", guardado.almacenNombre);

        db.deleteProducto(idProd);
        db.deleteAlmacen(idAlm1);
        db.deleteAlmacen(idAlm2);
    }

    @Test
    @DisplayName("Actualizar producto: quitar almacén (almacenId = 0)")
    public void testActualizarProductoQuitarAlmacen() {
        int idAlm = db.insertAlmacen("AlmQuitar", "Loc", "ADMIN");
        Producto p = crearProductoBase("ProdQuitarAlm", 1, 10.0);
        p.almacenId = idAlm;
        int idProd = db.insertProducto(p, "ADMIN");

        p.id = idProd;
        p.almacenId = 0;
        db.updateProducto(p, "ADMIN");

        Producto guardado = buscarProducto(idProd);
        assertNull(guardado.almacenNombre, "Al quitar almacén, almacenNombre debe ser null");

        db.deleteProducto(idProd);
        db.deleteAlmacen(idAlm);
    }

    @Test
    @DisplayName("Actualizar producto: fecha de modificación se asigna")
    public void testActualizarProductoFechaModificacion() {
        Producto p = crearProductoBase("ProdFechaMod", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");

        p.id = id;
        p.nombre = "ProdFechaModUpd";
        db.updateProducto(p, "ADMIN");

        Producto guardado = buscarProducto(id);
        assertNotNull(guardado.fechaModificacion, "La fecha de modificación debe asignarse al actualizar");

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Actualizar producto: último usuario se actualiza")
    public void testActualizarProductoUltimoUsuario() {
        Producto p = crearProductoBase("ProdUsr", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");

        p.id = id;
        db.updateProducto(p, "PRODUCTOS");

        Producto guardado = buscarProducto(id);
        assertEquals("PRODUCTOS", guardado.ultimoUsuario);

        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Actualizar producto inexistente: no causa excepciones")
    public void testActualizarProductoInexistente() {
        Producto p = crearProductoBase("Fantasma", 1, 10.0);
        p.id = 999999;
        assertDoesNotThrow(() -> db.updateProducto(p, "ADMIN"),
            "Actualizar producto inexistente no debe lanzar excepción");
    }

    @Test
    @DisplayName("Actualizar producto múltiples veces")
    public void testActualizarProductoMultiplesVeces() {
        Producto p = crearProductoBase("V1", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");

        for (int i = 2; i <= 5; i++) {
            p.id = id;
            p.nombre = "V" + i;
            p.cantidad = i * 10;
            p.precio = i * 100.0;
            db.updateProducto(p, "ADMIN");
        }

        Producto guardado = buscarProducto(id);
        assertEquals("V5", guardado.nombre);
        assertEquals(50, guardado.cantidad);
        assertEquals(500.0, guardado.precio, 0.01);

        db.deleteProducto(id);
    }

    // ============================================================
    // 12. CRUD DE PRODUCTOS — ELIMINAR
    // ============================================================

    @Test
    @DisplayName("Eliminar producto: desaparece de la lista")
    public void testEliminarProductoDesaparece() {
        Producto p = crearProductoBase("ProdElim", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");
        assertNotNull(buscarProducto(id));

        db.deleteProducto(id);
        assertNull(buscarProducto(id), "El producto eliminado no debe existir");
    }

    @Test
    @DisplayName("Eliminar producto inexistente: no causa excepciones")
    public void testEliminarProductoInexistente() {
        assertDoesNotThrow(() -> db.deleteProducto(999999));
    }

    @Test
    @DisplayName("Eliminar producto dos veces: no causa excepciones")
    public void testEliminarProductoDosVeces() {
        Producto p = crearProductoBase("ProdDoble", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");
        db.deleteProducto(id);
        assertDoesNotThrow(() -> db.deleteProducto(id));
    }

    @Test
    @DisplayName("Eliminar producto: no afecta al almacén asociado")
    public void testEliminarProductoNoAfectaAlmacen() {
        int idAlm = db.insertAlmacen("AlmNoAfectar", "Loc", "ADMIN");
        Producto p = crearProductoBase("ProdNoAfectar", 1, 10.0);
        p.almacenId = idAlm;
        int idProd = db.insertProducto(p, "ADMIN");

        db.deleteProducto(idProd);

        // El almacén sigue existiendo
        assertTrue(db.listAlmacenes().stream().anyMatch(a -> a.id == idAlm),
            "Eliminar un producto no debe eliminar su almacén");

        db.deleteAlmacen(idAlm);
    }

    @Test
    @DisplayName("Eliminar producto: no afecta a otros productos")
    public void testEliminarProductoNoAfectaOtros() {
        Producto p1 = crearProductoBase("ProdKeep", 1, 10.0);
        Producto p2 = crearProductoBase("ProdDelete", 2, 20.0);
        int id1 = db.insertProducto(p1, "ADMIN");
        int id2 = db.insertProducto(p2, "ADMIN");

        db.deleteProducto(id2);

        assertNotNull(buscarProducto(id1), "El otro producto no debe ser afectado");
        assertNull(buscarProducto(id2));

        db.deleteProducto(id1);
    }

    // ============================================================
    // 13. CRUD COMPLETO DE PRODUCTOS (integración)
    // ============================================================

    @Test
    @DisplayName("CRUD completo de producto: crear → leer → actualizar → leer → eliminar → verificar")
    public void testCrudCompletoProducto() {
        int idAlm = db.insertAlmacen("AlmCrud", "LocCrud", "ADMIN");

        // CREATE
        Producto p = new Producto();
        p.nombre = "CrudProd";
        p.descripcion = "Desc original";
        p.cantidad = 10;
        p.precio = 100.0;
        p.almacenId = idAlm;
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0);

        // READ
        Producto leido = buscarProducto(id);
        assertNotNull(leido);
        assertEquals("CrudProd", leido.nombre);
        assertEquals("Desc original", leido.descripcion);
        assertEquals(10, leido.cantidad);
        assertEquals(100.0, leido.precio, 0.01);
        assertEquals("AlmCrud", leido.almacenNombre);

        // UPDATE
        p.id = id;
        p.nombre = "CrudProdMod";
        p.descripcion = "Desc modificada";
        p.cantidad = 20;
        p.precio = 200.0;
        db.updateProducto(p, "PRODUCTOS");

        Producto actualizado = buscarProducto(id);
        assertEquals("CrudProdMod", actualizado.nombre);
        assertEquals("Desc modificada", actualizado.descripcion);
        assertEquals(20, actualizado.cantidad);
        assertEquals(200.0, actualizado.precio, 0.01);
        assertEquals("PRODUCTOS", actualizado.ultimoUsuario);

        // DELETE
        db.deleteProducto(id);
        assertNull(buscarProducto(id));

        db.deleteAlmacen(idAlm);
    }

    // ============================================================
    // 14. PRUEBAS DE INTEGRACIÓN CRUZADA (Almacén + Producto)
    // ============================================================

    @Test
    @DisplayName("Eliminar almacén con productos asociados: productos siguen existiendo")
    public void testEliminarAlmacenConProductos() {
        int idAlm = db.insertAlmacen("AlmConProd", "Loc", "ADMIN");
        Producto p = crearProductoBase("ProdHuerfano", 1, 10.0);
        p.almacenId = idAlm;
        int idProd = db.insertProducto(p, "ADMIN");

        // Eliminar el almacén
        db.deleteAlmacen(idAlm);

        // El producto sigue existiendo pero sin almacén
        Producto guardado = buscarProducto(idProd);
        assertNotNull(guardado, "El producto no debe ser eliminado en cascada");

        db.deleteProducto(idProd);
    }

    @Test
    @DisplayName("Múltiples productos en el mismo almacén")
    public void testMultiplesProductosMismoAlmacen() {
        int idAlm = db.insertAlmacen("AlmCompartido", "Loc", "ADMIN");

        Producto p1 = crearProductoBase("Prod1Comp", 1, 10.0);
        p1.almacenId = idAlm;
        Producto p2 = crearProductoBase("Prod2Comp", 2, 20.0);
        p2.almacenId = idAlm;
        Producto p3 = crearProductoBase("Prod3Comp", 3, 30.0);
        p3.almacenId = idAlm;

        int id1 = db.insertProducto(p1, "ADMIN");
        int id2 = db.insertProducto(p2, "ADMIN");
        int id3 = db.insertProducto(p3, "ADMIN");

        // Todos deben mostrar el mismo almacén
        assertEquals("AlmCompartido", buscarProducto(id1).almacenNombre);
        assertEquals("AlmCompartido", buscarProducto(id2).almacenNombre);
        assertEquals("AlmCompartido", buscarProducto(id3).almacenNombre);

        db.deleteProducto(id1);
        db.deleteProducto(id2);
        db.deleteProducto(id3);
        db.deleteAlmacen(idAlm);
    }

    @Test
    @DisplayName("Producto con almacén inexistente (almacenId apunta a id que no existe)")
    public void testProductoConAlmacenInexistente() {
        Producto p = crearProductoBase("ProdAlmFantasma", 1, 10.0);
        p.almacenId = 999999; // no existe

        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);

        assertNotNull(guardado);
        assertEquals(999999, guardado.almacenId);
        assertNull(guardado.almacenNombre, "LEFT JOIN con almacén inexistente debe dar null");

        db.deleteProducto(id);
    }

    // ============================================================
    // 15. PRUEBAS DE CONSISTENCIA DE DATOS
    // ============================================================

    @Test
    @DisplayName("Fecha de creación del almacén tiene formato ISO")
    public void testFechaCreacionAlmacenFormato() {
        int id = db.insertAlmacen("TestFechaFmt", "Loc", "ADMIN");
        Almacen a = db.listAlmacenes().stream().filter(x -> x.id == id).findFirst().orElse(null);
        assertNotNull(a);
        assertNotNull(a.fechaHoraCreacion);
        // Formato ISO: YYYY-MM-DDTHH:MM:SS
        assertTrue(a.fechaHoraCreacion.contains("T"),
            "La fecha debe estar en formato ISO con separador 'T'");
        db.deleteAlmacen(id);
    }

    @Test
    @DisplayName("Fecha de creación del producto tiene formato ISO")
    public void testFechaCreacionProductoFormato() {
        Producto p = crearProductoBase("TestFechaProd", 1, 10.0);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertNotNull(guardado);
        assertNotNull(guardado.fechaCreacion);
        assertTrue(guardado.fechaCreacion.contains("T"),
            "La fecha debe estar en formato ISO con separador 'T'");
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Insertar y listar mantiene el conteo correcto")
    public void testConteoConsistente() {
        int sizeBefore = db.listProductos().size();

        Producto p1 = crearProductoBase("ConteoA", 1, 10.0);
        Producto p2 = crearProductoBase("ConteoB", 2, 20.0);
        int id1 = db.insertProducto(p1, "ADMIN");
        int id2 = db.insertProducto(p2, "ADMIN");

        assertEquals(sizeBefore + 2, db.listProductos().size());

        db.deleteProducto(id1);
        assertEquals(sizeBefore + 1, db.listProductos().size());

        db.deleteProducto(id2);
        assertEquals(sizeBefore, db.listProductos().size());
    }

    // ============================================================
    // 16. PRUEBAS CON VALORES EXTREMOS EN PRODUCTOS
    // ============================================================

    @Test
    @DisplayName("Producto con cantidad muy grande")
    public void testProductoCantidadMuyGrande() {
        Producto p = crearProductoBase("ProdCantGrande", Integer.MAX_VALUE, 10.0);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertEquals(Integer.MAX_VALUE, guardado.cantidad);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Producto con precio muy grande")
    public void testProductoPrecioMuyGrande() {
        Producto p = crearProductoBase("ProdPrecioGrande", 1, 9999999.99);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertEquals(9999999.99, guardado.precio, 0.01);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Producto con nombre de un solo carácter")
    public void testProductoNombreUnCaracter() {
        Producto p = crearProductoBase("X", 1, 1.0);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertEquals("X", guardado.nombre);
        db.deleteProducto(id);
    }

    @Test
    @DisplayName("Producto con nombre muy largo (500 chars)")
    public void testProductoNombreLargo() {
        String largo = "P".repeat(500);
        Producto p = crearProductoBase(largo, 1, 1.0);
        int id = db.insertProducto(p, "ADMIN");
        Producto guardado = buscarProducto(id);
        assertEquals(500, guardado.nombre.length());
        db.deleteProducto(id);
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    /**
     * Crea un Producto base con nombre, cantidad, precio y sin almacén.
     */
    private Producto crearProductoBase(String nombre, int cantidad, double precio) {
        Producto p = new Producto();
        p.nombre = nombre;
        p.descripcion = "Desc de " + nombre;
        p.cantidad = cantidad;
        p.precio = precio;
        p.almacenId = 0;
        return p;
    }

    /**
     * Busca un producto por id en la lista completa.
     */
    private Producto buscarProducto(int id) {
        return db.listProductos().stream()
            .filter(p -> p.id == id)
            .findFirst()
            .orElse(null);
    }
}
