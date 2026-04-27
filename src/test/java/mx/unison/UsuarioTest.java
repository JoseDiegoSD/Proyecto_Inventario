package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias exhaustivas para la clase Usuario.
 * Cubre: valores por defecto, asignación de campos, valores nulos,
 * valores vacíos, caracteres especiales, independencia de instancias,
 * sobreescritura de valores.
 */
public class UsuarioTest {

    // ============================================================
    // 1. VALORES POR DEFECTO
    // ============================================================

    @Test
    @DisplayName("Valores por defecto: nombre debe ser null")
    public void testValorPorDefectoNombre() {
        Usuario u = new Usuario();
        assertNull(u.nombre, "El nombre por defecto debe ser null");
    }

    @Test
    @DisplayName("Valores por defecto: rol debe ser null")
    public void testValorPorDefectoRol() {
        Usuario u = new Usuario();
        assertNull(u.rol, "El rol por defecto debe ser null");
    }

    // ============================================================
    // 2. ASIGNACIÓN DE CAMPOS VÁLIDOS
    // ============================================================

    @Test
    @DisplayName("Asignación correcta de nombre y rol ADMIN")
    public void testAsignacionAdmin() {
        Usuario u = new Usuario();
        u.nombre = "ADMIN";
        u.rol = "ADMIN";
        assertEquals("ADMIN", u.nombre);
        assertEquals("ADMIN", u.rol);
    }

    @Test
    @DisplayName("Asignación correcta de nombre y rol PRODUCTOS")
    public void testAsignacionProductos() {
        Usuario u = new Usuario();
        u.nombre = "PRODUCTOS";
        u.rol = "PRODUCTOS";
        assertEquals("PRODUCTOS", u.nombre);
        assertEquals("PRODUCTOS", u.rol);
    }

    @Test
    @DisplayName("Asignación correcta de nombre y rol ALMACENES")
    public void testAsignacionAlmacenes() {
        Usuario u = new Usuario();
        u.nombre = "ALMACENES";
        u.rol = "ALMACENES";
        assertEquals("ALMACENES", u.nombre);
        assertEquals("ALMACENES", u.rol);
    }

    @Test
    @DisplayName("Nombre y rol pueden ser distintos")
    public void testNombreYRolDistintos() {
        Usuario u = new Usuario();
        u.nombre = "juan_perez";
        u.rol = "ADMIN";
        assertEquals("juan_perez", u.nombre);
        assertEquals("ADMIN", u.rol);
    }

    // ============================================================
    // 3. VALORES NULOS EXPLÍCITOS
    // ============================================================

    @Test
    @DisplayName("Asignar null explícito al nombre después de un valor")
    public void testNombreNullExplicito() {
        Usuario u = new Usuario();
        u.nombre = "UsuarioTemp";
        u.nombre = null;
        assertNull(u.nombre);
    }

    @Test
    @DisplayName("Asignar null explícito al rol después de un valor")
    public void testRolNullExplicito() {
        Usuario u = new Usuario();
        u.rol = "ADMIN";
        u.rol = null;
        assertNull(u.rol);
    }

    @Test
    @DisplayName("Ambos campos nulos simultáneamente")
    public void testAmbosCamposNull() {
        Usuario u = new Usuario();
        u.nombre = null;
        u.rol = null;
        assertNull(u.nombre);
        assertNull(u.rol);
    }

    // ============================================================
    // 4. CADENAS VACÍAS
    // ============================================================

    @Test
    @DisplayName("Nombre cadena vacía")
    public void testNombreCadenaVacia() {
        Usuario u = new Usuario();
        u.nombre = "";
        assertEquals("", u.nombre);
    }

    @Test
    @DisplayName("Rol cadena vacía")
    public void testRolCadenaVacia() {
        Usuario u = new Usuario();
        u.rol = "";
        assertEquals("", u.rol);
    }

    @Test
    @DisplayName("Nombre con solo espacios en blanco")
    public void testNombreSoloEspacios() {
        Usuario u = new Usuario();
        u.nombre = "   ";
        assertEquals("   ", u.nombre);
    }

    // ============================================================
    // 5. CARACTERES ESPECIALES Y UNICODE
    // ============================================================

    @Test
    @DisplayName("Nombre con acentos")
    public void testNombreConAcentos() {
        Usuario u = new Usuario();
        u.nombre = "José García";
        assertEquals("José García", u.nombre);
    }

    @Test
    @DisplayName("Nombre con caracteres especiales")
    public void testNombreConCaracteresEspeciales() {
        Usuario u = new Usuario();
        u.nombre = "user@domain.com";
        assertEquals("user@domain.com", u.nombre);
    }

    @Test
    @DisplayName("Rol con caracteres especiales")
    public void testRolConCaracteresEspeciales() {
        Usuario u = new Usuario();
        u.rol = "SUPER_ADMIN/ROOT";
        assertEquals("SUPER_ADMIN/ROOT", u.rol);
    }

    @Test
    @DisplayName("Nombre muy largo (500 caracteres)")
    public void testNombreMuyLargo() {
        Usuario u = new Usuario();
        String largo = "U".repeat(500);
        u.nombre = largo;
        assertEquals(500, u.nombre.length());
    }

    @Test
    @DisplayName("Nombre con un solo carácter")
    public void testNombreUnCaracter() {
        Usuario u = new Usuario();
        u.nombre = "A";
        assertEquals("A", u.nombre);
    }

    // ============================================================
    // 6. INDEPENDENCIA DE INSTANCIAS
    // ============================================================

    @Test
    @DisplayName("Dos instancias de Usuario son independientes")
    public void testInstanciasIndependientes() {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        u1.nombre = "Admin";
        u1.rol = "ADMIN";
        u2.nombre = "Operador";
        u2.rol = "PRODUCTOS";

        assertEquals("Admin", u1.nombre);
        assertEquals("Operador", u2.nombre);
        assertNotEquals(u1.nombre, u2.nombre);
        assertNotEquals(u1.rol, u2.rol);
    }

    @Test
    @DisplayName("Modificar un usuario no afecta al otro")
    public void testModificarNoAfectaOtro() {
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        u1.nombre = "Mismo";
        u2.nombre = "Mismo";
        u1.nombre = "Diferente";

        assertEquals("Diferente", u1.nombre);
        assertEquals("Mismo", u2.nombre);
    }

    // ============================================================
    // 7. SOBREESCRITURA DE VALORES
    // ============================================================

    @Test
    @DisplayName("Sobreescribir nombre mantiene último valor")
    public void testSobreescribirNombre() {
        Usuario u = new Usuario();
        u.nombre = "Primero";
        u.nombre = "Segundo";
        u.nombre = "Tercero";
        assertEquals("Tercero", u.nombre);
    }

    @Test
    @DisplayName("Sobreescribir rol mantiene último valor")
    public void testSobreescribirRol() {
        Usuario u = new Usuario();
        u.rol = "ADMIN";
        u.rol = "PRODUCTOS";
        u.rol = "ALMACENES";
        assertEquals("ALMACENES", u.rol);
    }

    // ============================================================
    // 8. ESCENARIOS DE NEGOCIO
    // ============================================================

    @Test
    @DisplayName("Un usuario recién creado no debería tener asignaciones")
    public void testUsuarioRecienCreado() {
        Usuario u = new Usuario();
        assertAll("Un usuario recién instanciado no tiene datos",
            () -> assertNull(u.nombre),
            () -> assertNull(u.rol)
        );
    }

    @Test
    @DisplayName("Verificar que rol distingue mayúsculas de minúsculas")
    public void testRolCaseSensitive() {
        Usuario u = new Usuario();
        u.rol = "admin";
        assertNotEquals("ADMIN", u.rol, "El rol debe distinguir entre mayúsculas y minúsculas");
    }
}
