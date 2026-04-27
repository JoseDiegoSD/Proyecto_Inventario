package mx.unison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

/**
 * Pruebas unitarias para la clase Main.
 * Cubre: existencia del método main, firma correcta,
 * que es público y estático, que la clase se puede instanciar.
 */
public class MainTest {

    // ============================================================
    // 1. EXISTENCIA Y FIRMA DEL MÉTODO main
    // ============================================================

    @Test
    @DisplayName("La clase Main existe y se puede cargar")
    public void testClaseMainExiste() {
        assertDoesNotThrow(() -> Class.forName("mx.unison.Main"),
            "La clase Main debe existir en el paquete mx.unison");
    }

    @Test
    @DisplayName("El método main existe con la firma correcta (String[])")
    public void testMetodoMainExiste() {
        assertDoesNotThrow(() -> {
            Method m = Main.class.getMethod("main", String[].class);
            assertNotNull(m);
        }, "El método main(String[]) debe existir");
    }

    @Test
    @DisplayName("El método main es público")
    public void testMetodoMainEsPublico() throws Exception {
        Method m = Main.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(m.getModifiers()),
            "main debe ser público");
    }

    @Test
    @DisplayName("El método main es estático")
    public void testMetodoMainEsEstatico() throws Exception {
        Method m = Main.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(m.getModifiers()),
            "main debe ser estático");
    }

    @Test
    @DisplayName("El método main retorna void")
    public void testMetodoMainRetornaVoid() throws Exception {
        Method m = Main.class.getMethod("main", String[].class);
        assertEquals(void.class, m.getReturnType(), "main debe retornar void");
    }

    // ============================================================
    // 2. INSTANCIACIÓN DE LA CLASE
    // ============================================================

    @Test
    @DisplayName("La clase Main se puede instanciar (constructor por defecto)")
    public void testMainSePuedeInstanciar() {
        assertDoesNotThrow(() -> new Main(),
            "Main debe tener un constructor público por defecto");
    }

    // ============================================================
    // 3. ESTRUCTURA DE LA CLASE
    // ============================================================

    @Test
    @DisplayName("Main no tiene campos de instancia (solo método main)")
    public void testMainNoTieneCamposInstancia() {
        assertEquals(0, Main.class.getDeclaredFields().length,
            "Main no debería tener campos declarados, solo el método main");
    }

    @Test
    @DisplayName("Main tiene el método main entre sus métodos declarados")
    public void testMainTieneMetodoMain() {
        // getDeclaredMethods puede incluir métodos sintéticos generados por lambdas
        Method[] metodos = Main.class.getDeclaredMethods();
        boolean tieneMain = false;
        for (Method m : metodos) {
            if (m.getName().equals("main")) {
                tieneMain = true;
                break;
            }
        }
        assertTrue(tieneMain, "Main debe tener el método main entre sus métodos declarados");
    }
}
