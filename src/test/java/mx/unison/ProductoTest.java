package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias exhaustivas para la clase Producto.
 * Cubre: valores por defecto, campos válidos, valores nulos,
 * valores de borde (int y double), caracteres especiales, 
 * precisión de punto flotante, independencia de instancias.
 */
public class ProductoTest {

    // ============================================================
    // 1. VALORES POR DEFECTO
    // ============================================================

    @Test
    @DisplayName("Valores por defecto: id debe ser 0")
    public void testValorPorDefectoId() {
        Producto p = new Producto();
        assertEquals(0, p.id);
    }

    @Test
    @DisplayName("Valores por defecto: nombre debe ser null")
    public void testValorPorDefectoNombre() {
        Producto p = new Producto();
        assertNull(p.nombre);
    }

    @Test
    @DisplayName("Valores por defecto: descripcion debe ser null")
    public void testValorPorDefectoDescripcion() {
        Producto p = new Producto();
        assertNull(p.descripcion);
    }

    @Test
    @DisplayName("Valores por defecto: cantidad debe ser 0")
    public void testValorPorDefectoCantidad() {
        Producto p = new Producto();
        assertEquals(0, p.cantidad);
    }

    @Test
    @DisplayName("Valores por defecto: precio debe ser 0.0")
    public void testValorPorDefectoPrecio() {
        Producto p = new Producto();
        assertEquals(0.0, p.precio, 0.001);
    }

    @Test
    @DisplayName("Valores por defecto: almacenId debe ser 0")
    public void testValorPorDefectoAlmacenId() {
        Producto p = new Producto();
        assertEquals(0, p.almacenId);
    }

    @Test
    @DisplayName("Valores por defecto: almacenNombre debe ser null")
    public void testValorPorDefectoAlmacenNombre() {
        Producto p = new Producto();
        assertNull(p.almacenNombre);
    }

    @Test
    @DisplayName("Valores por defecto: fechaCreacion debe ser null")
    public void testValorPorDefectoFechaCreacion() {
        Producto p = new Producto();
        assertNull(p.fechaCreacion);
    }

    @Test
    @DisplayName("Valores por defecto: fechaModificacion debe ser null")
    public void testValorPorDefectoFechaModificacion() {
        Producto p = new Producto();
        assertNull(p.fechaModificacion);
    }

    @Test
    @DisplayName("Valores por defecto: ultimoUsuario debe ser null")
    public void testValorPorDefectoUltimoUsuario() {
        Producto p = new Producto();
        assertNull(p.ultimoUsuario);
    }

    // ============================================================
    // 2. ASIGNACIÓN Y LECTURA DE CAMPOS VÁLIDOS
    // ============================================================

    @Test
    @DisplayName("Asignación correcta de todos los campos")
    public void testAsignacionTodosCampos() {
        Producto p = new Producto();
        p.id = 1;
        p.nombre = "Laptop HP";
        p.descripcion = "Laptop empresarial 14 pulgadas";
        p.cantidad = 10;
        p.precio = 15000.50;
        p.almacenId = 2;
        p.almacenNombre = "Bodega Central";
        p.fechaCreacion = "2026-03-20T10:00:00";
        p.fechaModificacion = "2026-04-10T14:00:00";
        p.ultimoUsuario = "ADMIN";

        assertEquals(1, p.id);
        assertEquals("Laptop HP", p.nombre);
        assertEquals("Laptop empresarial 14 pulgadas", p.descripcion);
        assertEquals(10, p.cantidad);
        assertEquals(15000.50, p.precio, 0.001);
        assertEquals(2, p.almacenId);
        assertEquals("Bodega Central", p.almacenNombre);
        assertEquals("2026-03-20T10:00:00", p.fechaCreacion);
        assertEquals("2026-04-10T14:00:00", p.fechaModificacion);
        assertEquals("ADMIN", p.ultimoUsuario);
    }

    // ============================================================
    // 3. VALORES DE BORDE: campo id (int)
    // ============================================================

    @Test
    @DisplayName("Borde: id = 0")
    public void testIdCero() {
        Producto p = new Producto();
        p.id = 0;
        assertEquals(0, p.id);
    }

    @Test
    @DisplayName("Borde: id negativo")
    public void testIdNegativo() {
        Producto p = new Producto();
        p.id = -1;
        assertEquals(-1, p.id);
    }

    @Test
    @DisplayName("Borde: id = Integer.MAX_VALUE")
    public void testIdMaxInt() {
        Producto p = new Producto();
        p.id = Integer.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, p.id);
    }

    @Test
    @DisplayName("Borde: id = Integer.MIN_VALUE")
    public void testIdMinInt() {
        Producto p = new Producto();
        p.id = Integer.MIN_VALUE;
        assertEquals(Integer.MIN_VALUE, p.id);
    }

    // ============================================================
    // 4. VALORES DE BORDE: campo cantidad (int)
    // ============================================================

    @Test
    @DisplayName("Borde: cantidad = 0 (inventario vacío)")
    public void testCantidadCero() {
        Producto p = new Producto();
        p.cantidad = 0;
        assertEquals(0, p.cantidad);
    }

    @Test
    @DisplayName("Borde: cantidad = 1 (mínimo positivo)")
    public void testCantidadUno() {
        Producto p = new Producto();
        p.cantidad = 1;
        assertEquals(1, p.cantidad);
    }

    @Test
    @DisplayName("Borde: cantidad negativa (podría ser devolución)")
    public void testCantidadNegativa() {
        Producto p = new Producto();
        p.cantidad = -5;
        assertEquals(-5, p.cantidad);
    }

    @Test
    @DisplayName("Borde: cantidad muy grande")
    public void testCantidadMuyGrande() {
        Producto p = new Producto();
        p.cantidad = 999999;
        assertEquals(999999, p.cantidad);
    }

    @Test
    @DisplayName("Borde: cantidad = Integer.MAX_VALUE")
    public void testCantidadMaxInt() {
        Producto p = new Producto();
        p.cantidad = Integer.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, p.cantidad);
    }

    // ============================================================
    // 5. VALORES DE BORDE: campo precio (double)
    // ============================================================

    @Test
    @DisplayName("Borde: precio = 0.0 (gratis)")
    public void testPrecioCero() {
        Producto p = new Producto();
        p.precio = 0.0;
        assertEquals(0.0, p.precio, 0.001);
    }

    @Test
    @DisplayName("Borde: precio muy pequeño (centavos)")
    public void testPrecioCentavos() {
        Producto p = new Producto();
        p.precio = 0.01;
        assertEquals(0.01, p.precio, 0.001);
    }

    @Test
    @DisplayName("Borde: precio negativo")
    public void testPrecioNegativo() {
        Producto p = new Producto();
        p.precio = -100.0;
        assertEquals(-100.0, p.precio, 0.001);
    }

    @Test
    @DisplayName("Borde: precio muy grande")
    public void testPrecioMuyGrande() {
        Producto p = new Producto();
        p.precio = 9999999.99;
        assertEquals(9999999.99, p.precio, 0.001);
    }

    @Test
    @DisplayName("Borde: precio = Double.MAX_VALUE")
    public void testPrecioMaxDouble() {
        Producto p = new Producto();
        p.precio = Double.MAX_VALUE;
        assertEquals(Double.MAX_VALUE, p.precio, 0.0);
    }

    @Test
    @DisplayName("Borde: precio = Double.MIN_VALUE (mínimo positivo representable)")
    public void testPrecioMinDouble() {
        Producto p = new Producto();
        p.precio = Double.MIN_VALUE;
        assertEquals(Double.MIN_VALUE, p.precio, 0.0);
    }

    @Test
    @DisplayName("Borde: precio con muchos decimales de precisión")
    public void testPrecioPrecision() {
        Producto p = new Producto();
        p.precio = 123.456789;
        assertEquals(123.456789, p.precio, 0.000001);
    }

    // ============================================================
    // 6. VALORES DE BORDE: campo almacenId
    // ============================================================

    @Test
    @DisplayName("Borde: almacenId = 0 (sin almacén)")
    public void testAlmacenIdCero() {
        Producto p = new Producto();
        p.almacenId = 0;
        assertEquals(0, p.almacenId);
    }

    @Test
    @DisplayName("Borde: almacenId negativo")
    public void testAlmacenIdNegativo() {
        Producto p = new Producto();
        p.almacenId = -1;
        assertEquals(-1, p.almacenId);
    }

    // ============================================================
    // 7. CADENAS NULAS Y VACÍAS
    // ============================================================

    @Test
    @DisplayName("Nombre nulo explícito")
    public void testNombreNull() {
        Producto p = new Producto();
        p.nombre = null;
        assertNull(p.nombre);
    }

    @Test
    @DisplayName("Nombre cadena vacía")
    public void testNombreVacio() {
        Producto p = new Producto();
        p.nombre = "";
        assertEquals("", p.nombre);
    }

    @Test
    @DisplayName("Descripción nula")
    public void testDescripcionNull() {
        Producto p = new Producto();
        p.descripcion = null;
        assertNull(p.descripcion);
    }

    @Test
    @DisplayName("Descripción cadena vacía")
    public void testDescripcionVacia() {
        Producto p = new Producto();
        p.descripcion = "";
        assertEquals("", p.descripcion);
    }

    @Test
    @DisplayName("AlmacenNombre nulo (producto sin almacén)")
    public void testAlmacenNombreNull() {
        Producto p = new Producto();
        p.almacenNombre = null;
        assertNull(p.almacenNombre);
    }

    // ============================================================
    // 8. CARACTERES ESPECIALES Y UNICODE
    // ============================================================

    @Test
    @DisplayName("Nombre con acentos y ñ")
    public void testNombreConAcentos() {
        Producto p = new Producto();
        p.nombre = "Lámina pequeña";
        assertEquals("Lámina pequeña", p.nombre);
    }

    @Test
    @DisplayName("Descripción con caracteres Unicode (emojis)")
    public void testDescripcionConEmojis() {
        Producto p = new Producto();
        p.descripcion = "Producto especial ⭐🔥";
        assertEquals("Producto especial ⭐🔥", p.descripcion);
    }

    @Test
    @DisplayName("Nombre con caracteres especiales")
    public void testNombreCaracteresEspeciales() {
        Producto p = new Producto();
        p.nombre = "Cable USB-C 3.0 (2m) #Premium";
        assertEquals("Cable USB-C 3.0 (2m) #Premium", p.nombre);
    }

    @Test
    @DisplayName("Nombre con cadena muy larga (1000 chars)")
    public void testNombreMuyLargo() {
        Producto p = new Producto();
        String largo = "X".repeat(1000);
        p.nombre = largo;
        assertEquals(1000, p.nombre.length());
    }

    // ============================================================
    // 9. INDEPENDENCIA DE INSTANCIAS
    // ============================================================

    @Test
    @DisplayName("Dos productos son instancias independientes")
    public void testInstanciasIndependientes() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        p1.nombre = "Producto A";
        p1.precio = 100.0;
        p2.nombre = "Producto B";
        p2.precio = 200.0;

        assertNotEquals(p1.nombre, p2.nombre);
        assertNotEquals(p1.precio, p2.precio);
    }

    @Test
    @DisplayName("Modificar un producto no afecta al otro")
    public void testModificarNoAfectaOtro() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        p1.cantidad = 10;
        p2.cantidad = 10;
        p1.cantidad = 0;

        assertEquals(0, p1.cantidad);
        assertEquals(10, p2.cantidad);
    }

    // ============================================================
    // 10. SOBREESCRITURA DE VALORES
    // ============================================================

    @Test
    @DisplayName("Sobreescribir precio mantiene último valor")
    public void testSobreescribirPrecio() {
        Producto p = new Producto();
        p.precio = 50.0;
        p.precio = 75.0;
        p.precio = 99.99;
        assertEquals(99.99, p.precio, 0.001);
    }

    @Test
    @DisplayName("Sobreescribir cantidad mantiene último valor")
    public void testSobreescribirCantidad() {
        Producto p = new Producto();
        p.cantidad = 5;
        p.cantidad = 10;
        assertEquals(10, p.cantidad);
    }

    @Test
    @DisplayName("Sobreescribir nombre de null a valor y de vuelta a null")
    public void testSobreescribirNombreNullValorNull() {
        Producto p = new Producto();
        assertNull(p.nombre);
        p.nombre = "Laptop";
        assertEquals("Laptop", p.nombre);
        p.nombre = null;
        assertNull(p.nombre);
    }
}
