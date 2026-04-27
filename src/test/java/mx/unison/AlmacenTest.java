package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias exhaustivas para la clase Almacen.
 * Cubre: valores por defecto, campos válidos, valores nulos, 
 * valores vacíos, caracteres especiales, valores de borde.
 */
public class AlmacenTest {

    // ============================================================
    // 1. VALORES POR DEFECTO (al crear instancia nueva)
    // ============================================================

    @Test
    @DisplayName("Valores por defecto: id debe ser 0 al inicializar")
    public void testValorPorDefectoId() {
        Almacen a = new Almacen();
        assertEquals(0, a.id, "El id por defecto de un int debe ser 0");
    }

    @Test
    @DisplayName("Valores por defecto: nombre debe ser null al inicializar")
    public void testValorPorDefectoNombre() {
        Almacen a = new Almacen();
        assertNull(a.nombre, "El nombre por defecto debe ser null");
    }

    @Test
    @DisplayName("Valores por defecto: ubicacion debe ser null al inicializar")
    public void testValorPorDefectoUbicacion() {
        Almacen a = new Almacen();
        assertNull(a.ubicacion, "La ubicación por defecto debe ser null");
    }

    @Test
    @DisplayName("Valores por defecto: fechaHoraCreacion debe ser null")
    public void testValorPorDefectoFechaCreacion() {
        Almacen a = new Almacen();
        assertNull(a.fechaHoraCreacion, "La fecha de creación por defecto debe ser null");
    }

    @Test
    @DisplayName("Valores por defecto: fechaHoraUltimaMod debe ser null")
    public void testValorPorDefectoFechaUltimaMod() {
        Almacen a = new Almacen();
        assertNull(a.fechaHoraUltimaMod, "La fecha de última modificación por defecto debe ser null");
    }

    @Test
    @DisplayName("Valores por defecto: ultimoUsuario debe ser null")
    public void testValorPorDefectoUltimoUsuario() {
        Almacen a = new Almacen();
        assertNull(a.ultimoUsuario, "El último usuario por defecto debe ser null");
    }

    // ============================================================
    // 2. ASIGNACIÓN Y LECTURA DE CAMPOS VÁLIDOS
    // ============================================================

    @Test
    @DisplayName("Asignación correcta de id positivo")
    public void testAsignacionIdPositivo() {
        Almacen a = new Almacen();
        a.id = 50;
        assertEquals(50, a.id);
    }

    @Test
    @DisplayName("Asignación correcta de nombre típico")
    public void testAsignacionNombreTipico() {
        Almacen a = new Almacen();
        a.nombre = "Bodega Central";
        assertEquals("Bodega Central", a.nombre);
    }

    @Test
    @DisplayName("Asignación correcta de ubicación típica")
    public void testAsignacionUbicacionTipica() {
        Almacen a = new Almacen();
        a.ubicacion = "Ciudad de México";
        assertEquals("Ciudad de México", a.ubicacion);
    }

    @Test
    @DisplayName("Asignación correcta de fecha de creación")
    public void testAsignacionFechaCreacion() {
        Almacen a = new Almacen();
        a.fechaHoraCreacion = "2026-03-20T10:30:00";
        assertEquals("2026-03-20T10:30:00", a.fechaHoraCreacion);
    }

    @Test
    @DisplayName("Asignación correcta de fecha de última modificación")
    public void testAsignacionFechaUltimaMod() {
        Almacen a = new Almacen();
        a.fechaHoraUltimaMod = "2026-04-10T14:00:00";
        assertEquals("2026-04-10T14:00:00", a.fechaHoraUltimaMod);
    }

    @Test
    @DisplayName("Asignación correcta de último usuario")
    public void testAsignacionUltimoUsuario() {
        Almacen a = new Almacen();
        a.ultimoUsuario = "ADMIN";
        assertEquals("ADMIN", a.ultimoUsuario);
    }

    @Test
    @DisplayName("Asignación de todos los campos simultáneamente")
    public void testAsignacionTodosCampos() {
        Almacen a = new Almacen();
        a.id = 100;
        a.nombre = "Almacén Norte";
        a.ubicacion = "Hermosillo, Sonora";
        a.fechaHoraCreacion = "2026-01-01T00:00:00";
        a.fechaHoraUltimaMod = "2026-04-10T12:00:00";
        a.ultimoUsuario = "PRODUCTOS";

        assertEquals(100, a.id);
        assertEquals("Almacén Norte", a.nombre);
        assertEquals("Hermosillo, Sonora", a.ubicacion);
        assertEquals("2026-01-01T00:00:00", a.fechaHoraCreacion);
        assertEquals("2026-04-10T12:00:00", a.fechaHoraUltimaMod);
        assertEquals("PRODUCTOS", a.ultimoUsuario);
    }

    // ============================================================
    // 3. VALORES DE BORDE (boundary values)
    // ============================================================

    @Test
    @DisplayName("Borde: id con valor cero")
    public void testIdCero() {
        Almacen a = new Almacen();
        a.id = 0;
        assertEquals(0, a.id);
    }

    @Test
    @DisplayName("Borde: id negativo")
    public void testIdNegativo() {
        Almacen a = new Almacen();
        a.id = -1;
        assertEquals(-1, a.id);
    }

    @Test
    @DisplayName("Borde: id con valor máximo de int")
    public void testIdMaxInt() {
        Almacen a = new Almacen();
        a.id = Integer.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, a.id);
    }

    @Test
    @DisplayName("Borde: id con valor mínimo de int")
    public void testIdMinInt() {
        Almacen a = new Almacen();
        a.id = Integer.MIN_VALUE;
        assertEquals(Integer.MIN_VALUE, a.id);
    }

    @Test
    @DisplayName("Borde: nombre es cadena vacía")
    public void testNombreCadenaVacia() {
        Almacen a = new Almacen();
        a.nombre = "";
        assertEquals("", a.nombre);
    }

    @Test
    @DisplayName("Borde: ubicación es cadena vacía")
    public void testUbicacionCadenaVacia() {
        Almacen a = new Almacen();
        a.ubicacion = "";
        assertEquals("", a.ubicacion);
    }

    @Test
    @DisplayName("Borde: nombre con un solo carácter")
    public void testNombreUnCaracter() {
        Almacen a = new Almacen();
        a.nombre = "A";
        assertEquals("A", a.nombre);
    }

    @Test
    @DisplayName("Borde: nombre con cadena muy larga (1000 caracteres)")
    public void testNombreMuyLargo() {
        Almacen a = new Almacen();
        String largo = "A".repeat(1000);
        a.nombre = largo;
        assertEquals(1000, a.nombre.length());
        assertEquals(largo, a.nombre);
    }

    // ============================================================
    // 4. VALORES NULOS explícitos
    // ============================================================

    @Test
    @DisplayName("Asignar null explícito al nombre")
    public void testNombreNullExplicito() {
        Almacen a = new Almacen();
        a.nombre = "Temporal";
        a.nombre = null;
        assertNull(a.nombre);
    }

    @Test
    @DisplayName("Asignar null explícito a la ubicación")
    public void testUbicacionNullExplicito() {
        Almacen a = new Almacen();
        a.ubicacion = "Sonora";
        a.ubicacion = null;
        assertNull(a.ubicacion);
    }

    @Test
    @DisplayName("Asignar null explícito a ultimoUsuario")
    public void testUltimoUsuarioNullExplicito() {
        Almacen a = new Almacen();
        a.ultimoUsuario = "ADMIN";
        a.ultimoUsuario = null;
        assertNull(a.ultimoUsuario);
    }

    // ============================================================
    // 5. CARACTERES ESPECIALES Y UNICODE
    // ============================================================

    @Test
    @DisplayName("Nombre con acentos y ñ")
    public void testNombreConAcentos() {
        Almacen a = new Almacen();
        a.nombre = "Almacén Ñoño";
        assertEquals("Almacén Ñoño", a.nombre);
    }

    @Test
    @DisplayName("Ubicación con caracteres Unicode")
    public void testUbicacionUnicode() {
        Almacen a = new Almacen();
        a.ubicacion = "Tokio 東京, Japón 日本";
        assertEquals("Tokio 東京, Japón 日本", a.ubicacion);
    }

    @Test
    @DisplayName("Nombre con caracteres especiales SQL-injection-like")
    public void testNombreCaracteresEspeciales() {
        Almacen a = new Almacen();
        a.nombre = "'; DROP TABLE almacenes; --";
        assertEquals("'; DROP TABLE almacenes; --", a.nombre);
    }

    @Test
    @DisplayName("Nombre con saltos de línea y tabulaciones")
    public void testNombreConSaltosLinea() {
        Almacen a = new Almacen();
        a.nombre = "Almacén\nCentral\tNorte";
        assertEquals("Almacén\nCentral\tNorte", a.nombre);
    }

    @Test
    @DisplayName("Nombre con solo espacios en blanco")
    public void testNombreSoloEspacios() {
        Almacen a = new Almacen();
        a.nombre = "   ";
        assertEquals("   ", a.nombre);
    }

    // ============================================================
    // 6. MÚLTIPLES INSTANCIAS (independencia de objetos)
    // ============================================================

    @Test
    @DisplayName("Dos instancias de Almacen son independientes")
    public void testInstanciasIndependientes() {
        Almacen a1 = new Almacen();
        Almacen a2 = new Almacen();
        a1.id = 1;
        a1.nombre = "Almacén A";
        a2.id = 2;
        a2.nombre = "Almacén B";

        assertNotEquals(a1.id, a2.id);
        assertNotEquals(a1.nombre, a2.nombre);
    }

    @Test
    @DisplayName("Modificar una instancia no afecta a otra")
    public void testModificarUnaInstanciaNoAfectaOtra() {
        Almacen a1 = new Almacen();
        Almacen a2 = new Almacen();
        a1.nombre = "Original";
        a2.nombre = "Original";
        a1.nombre = "Modificado";

        assertEquals("Modificado", a1.nombre);
        assertEquals("Original", a2.nombre);
    }

    // ============================================================
    // 7. SOBREESCRITURA DE VALORES
    // ============================================================

    @Test
    @DisplayName("Sobreescribir id mantiene el último valor")
    public void testSobreescribirId() {
        Almacen a = new Almacen();
        a.id = 10;
        a.id = 20;
        a.id = 30;
        assertEquals(30, a.id);
    }

    @Test
    @DisplayName("Sobreescribir nombre mantiene el último valor")
    public void testSobreescribirNombre() {
        Almacen a = new Almacen();
        a.nombre = "Primero";
        a.nombre = "Segundo";
        assertEquals("Segundo", a.nombre);
    }
}
